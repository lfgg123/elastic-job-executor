package com.github.lfgg123.exception;

/**
 * 描述：
 * job操作异常类
 *
 * @author chentianlong
 * @date 2018/06/27 13:21
 */
public class JobOperationException extends RuntimeException{

    private static final long serialVersionUID = -7641391706251122284L;

    public JobOperationException(final String errorMessage, final Object... args) {
        super(String.format(errorMessage, args));
    }

    public JobOperationException(final String message){
        super(message);
    }
}
