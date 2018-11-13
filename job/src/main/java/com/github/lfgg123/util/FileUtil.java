package com.github.lfgg123.util;

import com.github.lfgg123.constant.Constant;
import com.github.lfgg123.exception.JobOperationException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 *
 * @author chentianlong
 * @date 2018/06/29 15:32
 */
public class FileUtil {

    private final static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static List<File> getJarResourceFile(String path) {
        //获取容器资源解析器
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        List<File> files = new ArrayList<>();
        try {
            //获取所有匹配的文件
            Resource[] resources = resolver.getResources(path);
            if (resources.length > 0) {
                //获得文件流，因为在jar文件中，不能直接通过文件资源路径拿到文件，但是可以在jar包中拿到文件流
                for (Resource resource : resources) {
                    InputStream stream = resource.getInputStream();
                    String targetFilePath = Constant.JAR_PATH + File.separator + resource.getFilename();
                    File ttfFile = new File(targetFilePath);
                    FileUtils.copyInputStreamToFile(stream, ttfFile);
                    files.add(ttfFile);
                }
                return files;
            }
        } catch (Exception e) {
            logger.error("读取文件流失败，写入本地库失败！ " + e);
        }
        throw new JobOperationException("未找到文件");
    }
}
