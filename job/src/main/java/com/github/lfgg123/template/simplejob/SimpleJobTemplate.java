package com.github.lfgg123.template.simplejob;

import com.github.lfgg123.constant.Constant;
import com.github.lfgg123.exception.JobOperationException;
import com.github.lfgg123.model.SchedulerJob;
import com.github.lfgg123.template.BaseJob;
import com.github.lfgg123.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * 描述：
 * simple任务模板
 *
 * @author chentianlong
 * @date 2018/06/19 14:57
 */
@Component
public class SimpleJobTemplate extends BaseJob {

    private final Logger logger = LoggerFactory.getLogger(SimpleJobTemplate.class);

    private static StringBuilder source = new StringBuilder();

    @Override
    protected String getTemplate() {
        if (StringUtils.isEmpty(source.toString())) {
            File template = FileUtil.getJarResourceFile(Constant.JOB_PATH).get(0);
            if (template.isFile() && template.exists()) {
                try (InputStreamReader read = new InputStreamReader(new FileInputStream(template), Constant.UTF8);
                     BufferedReader bufferedReader = new BufferedReader(read)) {
                    String lineTxt = bufferedReader.readLine();
                    while (!StringUtils.isEmpty(lineTxt)) {
                        source.append(lineTxt);
                        lineTxt = bufferedReader.readLine();
                    }
                } catch (Exception e) {
                    logger.error("读取文件失败", e);
                    throw new JobOperationException("读取文件失败");
                }
            } else {
                throw new JobOperationException("模板文件不存在");
            }
        }
        return source.toString();
    }


    @Override
    public void startup(SchedulerJob config, boolean disabled) {
        try {
            String templateSource = resolve(getTemplate(), config);
            logger.info(templateSource);
            Class<?> clazz = compiler(config.getJobName(), templateSource);
            register(clazz, config, disabled);
        } catch (Exception e) {
            logger.error("启动失败", e);
        }
    }
}
