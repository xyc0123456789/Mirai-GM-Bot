package com.king.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DataResponse<D> extends Response{
    private D data;

    public DataResponse() {

    }

    public DataResponse(CODE code, String message) {
        super(code, message);
    }

    public DataResponse(CODE code, String message, D data) {
        super(code, message);
        this.data = data;
    }

    public DataResponse(Response response, D data) {
        super(response);
        this.data = data;
    }

    public DataResponse(Response response) {
        super(response);
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + getCode() +
                ", message='" + getMessage() + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
