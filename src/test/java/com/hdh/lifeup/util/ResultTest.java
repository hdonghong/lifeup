package com.hdh.lifeup.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdh.lifeup.model.enums.CodeMsgEnum;
import com.hdh.lifeup.model.vo.ResultVO;
import org.junit.Test;

import java.util.HashMap;

public class ResultTest {


    @Test
    public void success() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ResultVO<Object> result = Result.success();
        System.out.println(objectMapper.writeValueAsString(result));

    }

    @Test
    public void success1() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ResultVO<Object> result = Result.success(new HashMap<String, Object>() {
            private static final long serialVersionUID = 5593375536518920044L;
            {
                put("nick_name", "hdonghong");
                put("password", "123");
            }
        });
        System.out.println(objectMapper.writeValueAsString(result));
    }

    @Test
    public void error() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ResultVO<Object> result = Result.error(CodeMsgEnum.PARAMETER_ERROR);
        System.out.println(objectMapper.writeValueAsString(result));
    }

}