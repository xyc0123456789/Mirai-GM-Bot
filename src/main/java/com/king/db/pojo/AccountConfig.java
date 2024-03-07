package com.king.db.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountConfig {
    private Integer id;

    private Long account;

    private String password;

    private String heartBeatStrategy;

    private String protocol;

    private String workingDir;

    /**
     * 名称即可，存在working_dir下
     */
    private String cacheDirName;

    /**
     * 名称即可，存在working_dir下
     */
    private String deviceInfoFileName;

    /**
     * 名称即可，存在working_dir下
     */
    private String sensitiveWordDirName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ctime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date utime;

    private Boolean enable;
}