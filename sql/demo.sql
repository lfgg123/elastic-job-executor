CREATE TABLE `scheduler_job` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `job_name` varchar(64) NOT NULL COMMENT '作业名称',
  `dubbo_class` varchar(256) NOT NULL COMMENT 'dubbo接口class全路径',
  `method_name` varchar(256) NOT NULL COMMENT '方法名',
  `sharding_total_count` smallint(5) DEFAULT '2' COMMENT '分片总数',
  `cron` varchar(64) NOT NULL COMMENT '作业触发时间表达式',
  `sharding_item_parameters` varchar(256) DEFAULT NULL COMMENT '分片序列号和参数用等号分隔，多个键值对用逗号分隔',
  `job_parameter` varchar(256) DEFAULT NULL COMMENT '作业自定义参数',
  `monitor_port` smallint(5) DEFAULT '-1' COMMENT '监控端口',
  `monitor_execution` tinyint(1) DEFAULT '0' COMMENT '监控作业运行时状态',
  `failover` tinyint(1) DEFAULT '1' COMMENT '是否开启失效转移',
  `description` varchar(256) DEFAULT NULL COMMENT '任务描述',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_idx_job_name` (`job_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='定时任务配置表';