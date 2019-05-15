package com.leone.util.generator.mybatis;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * @author leone
 * @since 2019-05-13
 **/
public class GeneratorCode {

    public static void main(String[] args) {
        System.out.println(GeneratorCode.class.getResource("/mybatis/generatorConfig.xml").toString().substring(6));
        try {
            generator();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @throws Exception
     */
    private static void generator() throws Exception {
        List<String> warnings = new ArrayList<>();
        boolean override = true;
        // 指定配置文件
        File configFile = new File(GeneratorCode.class.getResource("/mybatis/generatorConfig.xml").toString().substring(6));
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(override);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }

}