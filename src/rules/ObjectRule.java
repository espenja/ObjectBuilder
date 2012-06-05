package rules;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;

import interfaces.CustomObjectFormatter;
import interfaces.ICanOutputFormattedData;

public class ObjectRule implements ICanOutputFormattedData {
	
	private HashMap<String, MethodRule> methodRules;
	private HashMap<String, AnnotationRule> annotationRules;
	private CustomObjectFormatter customObjectFormatter = null;
	private boolean ignoreObject;
	private boolean ignoreMethods;
	private boolean ignoreAnnotations;
	protected boolean valid;
	
	private Class<?> primaryClass;
	private HashSet<Class<?>> classes;
	private String classPath;
	
	public ObjectRule(Class<?> primaryClass, Class<?>... formatSupportClasses) {
		this.primaryClass = primaryClass;
		this.classes = new HashSet<>();
		
		methodRules = new HashMap<>();
		annotationRules = new HashMap<>();
		
		for(Class<?> cls : formatSupportClasses) {
			this.classes.add(cls);
		}
	}
	
	public HashMap<String, MethodRule> getMethodRules() {
		return methodRules;
	}

	public void setMethodRules(HashMap<String, MethodRule> methodRules) {
		this.methodRules = methodRules;
	}
	
	public HashMap<String, AnnotationRule> getAnnotationRules() {
		return annotationRules;
	}

	public void setAnnotationRules(HashMap<String, AnnotationRule> annotationRules) {
		this.annotationRules = annotationRules;
	}

	public boolean getIgnoreMethods() {
		return ignoreMethods;
	}

	public void setIgnoreMethods(boolean ignoreMethods) {
		this.ignoreMethods = ignoreMethods;
	}

	public boolean getIgnoreAnnotations() {
		return ignoreAnnotations;
	}

	public void setIgnoreAnnotations(boolean ignoreAnnotations) {
		this.ignoreAnnotations = ignoreAnnotations;
	}
	
	protected void setValid(boolean valid) {
		this.valid = valid;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public void setIgnoredObject(boolean ignoredObject) {
		this.ignoreObject = ignoredObject;
	}
	
	public boolean isIgnoredObject() {
		return ignoreObject;
	}
	
	public Class<?> getPrimaryClass() {
		return primaryClass;
	}
	
	public HashSet<Class<?>> getClasses() {
		return classes;
	}

	public String getClassPath() {
		return classPath;
	}
	
	public ObjectRule methodRules(HashMap<String, MethodRule> methodRules) {
		this.methodRules = methodRules;
		return this;
	}
	
	public ObjectRule annotationRules(HashMap<String, AnnotationRule> annotationRules) {
		this.annotationRules = annotationRules;
		return this;
	}

	public ObjectRule ignoreMethods() {
		ignoreMethods = true;
		return this;
	}
	
	public ObjectRule ignoreAnnotations() {
		ignoreAnnotations = true;
		return this;
	}
	
	public ObjectRule ignoredObject(boolean ignoredObject) {
		this.ignoreObject = ignoredObject;
		return this;
	}
	
	public MethodRule createMethodRule(Method method) {
		MethodRule methodRule = new MethodRule(this, method);
		methodRules.put(methodRule.getMethodName(), methodRule);
		return methodRule;
	}
	
	public MethodRule createMethodRule(String methodName) {
		MethodRule methodRule = new MethodRule(this, methodName);
		methodRules.put(methodName, methodRule);
		return methodRule;
	}
	
	public MethodRule createMethodRule(String methodName, @SuppressWarnings("rawtypes") Class... parameterValues) {
		MethodRule methodRule = new MethodRule(this, methodName, parameterValues);
		methodRules.put(methodName, methodRule);
		return methodRule;
	}
	
	public String format(Object object) {
		if(classes.contains(object.getClass())) {
			if(customObjectFormatter != null)
				return customObjectFormatter.format(object);
			return object.toString();
		}
		throw new RuntimeException("Cannot format object of class not represented by the object rule: " + object.getClass());
	}

	public void addCustomObjectFormatter(CustomObjectFormatter customObjectFormatter) {
		this.customObjectFormatter = customObjectFormatter;
	}
	
	public CustomObjectFormatter getCustomObjectFormatter() {
		return customObjectFormatter;
	}
}
