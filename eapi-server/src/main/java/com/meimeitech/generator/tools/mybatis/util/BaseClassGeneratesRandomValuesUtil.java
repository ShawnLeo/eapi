package com.meimeitech.generator.tools.mybatis.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author paul
 * @description
 * @date 2019/4/17
 */
public class BaseClassGeneratesRandomValuesUtil {
    public static final Map<Class, Object> values = new HashMap<>();

    static {

        values.put(java.lang.Float.class, Float.valueOf("1"));
        values.put(float[].class, new float[]{1});
        values.put(java.lang.Long.class, Long.valueOf("2"));
        values.put(java.lang.Double.class, Double.valueOf("3"));
        values.put(java.lang.Short.class, Short.valueOf("4"));
        values.put(java.lang.Byte.class, Byte.valueOf("5"));
        values.put(java.lang.Integer.class, Integer.valueOf("6"));
        values.put(BigDecimal.class, BigDecimal.valueOf(7));
        values.put(java.lang.Boolean.class, Boolean.FALSE);
        values.put(java.lang.String.class, "hello world");
        values.put(java.lang.Object.class, new Object());
        values.put(java.util.Date.class, new Date());
    }



}
