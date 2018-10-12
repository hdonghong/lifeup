package com.hdh.lifeup.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class JsonUtilTest {

    @Test
    public void toJson() throws Exception {
    }

    @Test
    public void jsonToObject() throws Exception {
        Long aLong = JsonUtil.jsonToObject("1", Long.TYPE);
        System.out.println(aLong);
    }

    @Test
    public void jsonToList() throws Exception {
    }

}