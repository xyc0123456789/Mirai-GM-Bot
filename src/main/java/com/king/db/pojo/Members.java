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
public class Members {
    /**
     * q群号
     */
    private Long groupId;

    /**
     * qq号
     */
    private Long qqId;

    /**
     * qq等级
     */
    private Integer qqLevel;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别
     */
    private String sex;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 群名片
     */
    private String nameCard;

    /**
     * 群头衔
     */
    private String specialTitle;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 入群时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date joinDate;

    /**
     * 最后发言时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastSpeakDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ctime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date utime;

    private Boolean enable;

    /**
     * 当前个性签名
     */
    private String sign;

    private String extData;
}