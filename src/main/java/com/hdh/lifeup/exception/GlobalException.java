package com.hdh.lifeup.exception;

import com.hdh.lifeup.model.enums.CodeMsgEnum;
import lombok.Getter;

/**
 * GlobalException class<br/>
 * 全局异常
 * @author hdonghong
 * @since 2018/08/13
 */
@Getter
public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 6174274224875271127L;

    private CodeMsgEnum codeMsgEnum;

    public GlobalException(CodeMsgEnum codeMsgEnum) {
        super(codeMsgEnum.toString());
        this.codeMsgEnum = codeMsgEnum;
    }
}
