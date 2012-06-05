package model;

public class ObjectWrapper {

	private Class<?> classType;
	private Object object;
	
	public ObjectWrapper(Object object) {
		
		if(object != null)
			classType = object.getClass();
		this.object = object;
	}
	
	public Class<?> getClassType() {
		return classType;
	}
	
	@Override
	public String toString() {
		return String.valueOf(object);
	}
	
	public Object getObject() {
		return object;
	}
	
}
