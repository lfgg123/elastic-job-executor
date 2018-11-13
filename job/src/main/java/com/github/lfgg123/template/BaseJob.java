package com.github.lfgg123.template;

import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.internal.storage.JobNodePath;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.github.lfgg123.compiler.JavaStringCompiler;
import com.github.lfgg123.exception.JobOperationException;
import com.github.lfgg123.model.SchedulerJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述：
 * 任务模板实现类
 *
 * @author chentianlong
 * @date 2018/06/19 14:57
 */
public abstract class BaseJob implements JobOperation {

    private final Logger logger = LoggerFactory.getLogger(BaseJob.class);

    @Resource
    private ZookeeperRegistryCenter regCenter;
    @Resource
    private JobEventConfiguration jobEventConfiguration;
    @Resource
    private JavaStringCompiler compiler;

    private static final Pattern MATCHER = Pattern.compile("[a-zA-z]");

    private static final Pattern PATTERN = Pattern.compile("\\$\\{([^\\}]+)\\}");

    @Value("${package.prefix}")
    private String packagePrefix;

    private static final String DISABLED = "DISABLED";

    /**
     * 获取模板
     */
    protected abstract String getTemplate();

    /**
     * 启动job
     *
     * @param config   配置信息
     * @param disabled 是否生效
     */
    public abstract void startup(SchedulerJob config, boolean disabled);

    @Override
    public void register(Class<?> clazz, SchedulerJob config, boolean disabled) {
        String params = JSON.toJSONString(config);
        logger.info("作业:{}准备注册,作业参数为{}", config.getJobName(), params);
        // 定义作业核心配置
        JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder(config.getJobName(), config.getCron(), config.getShardingTotalCount())
                .failover(config.isFailover())
                .description(config.getDescription())
                .shardingItemParameters(config.getShardingItemParameters())
                .build();
        // 定义SIMPLE类型配置
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, clazz.getCanonicalName());
        // 定义Lite作业根配置
        String ip = "";
        boolean jobStatus = false;
        JobNodePath jobNodePath = new JobNodePath(config.getJobName());
        try {
            InetAddress address = InetAddress.getLocalHost();
            ip = address.getHostAddress();
            logger.info("本机ip为:{}", ip);
            //不覆盖失效状态
            for (String each : regCenter.getChildrenKeys(jobNodePath.getServerNodePath())) {
                logger.info("任务:{}的节点ip为:{},当前状态为{}", config.getJobName(), each, regCenter.get(jobNodePath.getServerNodePath(each)));
                if (DISABLED.equals(regCenter.get(jobNodePath.getServerNodePath(each))) && each.equals(ip)) {
                    jobStatus = true;
                }
            }
        } catch (UnknownHostException e) {
            logger.error("获取ip失败", e);
        }
        LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).monitorExecution(config.isMonitorExecution()).monitorPort(config.getMonitorPort()).build();
        new JobScheduler(regCenter, simpleJobRootConfig, clazz, jobEventConfiguration).init();
        if (jobStatus) {
            regCenter.persist(jobNodePath.getServerNodePath(ip), DISABLED);
        }
        //当pre环境时，将所有job都置为失效状态
        if (disabled) {
            for (String each : regCenter.getChildrenKeys(jobNodePath.getServerNodePath())) {
                regCenter.persist(jobNodePath.getServerNodePath(each), DISABLED);
            }
        }
        logger.info("作业:{}注册成功", config.getJobName());
    }

    @Override
    public Class<?> compiler(String fileName, String source) {
        if (StringUtils.isEmpty(source)) {
            throw new JobOperationException("source code can not be null!");
        }
        if (!MATCHER.matcher(fileName).find()) {
            throw new JobOperationException("jobName必须为英文字母组成");
        }
        try {
            Map<String, byte[]> results = compiler.compile(fileName + ".java", source);
            return compiler.loadClass(packagePrefix + fileName, results);
        } catch (ClassNotFoundException | IOException e) {
            logger.error("compiler source error!", e);
            throw new JobOperationException("compiler source error!");
        }
    }

    @Override
    public String resolve(String template, SchedulerJob config) {
        return parseStringValue(template, config);
    }

    private String parseStringValue(String template, SchedulerJob config) {
        if (StringUtils.isEmpty(template)) {
            throw new JobOperationException("template can not be null!");
        }
        Matcher matcher = PATTERN.matcher(template);
        StringBuffer stringBuffer = new StringBuffer();
        doMatch(config, matcher, stringBuffer);

        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();

    }

    private void doMatch(SchedulerJob config, Matcher matcher, StringBuffer stringBuffer) {
        while (matcher.find()) {
            try {
                String matcherKey = matcher.group(1);
                Field field = config.getClass().getDeclaredField(matcherKey);
                field.setAccessible(true);
                String matcherValue = (String) field.get(config);
                if (StringUtils.isEmpty(matcherValue)) {
                    if (!"jobParameter".equals(matcherKey)) {
                        throw new JobOperationException("必要参数不能为空!");
                    } else {
                        matcherValue = "";
                    }
                }
                matcher.appendReplacement(stringBuffer, matcherValue);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                logger.error("resolve template error!", e);
                throw new JobOperationException("resolve template error!");
            }
        }
    }
}
