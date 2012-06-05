package rules;

import java.lang.reflect.Method;
import java.util.List;

import interfaces.CustomObjectFormatter;
import interfaces.ICanOutputFormattedData;

public class MethodRule implements ICanOutputFormattedData {
	
	private ObjectRule objectRule;
	private Method method;
	private String methodName;
	private List<AnnotationRule> annotationRules;
	private boolean ignored;
	@SuppressWarnings({ "rawtypes", "unused" })
	private Class[] parameterValues;
	
	public MethodRule(ObjectRule objectRule, Method method) {
		this.objectRule = objectRule;
		this.method = method;
		methodName = method.getName();
		try {
			objectRule.getPrimaryClass().getMethod(methodName, new Class[] { });
			parameterValues = new Class[] { };
			method.setAccessible(true);
		} catch (NoSuchMethodException | SecurityException e) {
			objectRule.setValid(false);
		}
	}
	
	public MethodRule(ObjectRule objectRule, String methodName) {
		this.objectRule = objectRule;
		this.methodName = methodName;
		try {
			method = objectRule.getPrimaryClass().getMethod(methodName, new Class[] { });
			parameterValues = new Class[] { };
			method.setAccessible(true);
		} catch (NoSuchMethodException | SecurityException e) {
			objectRule.setValid(false);
		}
	}
	
	public MethodRule(ObjectRule objectRule, String methodName, @SuppressWarnings("rawtypes") Class... parameterValues) {
		this.objectRule = objectRule;
		this.methodName = methodName;
		try {
			method = objectRule.getPrimaryClass().getMethod(methodName, parameterValues);
			this.parameterValues = parameterValues;
			method.setAccessible(true);
		} catch (NoSuchMethodException | SecurityException e) {
			objectRule.setValid(false);
		}
	}
	
	public ObjectRule getObjectRule() {
		return objectRule;
	}

	public Method getMethod() {
		return method;
	}
	
	public String getMethodName() {
		return methodName;
	}

	public List<AnnotationRule> getAnnotationRules() {
		return annotationRules;
	}

	public void setAnnotationRules(List<AnnotationRule> annotationRules) {
		this.annotationRules = annotationRules;
	}

	public boolean isIgnored() {
		return ignored;
	}

	public void setIgnored(boolean ignored) {
		this.ignored = ignored;
	}

	public MethodRule ignored(boolean ignored) {
		this.ignored = ignored;
		return this;
	}
	
	public String format(Object object) {
		return object.toString();
	}

	@Override
	public void addCustomObjectFormatter(
			CustomObjectFormatter customObjectFormatter) {
		// TODO Auto-generated method stub
		
	}
}
