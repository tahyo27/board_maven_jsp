package com.duck.myboard.validation;

import com.duck.myboard.exception.BlankException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class BlankValidation {

    public boolean isValid(Object obj) {
        if(obj == null) {
            throw new BlankException();
        }
        try {
            for(Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(obj);
                if(value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
                    throw  new BlankException();
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("필드에 접근 실패", e);
        }

        return true;
    }
}
