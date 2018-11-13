package com.github.lfgg123.compiler;

import com.dangdang.ddframe.job.exception.JobConfigurationException;
import com.github.lfgg123.constant.Constant;
import com.github.lfgg123.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * In-memory compile Java source code as String.
 *
 * @author chentianlong
 */
@Component
public class JavaStringCompiler {

    @Value("${env}")
    private String env;

    JavaCompiler compiler;
    StandardJavaFileManager stdManager;
    private static String paths;

    public JavaStringCompiler() {
        this.compiler = ToolProvider.getSystemJavaCompiler();
        this.stdManager = compiler.getStandardFileManager(null, null, null);
    }

    /**
     * Compile a Java source file in memory.
     *
     * @param fileName Java file name, e.g. "Test.java"
     * @param source   The source code as String.
     * @return The compiled results as Map that contains class name as key,
     * class binary as value.
     * @throws IOException If compile error.
     */
    public Map<String, byte[]> compile(String fileName, String source) throws IOException, ClassNotFoundException {
        try (MemoryJavaFileManager manager = new MemoryJavaFileManager(stdManager)) {
            JavaFileObject javaFileObject = manager.makeStringSource(fileName, source);
            List<String> options = new ArrayList<>();
            if(!Constant.LOCAL.equals(env)){
                if (StringUtils.isEmpty(paths)) {
                    paths = getJarPath();
                }
                options = Arrays.asList("-classpath", paths);
            }
            CompilationTask task = compiler.getTask(null, manager, null, options, null, Arrays.asList(javaFileObject));
            Boolean result = task.call();
            if (result == null || !result) {
                throw new JobConfigurationException("Compilation failed.");
            }
            return manager.getClassBytes();
        }
    }

    private String getJarPath() {
        StringBuilder jarPath = new StringBuilder();
        String path = Constant.JAR_LIB_PATH;
        List<File> files = FileUtil.getJarResourceFile(path);
        if (!CollectionUtils.isEmpty(files)) {
            for (int i = 0; i < files.size(); i++) {
                if (i != (files.size() - 1)) {
                    jarPath.append(files.get(i).getAbsolutePath() + ":");
                } else {
                    jarPath.append(files.get(i).getAbsolutePath());
                }
            }

        }
        return jarPath.toString();
    }

    /**
     * Load class from compiled classes.
     *
     * @param name       Full class name.
     * @param classBytes Compiled results as a Map.
     * @return The Class instance.
     * @throws ClassNotFoundException If class not found.
     * @throws IOException            If load error.
     */
    public Class<?> loadClass(String name, Map<String, byte[]> classBytes) throws ClassNotFoundException, IOException {
        try (MemoryClassLoader classLoader = new MemoryClassLoader(classBytes)) {
            return classLoader.loadClass(name);
        }
    }
}
