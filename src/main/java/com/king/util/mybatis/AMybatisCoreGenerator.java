package com.king.util.mybatis;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * java代码生成器(会生成example的entity)<br>
 * mybatis逆向工程
 *
 * @author jingshiyu
 * @date 2019/7/17 17:24
 * @desc java代码生成器(会生成example的entity)<br>
 */
public class AMybatisCoreGenerator {
    public static void main(String[] args) {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        //如果这里出现空指针，直接写绝对路径即可。
        String genCfg = "/mybatis-generator.xml";
        String packageName="com.king.db.dao";
        String workPath = "src/main/java";
        File configFile = new File(AMybatisCoreGenerator.class.getResource(genCfg).getFile());
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = null;
        try {
            config = cp.parseConfiguration(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = null;
        try {
            myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        try {
            myBatisGenerator.generate(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        GenerateSubDao.generateSubDao(packageName,workPath);
    }

}
