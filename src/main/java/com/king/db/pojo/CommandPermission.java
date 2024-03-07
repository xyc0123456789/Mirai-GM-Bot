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
public class CommandPermission {
    /**
     * 类型0 人 1 群
     */
    private Integer contactType;

    /**
     * qq或者q群
     */
    private Long contactId;

    /**
     * q群个人配置
     */
    private Long subContactId;

    /**
     * 命令名称
     */
    private String commandName;

    /**
     * 命令描述
     */
    private String commandDesc;

    private String remark1;

    private String remark2;

    private String remark3;

    private String extData;

    /**
     * 是否开启
     */
    private Boolean isOpen;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ctime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date utime;

    /**
     * 是否可用
     */
    private Boolean enable;
}