package com.hdh.lifeup.exception;

import com.hdh.lifeup.model.enums.CodeMsgEnum;
import com.hdh.lifeup.util.Result;
import com.hdh.lifeup.model.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * GlobalExceptionHandler class<br/>
 * 全局异常处理
 * @author hdonghong
 * @since 2018/08/12
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 出现系统预知的异常
     * @param e 异常
     * @return 通用result
     */
    @ExceptionHandler(value = GlobalException.class)
    public ResultVO<?> handlerGlobalException(GlobalException e) {
        log.error("【系统预知异常】e = [{}]", e.toString());
        return Result.error((e.getCodeMsgEnum()));
    }

    /**
     * 出现参数校验异常
     * @param e 异常
     * @return 通用result
     */
    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    public ResultVO<?> handleBindException(Exception e) {
        BindingResult bindingResult = BindException.class.isInstance(e) ?
                ((BindException) e).getBindingResult() : ((MethodArgumentNotValidException) e).getBindingResult();
        Object[] messageArr = bindingResult.getFieldErrors()
                                           .stream()
                                           .map(error -> error.getField() + error.getDefaultMessage())
                                           .toArray();
        return Result.error(CodeMsgEnum.PARAMETER_ERROR)
                     .appendArgs(messageArr);
    }


    /**
     * 系统内部异常，专门处理空指针
     * @param e 异常
     * @return 通用result
     */
    @ExceptionHandler(value = NullPointerException.class)
    public ResultVO<String> handlerNullPointerException(NullPointerException e) {
        log.error("【系统内部异常】stacktrace = [{}]", e.toString());
        e.printStackTrace();
        return Result.error(CodeMsgEnum.PARAMETER_NULL);
    }

    /**
     * 系统内部异常，打印异常栈
     * @param e 异常
     * @return 通用result
     */
    @ExceptionHandler(value = Exception.class)
    public ResultVO<String> handlerInnerException(Exception e) {
        log.error("【系统内部异常】stacktrace = [{}]", e.toString());
        ResultVO<String> errorResult = Result.error(CodeMsgEnum.SERVER_ERROR);
        if (HttpRequestMethodNotSupportedException.class.isInstance(e)) {
            errorResult.appendArgs(HttpRequestMethodNotSupportedException.class.getName());
        }
        return errorResult;
    }
}
