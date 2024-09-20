package com.WebClientFactory.WebClient_Factory.utils;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class TestUtil {
    public static void setPrivateField(Object object, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }
}
