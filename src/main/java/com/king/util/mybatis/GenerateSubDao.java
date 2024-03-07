package com.king.util.mybatis;

import com.king.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;

@Slf4j
public class GenerateSubDao {


    private static final String subdaoTemplate = "package ${subpackageName};\n" +
            "\n" +
            "import ${packageName}.${className};\n" +
            "import org.springframework.stereotype.Component;\n" +
            "import org.apache.ibatis.annotations.Mapper;\n" +
            "\n" +
            "@Component\n" +
            "@Mapper\n" +
            "public interface Sub${className} extends ${className} {\n" +
            "    \n" +
            "}";

    private static final String serviceTemplate = "package ${servicepackage};\n" +
            "\n" +
            "\n" +
            "import ${subpackageName}.${subclassName};\n" +
            "import org.springframework.beans.factory.annotation.Autowired;\n" +
            "import org.springframework.stereotype.Service;\n" +
            "import lombok.extern.slf4j.Slf4j;\n" +
            "\n" +
            "@Slf4j\n" +
            "@Service\n" +
            "public class ${serviceClassName} {\n" +
            "\n" +
            "    @Autowired\n" +
            "    private ${subclassName} ${subclassValueName};\n" +
            "\n" +
            "}";

    private static final String subpackageToReplace = "\\$\\{subpackageName\\}";
    private static final String packageToReplace = "\\$\\{packageName\\}";

    private static final String classNameToReplace = "\\$\\{className\\}";

    private static final String subclassNameToReplace = "\\$\\{subclassName\\}";

    private static final String subclassValueNameToReplace = "\\$\\{subclassValueName\\}";

    private static final String servicepackageToReplace = "\\$\\{servicepackage\\}";

    private static final String serviceClassNameToReplace = "\\$\\{serviceClassName\\}";


    public static void generateSubDao(String packageName,String workdir){
        if (!packageName.endsWith(".dao")){
            log.error("packageName wrong");
        }

        String packageBaseName = packageName.substring(0, packageName.length() - 4);
        String subpackageName = packageBaseName + ".subdao";
        String servicepackageName = packageBaseName + ".service";

        String daoPath = packageName.replace('.', '/');
        String subdaoPath = subpackageName.replace('.', '/');
        String servicePath = servicepackageName.replace(".","/");

        URL resource = Thread.currentThread().getContextClassLoader().getResource("");
        int targetIndex = resource.getFile().indexOf("/target");
        String srcJava = resource.getFile().substring(0, targetIndex + 1) + workdir;
        File workDirFile = new File(srcJava);
        daoPath = workDirFile.getAbsolutePath() + "/" + daoPath;
        subdaoPath = workDirFile.getAbsolutePath() + "/" + subdaoPath;
        servicePath = workDirFile.getAbsolutePath() + "/" + servicePath;


        File subdaoDir = new File(subdaoPath);
        File daoDir = new File(daoPath);
        File serviceDir = new File(servicePath);
        if (!daoDir.exists()){
            log.error("dao not exist");
            return;
        }
        if (!subdaoDir.exists()){
            subdaoDir.mkdirs();
        }
        if (!serviceDir.exists()){
            serviceDir.mkdirs();
        }
        log.info(Arrays.toString(daoDir.listFiles()));
        for (File file: Objects.requireNonNull(daoDir.listFiles())){
            String simpleName = file.getName().substring(0,file.getName().length()-5);
            if (simpleName.endsWith("Mapper")){
                File subdao = new File(subdaoPath + "/Sub" + simpleName+".java");
                String subdaoName = "Sub" + simpleName;
                if (!subdao.exists()){
                    log.info("generate subdao:{}",simpleName);
                    String content = subdaoTemplate.replaceAll(classNameToReplace, simpleName).replaceAll(packageToReplace, packageName)
                            .replaceAll(subpackageToReplace, subpackageName);
                    outPutToFile(content, subdao);
                }

                String serviceClassName = simpleName.replace("Mapper","Service");
                File service = new File(servicePath + "/"+serviceClassName + ".java");
                String subclassValueName = (char)('a' + subdaoName.charAt(0)-'A') + subdaoName.substring(1);
                if (!service.exists()){
                    log.info("generate service:{}", serviceClassName);
                    String content = serviceTemplate.replaceAll(servicepackageToReplace,servicepackageName)
                            .replaceAll(subpackageToReplace,subpackageName).replaceAll(subclassNameToReplace,subdaoName)
                            .replaceAll(serviceClassNameToReplace,serviceClassName).replaceAll(subclassValueNameToReplace, subclassValueName);
                    outPutToFile(content, service);
                }

            }
        }
    }


    private static void outPutToFile(String content,File target){
        BufferedOutputStream outputStream = null;
        BufferedInputStream inputStream = null;
        byte[] buffer = new byte[1024];
        try {
            outputStream = new BufferedOutputStream(Files.newOutputStream(target.toPath()));
            inputStream = new BufferedInputStream(new ByteArrayInputStream(content.getBytes()));
            int len;
            while ((len = inputStream.read(buffer))!=-1){
                outputStream.write(buffer,0,len);
            }
        }catch (Exception e){
            log.error("ioException:",e);
        }finally {
            try {
                if (outputStream!=null){
                    outputStream.flush();
                    outputStream.close();
                }
            }catch (Exception e){
                log.error("",e);
            }
            try {
                if (inputStream!=null){
                    inputStream.close();
                }
            }catch (Exception e){
                log.error("",e);
            }
        }
    }



    public static void main(String[] args) {
        generateSubDao("com.king.dao","D:\\CodeAndIDE\\IDEAWorkplace\\my-qq-bot\\src\\main\\java");
    }

//    public static List<Class<?>> getClasssFromPackage(String packageName) {
//        List<Class<?>> clazzs = new ArrayList<>();
//        // 是否循环搜索子包
//        boolean recursive = true;
//        // 包名对应的路径名称
//        String packageDirName = packageName.replace('.', '/');
//        Enumeration<URL> dirs;
//
//        try {
//            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
//            while (dirs.hasMoreElements()) {
//
//                URL url = dirs.nextElement();
//                String protocol = url.getProtocol();
//
//                if ("file".equals(protocol)) {
//                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
//                    findClassInPackageByFile(packageName, filePath, recursive, clazzs);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return clazzs;
//    }
//
//    /**
//     * 在package对应的路径下找到所有的class
//     */
//    public static void findClassInPackageByFile(String packageName, String filePath, final boolean recursive,
//                                                List<Class<?>> clazzs) {
//        File dir = new File(filePath);
//        if (!dir.exists() || !dir.isDirectory()) {
//            return;
//        }
//        // 在给定的目录下找到所有的文件，并且进行条件过滤
//        File[] dirFiles = dir.listFiles(new FileFilter() {
//
//            public boolean accept(File file) {
//                boolean acceptDir = recursive && file.isDirectory();// 接受dir目录
//                boolean acceptClass = file.getName().endsWith("class");// 接受class文件
//                return acceptDir || acceptClass;
//            }
//        });
//
//        for (File file : dirFiles) {
//            if (file.isDirectory()) {
//                findClassInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, clazzs);
//            } else {
//                String className = file.getName().substring(0, file.getName().length() - 6);
//                try {
//                    clazzs.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

}
