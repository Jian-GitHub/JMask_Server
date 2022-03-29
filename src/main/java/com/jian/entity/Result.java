package com.jian.entity;

import lombok.Data;

@Data
public class Result<T> {
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 提示信息
     */
    private String msg;
    /**
     *
     */
    private T data;

//    public T getData(){
//        return data;
//    }
    public Result setData(T data){
        this.data=data;
        return this;
    }

    public Result(Integer code, String msg){
        this.code=code;
        this.msg=msg;
    }

    public static Result getSuccess() {
        return new Result(20000,"成功");
    }

    public static Result getFail() {
        return new Result(50008,"失败");
    }

}
