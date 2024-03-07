package com.king.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @description: 上一次检测信息
 * @author: xyc0123456789
 * @create: 2023/5/31 12:39
 **/
@Data
@AllArgsConstructor
public class LastDetectResult {
    public LastDetectResult(List<Long> ids) {
        this.ids = ids;
    }

    private List<Long> ids;

    private Integer type=0;//0,正常检测头衔，1，头衔名称检测,2 潜水，3 低等级

}
