package com.github.lfgg123.dao;

import com.github.lfgg123.model.SchedulerJob;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：
 * 任务配置dao类
 *
 * @author chentianlong
 * @date 2018/06/25 14:57
 */
@Repository
public interface JobConfigDao {

    List<SchedulerJob> getAllJobs();
}
