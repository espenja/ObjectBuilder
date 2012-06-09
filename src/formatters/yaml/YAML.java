package formatters.yaml;

import interfaces.Formatter;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import rules.ObjectRule;

import model.ObjectWrapper;
import model.Position;
import control.Reader;

public class YAML implements Formatter {
	
	private StringBuilder builder;
	private HashMap<Class<?>, ObjectRule> objectRules;
	private Stack<Object> classStack;
	private Reader reader;
	private boolean isMapKey;
	private boolean isMapValue;
	private int indentLevel;
	
	public YAML() {
		builder = new StringBuilder();
		classStack = new Stack<Object>();
		objectRules = new HashMap<>();
		reader = new Reader(this);
	}
	
	public void setObjectRules(HashMap<Class<?>, ObjectRule> objectRule) {
		this.objectRules = objectRules;
	}
	
	private void indent() {
		for(int i = 0; i < indentLevel; i++)
			for(int o = 0; o < 4; o++)
				builder.append(" ");
	}
	
	public HashMap<Class<?>, ObjectRule> getObjectRules() {
		return objectRules;
	}
	
	public String read(Object object) {
		return reader.read(object);
	}
	
	private Object getState() {
		if(classStack.isEmpty())
			return null;
		return classStack.peek();
	}
	
	private Object getPreviousState() {
		if(classStack.isEmpty() || classStack.size() < 2)
			return null;
		return classStack.get(classStack.size() - 2);
	}

	public void startFormatting() {
		builder.append("---\n");
	}

	public void endFormatting() {
		builder.append("...");
	}

	public void addObject(ObjectWrapper formattedObject, Position position) {
		
		String field = formattedObject.format();
		
		if(isMapKey) {
			indent();
			builder.append(field + ":");
		}
		if(isMapValue) {
			if(Collection.class.isAssignableFrom(getState().getClass())) {
				indent();
				if(position.isLast()) {
					builder.append(field + "\n");
				}
				else
					builder.append(field + ",\n");
			}
			else
				builder.append(" " + field + "\n");
		}
	}

	public void startList(Position position) {

		Object object = getPreviousState();
		if(Collection.class.isAssignableFrom(object.getClass())) {
			indent();
			builder.append("[\n");
		}
		else {
			builder.append(" [ \n");
		}
		
		indentLevel++;
		isMapKey = false;
		isMapValue = true;
	}

	public void endList(Position position) {
		indentLevel--;
		indent();
		
		Object currentObject = getState();
		Object previousObject = getPreviousState();
		
		if(previousObject != null) {
			if(Collection.class.isAssignableFrom(currentObject.getClass()) && Collection.class.isAssignableFrom(previousObject.getClass())) {
				if(position.isLast())
					builder.append("]\n");
					
				else
					builder.append("], \n");
			}
			else if(Collection.class.isAssignableFrom(currentObject.getClass()) && !Collection.class.isAssignableFrom(previousObject.getClass())) {
				builder.append("]\n");
			}
		}
	}

	public void startMap(String name, Annotation[] annotations, Position position) {
		indentLevel++;
		if(name == null)
			builder.append("\n");
		else
			builder.append(name + " :\n");
	}

	public void endMap(String name, Annotation[] annotations, Position position) {
		indentLevel--;
	}

	public void startMapKey(Position position) {
		isMapKey = true;
		isMapValue = false;
	}

	public void startMapValue(Position position) {
		isMapValue = true;
		isMapKey = false;
	}

	public void stateChange(Object cls) {
		if(cls == getState()) {
			System.out.println("popped class from stack: " + cls.getClass().getName());
			classStack.pop();
		}
		else {
			System.out.println("new class on stack: " + cls.getClass().getName());	
			classStack.add(cls);
		}
	}
	
	@Override
	public String toString() {
		return builder.toString();
	}
}
