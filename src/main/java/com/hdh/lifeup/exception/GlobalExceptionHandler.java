package com.hdh.lifeup.exception;

import com.hdh.lifeup.enums.CodeMsgEnum;
import com.hdh.lifeup.util.Result;
import com.hdh.lifeup.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * GlobalExceptionHandler class<br/>
 *
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
        return Result.error((e.getCodeMsgEnum()));
    }

    /**
     * 出现参数校验异常
     * @param e 异常
     * @return 通用result
     */
    @ExceptionHandler(value = BindException.class)
    public ResultVO<?> handleBindException(BindException e) {
        List<ObjectError> errorList = e.getAllErrors();
        ObjectError objectError = errorList.get(0);
        String defaultMessage = objectError.getDefaultMessage();
        return Result.error(CodeMsgEnum.PARAMETER_ERROR)
                     .appendArgs(defaultMessage);
    }

    /**
     * 系统内部异常，打印异常栈
     * @param e 异常
     * @return 通用result
     */
    @ExceptionHandler(value = Exception.class)
    public ResultVO<String> handlerInnerException(Exception e) {
        log.error("【系统内部异常】stacktrace = [{}]", e.toString());
        return Result.error(CodeMsgEnum.SERVER_ERROR);
    }
}
