package com.king;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import xyz.cssxsh.mirai.tool.FixProtocolVersion;
import xyz.cssxsh.mirai.tool.KFCFactory;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class SpringApplicationMain {
    public static void main(String[] args) {
        FixProtocolVersion.update();
        KFCFactory.install();
        log.info("FixProtocolVersion: {}",FixProtocolVersion.info());
        SpringApplication.run(SpringApplicationMain.class,args);
    }
}
