package com.example.demo;

import java.lang.reflect.Field;

public class TestUtils {
	public static void injectObjects(Object target,String fieldName, Object toInject) {
		boolean wasPrivate = true;
		try {
			Field field = target.getClass().getDeclaredField(fieldName);
			if(!field.isAccessible()) {
				field.setAccessible(true);
				wasPrivate = true;
			}
			field.set(target, toInject);
			if(wasPrivate) {
				field.setAccessible(false);
			}
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
	}

}
