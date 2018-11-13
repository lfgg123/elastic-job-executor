package com.github.lfgg123.service.impl;

import com.github.lfgg123.service.JobService;
import org.springframework.stereotype.Service;

/**
 * @author chentl
 * @version JobServiceImpl, v0.1 2018/11/13 10:32
 */
@Service
public class JobServiceImpl implements JobService {
    @Override
    public void processJob(String param) {
        System.out.println("执行定时任务:" + param);
    }
}
