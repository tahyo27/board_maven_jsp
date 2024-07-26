package com.duck.myboard.validation;

import com.duck.myboard.exception.BlankException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class BlankValidation {

    public void isValid(Object obj, String... fieldNames) {
        if(obj == null) {
            throw new BlankException();
        }
        try {
            for(String fieldName : fieldNames) {
                Field field = obj.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(obj);
                if(value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
                    throw new BlankException();
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("해당 필드에 접근 실패", e);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("해당 필드를 찾을 수 없습니다", e);
        }

    }
}
