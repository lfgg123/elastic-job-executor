package com.github.lfgg123.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 描述：
 * 任务配置类
 *
 * @author chentianlong
 * @date 2018/06/25 14:57
 */
public class SchedulerJob {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 作业名称(英文，大驼峰命名)
     */
    private String jobName;
    /**
     * dubbo接口class全路径
     */
    private String dubboClass;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 分片总数
     */
    private int shardingTotalCount;
    /**
     * 作业触发时间表达式
     */
    private String cron;
    /**
     * 分片序列号和参数用等号分隔，多个键值对用逗号分隔
     */
    private String shardingItemParameters;
    /**
     * 作业自定义参数
     */
    private String jobParameter;

    /**
     * 监控端口
     */
    private Integer monitorPort;
    /**
     * 监控作业运行时状态
     */
    private boolean monitorExecution;
    /**
     * 是否开启失效转移
     */
    private boolean failover;
    /**
     * 作业描述
     */
    private String description;
    /**
     * 创建时间
     */
    private Date createAt;
    /**
     * 更新时间
     */
    private Date updateAt;

    public Integer getMonitorPort() {
        return monitorPort;
    }

    public void setMonitorPort(Integer monitorPort) {
        this.monitorPort = monitorPort;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getDubboClass() {
        return dubboClass;
    }

    public void setDubboClass(String dubboClass) {
        this.dubboClass = dubboClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getShardingTotalCount() {
        return shardingTotalCount;
    }

    public void setShardingTotalCount(int shardingTotalCount) {
        this.shardingTotalCount = shardingTotalCount;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getShardingItemParameters() {
        return shardingItemParameters;
    }

    public void setShardingItemParameters(String shardingItemParameters) {
        this.shardingItemParameters = shardingItemParameters;
    }

    public String getJobParameter() {
        return jobParameter;
    }

    public void setJobParameter(String jobParameter) {
        this.jobParameter = jobParameter;
    }

    public boolean isMonitorExecution() {
        return monitorExecution;
    }

    public void setMonitorExecution(boolean monitorExecution) {
        this.monitorExecution = monitorExecution;
    }

    public boolean isFailover() {
        return failover;
    }

    public void setFailover(boolean failover) {
        this.failover = failover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}
