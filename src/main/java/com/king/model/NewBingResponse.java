package com.king.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/11 22:03
 **/
@Data
public class NewBingResponse {
    private String response;
    private List<String> suggestedResponses=new ArrayList<>();
    private List<String> references=new ArrayList<>();
}
