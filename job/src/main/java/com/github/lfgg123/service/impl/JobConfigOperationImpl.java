package com.github.lfgg123.service.impl;

import com.github.lfgg123.dao.JobConfigDao;
import com.github.lfgg123.model.SchedulerJob;
import com.github.lfgg123.service.JobConfigOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：
 * job操作实现类
 *
 * @author chentianlong
 * @date 2018/06/26 11:32
 */
@Service
public class JobConfigOperationImpl implements JobConfigOperation {

    @Autowired
    private JobConfigDao jobConfigDao;


    @Override
    public List<SchedulerJob> getAllJobs() {
        return jobConfigDao.getAllJobs();
    }
}
