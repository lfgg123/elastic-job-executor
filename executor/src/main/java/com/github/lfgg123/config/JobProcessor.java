package com.github.lfgg123.config;

import com.github.lfgg123.constant.Constant;
import com.github.lfgg123.model.SchedulerJob;
import com.github.lfgg123.service.JobConfigOperation;
import com.github.lfgg123.template.simplejob.SimpleJobTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * 描述：
 * 作业处理
 *
 * @author chentianlong
 * @date 2018/06/27 16:01
 */
@Component
public class JobProcessor {

    @Value("${env}")
    private String env;
    @Resource
    private JobConfigOperation jobConfigOperation;
    @Resource
    private SimpleJobTemplate simpleJobTemplate;

    @PostConstruct
    public void init() {
        File dir = new File(Constant.JAR_PATH);
        if (dir.exists()) {
            if (dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files != null && files.length > 0) {
                    for (File file : files) {
                        file.delete();
                    }
                }
            }
        } else {
            dir.mkdir();
        }
        List<SchedulerJob> configs = jobConfigOperation.getAllJobs();
        configs.forEach(config -> {
            boolean disabled = false;
            if (Constant.PRE.equals(env)) {
                disabled = true;
            }
            simpleJobTemplate.startup(config, disabled);
        });
    }
}
