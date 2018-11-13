package com.github.lfgg123.template;

import com.github.lfgg123.model.SchedulerJob;

/**
 * 描述：
 * 任务操作类
 *
 * @author chentianlong
 * @date 2018/06/19 14:11
 */
public interface JobOperation {
    /**
     * 注册
     *
     * @param clazz
     * @param config
     * @param disabled
     */
    void register(Class<?> clazz, SchedulerJob config, boolean disabled);

    /**
     * 编译
     *
     * @param fileName
     * @param source
     * @return
     */
    Class<?> compiler(String fileName, String source);

    /**
     * 解析模板
     *
     * @param template
     * @param config
     * @return
     */
    String resolve(String template, SchedulerJob config);
}
