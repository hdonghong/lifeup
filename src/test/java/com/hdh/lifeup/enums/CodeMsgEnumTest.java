package com.hdh.lifeup.enums;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdh.lifeup.util.Result;
import org.junit.Test;

import static org.junit.Assert.*;

public class CodeMsgEnumTest {


    @Test
    public void fillArgs() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(
                Result.error(CodeMsgEnum.PARAMETER_ERROR).appendArgs("sfas", "错了", "12", "df错了")));
    }

}