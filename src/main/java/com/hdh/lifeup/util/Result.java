package com.hdh.lifeup.util;

import com.hdh.lifeup.enums.CodeMsgEnum;
import com.hdh.lifeup.vo.ResultVO;

/**
 * Result class<br/>
 *
 * @author hdonghong
 * @since 2018/08/11
 */
public class Result {

    /**
     * 处理成功的请求
     * @param data 响应数据
     * @param <T>  返回的数据泛型类型
     * @return 通用结果
     */
    public static <T> ResultVO<T> success(T data) {
        return new ResultVO<>(data);
    }

    /**
     * 成功请求
     * @param <T> 返回的数据泛型类型
     * @return 通用结果
     */
    public static <T> ResultVO<T> success() {
        return success(null);
    }

    /**
     * 处理错误的请求
     * @param codeMsgEnum 状态码
     * @return 通用结果
     */
    public static <T> ResultVO<T> error(CodeMsgEnum codeMsgEnum) {
        return new ResultVO<>(codeMsgEnum);
    }
}
