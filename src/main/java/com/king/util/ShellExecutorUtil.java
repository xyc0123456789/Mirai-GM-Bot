package com.king.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ShellExecutorUtil {


    public static String exe(String command) {
        return exe(command, 0L);
    }
    public static String exe(String command, long timeout) {
        return exe(command, timeout, true);
    }

    public static String exe(String command, long timeout, boolean showLog) {
        StringBuilder output = new StringBuilder();
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
        pb.directory(new File("/home/appuser")); // 设置工作目录
        if (showLog) {
            log.info("exe command:" + command);
        }
        try {
            pb.redirectErrorStream(true);
            Process p = pb.start();
            if (timeout==0){
                p.waitFor();
            }else {
                p.waitFor(timeout, TimeUnit.SECONDS);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        } catch (Exception e) {
            log.error("shell err", e);
            output.append("shell err:").append(Arrays.toString(e.getStackTrace()));
        }
        if (showLog) {
            log.info(output.toString());
        }
        return output.toString();
    }

    public static String exeRuntime(String command) {
        StringBuilder output = new StringBuilder();
        log.info("exe command:"+command);
        try {
            Process exec = Runtime.getRuntime().exec("bash -c "+command);
            exec.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(exec.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                output.append(line).append("\n");
            }
        } catch (Exception e) {
            log.error("shell err", e);
            output.append("shell err:").append(Arrays.toString(e.getStackTrace()));
        }
        String replaceAll = output.toString().replaceAll("\n", "\t");
        if (replaceAll.length()>500){
            replaceAll = replaceAll.substring(0, 500);
        }
        log.info(replaceAll);
        return output.toString();
    }

}
