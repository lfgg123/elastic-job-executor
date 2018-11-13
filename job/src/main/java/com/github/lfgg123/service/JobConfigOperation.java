package com.github.lfgg123.service;


import com.github.lfgg123.model.SchedulerJob;

import java.util.List;

/**
 * 描述：
 * 任务配置类
 *
 * @author chentianlong
 * @date 2018/06/25 14:57
 */
public interface JobConfigOperation {

    List<SchedulerJob> getAllJobs();
}
