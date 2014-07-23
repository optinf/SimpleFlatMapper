package org.sfm.reflect.primitive;

import java.lang.reflect.Field;

public final class ShortFieldSetter<T> implements ShortSetter<T> {

	private final Field field;
	
	public ShortFieldSetter(Field field) {
		this.field = field;
	}

	@Override
	public void setShort(T target, short value) throws IllegalArgumentException, IllegalAccessException {
		field.setShort(target, value);
	}

}
