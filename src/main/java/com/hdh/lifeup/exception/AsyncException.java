package com.hdh.lifeup.exception;

import lombok.Data;

/**
 * AsyncException class<br/>
 *
 * 异步异常
 * @author hdonghong
 * @since 2019/06/06
 */
@Data
public class AsyncException extends Exception {

    private static final long serialVersionUID = 6351594248402364306L;
    private int code;
    private String msg;

}
