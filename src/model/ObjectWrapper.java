package model;

import interfaces.Formatter;
import control.Reader;

public class ObjectWrapper {

	private Class<?> classType;
	private Object object;
	private Reader reader;
	
	public ObjectWrapper(Object object, Reader reader) {
		
		if(object != null)
			classType = object.getClass();
		this.object = object;
		this.reader = reader;
	}
	
	public Class<?> getClassType() {
		return classType;
	}
	
	@Override
	public String toString() {
		return String.valueOf(object);
	}
	
	public String format() {
		if(object != null) {
			Formatter formatter = reader.getFormatter();
			if(formatter.getObjectRules().containsKey(classType)) {
				return formatter.getObjectRules().get(classType).format(object);
			}
		}
		return toString();
	}
	
	public Object getObject() {
		return object;
	}
}
