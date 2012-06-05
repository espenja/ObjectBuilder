package formatters;

import interfaces.Formatter;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
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
	
	public YAML() {
		builder = new StringBuilder();
		classStack = new Stack<Object>();
		reader = new Reader(this);
	}
	
	public void setObjectRules(HashMap<Class<?>, ObjectRule> objectRule) {
		this.objectRules = objectRules;
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
		if(classStack.isEmpty())
			return null;
		return classStack.get(1);
	}

	public void startFormatting() {
		builder.append("---\n");
	}

	public void endFormatting() {
		builder.append("...");
	}

	public void addObject(ObjectWrapper formattedObject, Position position) {
		
		if(isMapKey) {
			builder.append(formattedObject + ":");
		}
		if(isMapValue) {
			if(position.isLast())
				builder.append("\"" /*+ position.getPos() + "/" + position.getMax() + ": "*/ + formattedObject + "\"");
			else
				builder.append("\"" /*+ position.getPos() + "/" + position.getMax() + ": "*/ + formattedObject + "\", ");
		}
		
//		if(getState() instanceof Collection) {
//			builder.append("- " + formattedObject + "\n");
//		}
	}

	public void startList(Position position) {
		builder.append("[ ");
		isMapKey = false;
		isMapValue = true;
	}

	public void endList(Position position) {
		if(position.isLast())
			builder.append(" ] ");
		else
			builder.append(" ], ");
	}

	public void startMap(String name, Annotation[] annotations, Position position) {
		
		if(name == null)
			builder.append(" { ");
		else
			builder.append("- " + name + " {");
	}

	public void endMap(String name, Annotation[] annotations, Position position) {
		if(position.isLast())
			builder.append(" } ");
		else
			builder.append(" }, ");
	}

	public void startMapKey(Position position) {
		isMapKey = true;
		isMapValue = false;
	}

	public void startMapValue(Position position) {
		builder.append("\n");
		isMapValue = true;
		isMapKey = false;
	}

	public void stateChange(Object cls) {
		classStack.add(cls);
	}
	
	@Override
	public String toString() {
		return builder.toString();
	}
}
