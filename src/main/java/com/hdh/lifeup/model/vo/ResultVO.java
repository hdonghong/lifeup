package com.hdh.lifeup.model.vo;

import com.hdh.lifeup.model.enums.CodeMsgEnum;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Arrays;

/**
 * ResultVO class<br/>
 * http结果类
 * @author hdonghong
 * @since 2018/08/10
 */
@Getter
@ToString
public class ResultVO<T> implements Serializable {

    private static final long serialVersionUID = -8730161854744420763L;

    /** 状态码 */
    private Integer code;

    /** 提示信息 */
    private String msg;

    /**
     * 国际化信息
     */
    private String globalMsg;

    /** 具体内容 */
    private T data;

    private ResultVO(CodeMsgEnum codeMsgEnum, T data) {
        this.code = codeMsgEnum.getCode();
        this.msg = codeMsgEnum.getMsg();
        this.globalMsg = codeMsgEnum.getGlobalMsg();
        this.data = data;
    }

    public ResultVO(T data) {
        // 不传状态码，默认成功
        this(CodeMsgEnum.SUCCESS, data);
    }

    public ResultVO(CodeMsgEnum codeMsgEnum) {
        this(codeMsgEnum, null);
    }

    /**
     * 追加参数到输出结果的message中
     * @param args 追加的参数
     * @return ResultVO
     */
    public ResultVO<T> appendArgs(T ... args) {
        if (args != null && args.length > 0) {
            this.msg += Arrays.toString(args);
        }
        return this;
    }
}
