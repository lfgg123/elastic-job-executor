package com.github.lfgg123.jobs;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 描述：
 * demo
 *
 * @author chentianlong
 * @date 2018/06/13 16:23
 */
public class ExampleSimpleJob implements SimpleJob {

    private final Logger logger = LoggerFactory.getLogger(ExampleSimpleJob.class);

    @Override
    public void execute(ShardingContext shardingContext) {
        logger.info("准备执行定时任务,thread ID:{},任务总片数{},当前分片项:{}", Thread.currentThread().getId(), shardingContext.getShardingTotalCount(), shardingContext.getShardingItem());

    }
}
