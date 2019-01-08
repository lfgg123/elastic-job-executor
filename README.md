 elastic-job-executor
=================== 
**elastic-job二次开发**
-------------------
### 改动点：

1.将elastic-job原有的定时任务通过配置文件配置的方式启动，改为从数据库读取的方式启动，
表结构见sql文件夹下的demo.sql。

2.当一个任务置为失效之后，下次重新启动时，还是失效状态。

3.通过参数统一控制所有的job是否在应用启动的时候同时失效。

4.对elastic-job的源码中的JobScheduler进行修改，新增了一个构造函数。详情见documents/JobScheduler.java。

**注：** pom文件中的elastic-job的版本号是修改过JobScheduler重新deploy的版本号，与中央仓库中的版本号无关。


### 逻辑架构：
![Image text](https://raw.githubusercontent.com/lfgg123/elastic-job-demo/master/documents/%E9%80%BB%E8%BE%91%E7%BB%93%E6%9E%84.png)
