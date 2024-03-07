package com.king.util;

import java.io.Serializable;
import java.util.List;

public class ExcelData implements Serializable {

    private static final long serialVersionUID = 6133772627258154184L;
    /**
     * 表头
     */
    private List<String> titles;

    /**
     * 数据
     */
    private List<List<Object>> rows;

    public ExcelData() {
    }

    public ExcelData(List<String> titles, List<List<Object>> rows, String name) {
        this.titles = titles;
        this.rows = rows;
        this.name = name;
    }

    /**
     * 页签名称
     */
    private String name;

    //合并内容
    private String  mergeName;

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<List<Object>> getRows() {
        return rows;
    }

    public void setRows(List<List<Object>> rows) {
        this.rows = rows;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMergeName() {
        return mergeName;
    }

    public void setMergeName(String mergeName) {
        this.mergeName = mergeName;
    }
}
