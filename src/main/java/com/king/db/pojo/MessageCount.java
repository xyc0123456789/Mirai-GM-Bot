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
public class MessageCount {
    /**
     * 类型0 人 1 群
     */
    private Integer contactType;

    /**
     * qq群号
     */
    private Long contactId;

    /**
     * 群员qq_id
     */
    private Long subContactId;

    /**
     * 日期
     */
    private String date;

    /**
     * 计数
     */
    private Long count;

    /**
     * 备注
     */
    private String remark;

    private String permission;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ctime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date utime;

    private Boolean enable;
}