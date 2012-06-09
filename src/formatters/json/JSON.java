package formatters.json;

import interfaces.Formatter;

import java.lang.annotation.Annotation;
import java.util.HashMap;

import model.ObjectWrapper;
import model.Position;
import rules.ObjectRule;
import rules.formatting.NumberWithXDecimalPlaces;
import rules.formatting.StringCasingFormatter;
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
		loadSettings();
	}
	
	private void loadSettings() {
//		Settings.numberDecimalRule.addCustomObjectFormatter(new NumberWithXDecimalPlaces(Settings.decimalFormatLength));
//		ObjectRule stringRule = new ObjectRule(String.class, String.class);
//		stringRule.addCustomObjectFormatter(new StringCasingFormatter());
//		objectRules.put(String.class, stringRule);
//		mapMultipleObjectRules(Settings.numberDecimalRule, Settings.getNumberDecimalRuleClasses());
	}
	
	private void mapMultipleObjectRules(ObjectRule objectRule, Class<?>... classes) {
		for(Class<?> cls : classes) {
			objectRules.put(cls, objectRule);
		}
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
			builder.append("\"" + formattedObject + "\": ");
		}
		if(isMapValue) {
			if(position.isLast())
				builder.append("\"" + formattedObject.format() + "\"");
			else
				builder.append("\"" + formattedObject.format() + "\", ");
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
		builder.append(" } ");
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
		return builder.toString();//.replaceAll("\\s+", "");
	}

	public void stateChange(Object cls) {
	}
}