package com.hdh.lifeup.exception;

import com.hdh.lifeup.model.enums.CodeMsgEnum;
import lombok.Getter;

/**
 * SingleTaskException class<br/>
 * 单次任务异常
 * @author hdonghong
 * @since 2019/11/24
 */
@Getter
public class SingleTaskException extends RuntimeException {

    private static final long serialVersionUID = 6174274224875271127L;

    private CodeMsgEnum codeMsgEnum;

    public SingleTaskException(CodeMsgEnum codeMsgEnum) {
        super(codeMsgEnum.toString());
        this.codeMsgEnum = codeMsgEnum;
    }
}
