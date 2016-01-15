package com.telesens.afanasiev.model.helper;

import java.lang.reflect.Field;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by oleg on 12/17/15.
 */
public class DaoUtils {
    /**
     *
     * @param entity any object
     * @param fieldName String with name of private field
     * @param value value with will be set to field
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * This method collects all private fields from entity  class, his ancestors till Object class,
     * takes field with defined name and sets defined value.
     */
    public static void setPrivateField(Object entity, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        List<Field> fields;
        Class clazz = entity.getClass();
        while (!clazz.equals(Object.class)) {
            fields = asList(clazz.getDeclaredFields());
            for (Field f : fields) {
                if (f.getName().equals(fieldName)) {
                    f.setAccessible(true);
                    f.set(entity, value);
                }
            }
            clazz = clazz.getSuperclass();
        }
    }
}
