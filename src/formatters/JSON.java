package formatters;

import interfaces.Formatter;

import java.lang.annotation.Annotation;
import java.util.HashMap;

import rules.ObjectRule;

import model.ObjectWrapper;
import model.Position;
import control.Reader;

public class JSON implements Formatter {

	private Reader reader;
	private HashMap<Class<?>, ObjectRule> objectRules;
	private StringBuilder builder;
	private boolean isMapKey;
	private boolean isMapValue;
	
	public JSON() {
		builder = new StringBuilder();
		objectRules = new HashMap<>();
		reader = new Reader(this);
		createObjectRules();
	}
	
	private void createObjectRules() {
		ObjectRule objectRule = new ObjectRule(String.class);	
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
	
	public void startFormatting() {
		builder.append("{ ");
	}

	public void endFormatting() {
		builder.append(" }");
	}

	public void addObject(ObjectWrapper formattedObject, Position position) {
		
		if(isMapKey) {
			builder.append("\"" /*+ position.getPos() + "/" + position.getMax() + ": "*/ + formattedObject + "\": ");
		}
		if(isMapValue) {
			if(position.isLast())
				builder.append("\"" /*+ position.getPos() + "/" + position.getMax() + ": "*/ + formattedObject + "\"");
			else
				builder.append("\"" /*+ position.getPos() + "/" + position.getMax() + ": "*/ + formattedObject + "\", ");
		}
		
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
			builder.append("{ ");
		else
			builder.append("\"" + name + "\": { ");
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
		isMapValue = true;
		isMapKey = false;
	}
	
	@Override
	public String toString() {
		return builder.toString().replaceAll("\\s+", "");
	}

	public void stateChange(Object cls) {
	}
}