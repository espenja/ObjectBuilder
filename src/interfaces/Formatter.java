package interfaces;

import java.lang.annotation.Annotation;
import java.util.HashMap;

import rules.ObjectRule;

import model.ObjectWrapper;
import model.Position;

public interface Formatter {
	
	//Rules
	public HashMap<Class<?>, ObjectRule> getObjectRules();
	
	//Start & End of formatting
	public void startFormatting();
	public void endFormatting();
	
	//Add object
	public void addObject(ObjectWrapper formattedObject, Position position);
	
	//Lists
	public void startList(Position position);
	public void endList(Position position);
	
	//Maps
	public void startMap(String name, Annotation[] annotations, Position position);
	public void endMap(String name, Annotation[] annotations, Position position);
	public void startMapKey(Position position);
	public void startMapValue(Position position);
	
	//State
	public void stateChange(Object cls);
	
}
