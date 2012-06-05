package control;

import formatters.JSON;
import formatters.YAML;
import interfaces.Formatter;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import model.CustomObject4;
import model.ObjectWrapper;
import model.Position;
import model.Test;

public class Reader {
	
	private Stack<Object> classStack;
	private Set<Object> finishedObjects;
	private boolean failed;
	
	private String[] validMethodNames = new String[] { "get", "is", "has" };
	
	private Formatter formatter;
	
	public static void main(String[] args) {
		JSON json = new JSON();
		YAML yaml = new YAML();
		
		CustomObject4 customObject = new CustomObject4();
		customObject.setCustomObject();
		
		System.out.println(json.read(customObject));
		System.out.println(yaml.read(customObject));
	}
	
	public Reader(Formatter formatter) {
		this.formatter = formatter;
		classStack = new Stack<Object>();
		finishedObjects = new HashSet<Object>();
	}
	
	public String read(Object object) {
		formatter.startFormatting();
		readObject(object, null);
		formatter.endFormatting();
		
		return formatter.toString();
	}
	
	private void readObject(Object object, Position position) {
		
		if(checkFail(object, false))
			return;
		
		if(position == null)
			position = new Position(1, 0);
		
		if(object == null) {
			formatter.addObject(new ObjectWrapper(null), position);
			return;
		}
		
		if(
				object instanceof Boolean 	||
				object instanceof Character	||
				object instanceof Double	||
				object instanceof Enum		||			
				object instanceof Float		||
				object instanceof Integer 	||
				object instanceof Long		||
				object instanceof Short		||
				object instanceof String
												) {
			
//			formatter.stateChange(object);
			formatter.addObject(new ObjectWrapper(object), position);
		}
		
		else if(object.getClass().isAssignableFrom(Collection.class)) {
			
			Collection<?> collection = (Collection<?>) object;
			Iterator<?> collectionIterator = collection.iterator();
			Position collectionPosition = new Position(collection.size(), -1);
			
			classStack.push(object);
			formatter.stateChange(object);
			
			finishedObjects.add(object);
			formatter.startList(position);
			
			while(collectionIterator.hasNext()) {
				Object iterObject = collectionIterator.next();
				collectionPosition.increasePosition();
				readObject(iterObject, collectionPosition);
			}
			
			formatter.endList(position);
			formatter.stateChange(classStack.pop());
		}
		
		else if(object instanceof Map) {
			
			Map<?, ?> map = (Map<?, ?>) object;
			Iterator<?> mapIterator = map.keySet().iterator();
			Position mapPosition = new Position(map.keySet().size(), -1);
			
			classStack.push(object);
			formatter.stateChange(object);
			
			finishedObjects.add(object);
			formatter.startMap(null, null, position);
			
			while(mapIterator.hasNext()) {
				
				Object iterObject = mapIterator.next();
				mapPosition.increasePosition();
				
				formatter.startMapKey(mapPosition);
				readObject(iterObject, mapPosition);
				
				Object iterValue = map.get(iterObject);
				formatter.startMapValue(mapPosition);
				readObject(iterValue, mapPosition);
			}
			
			formatter.endMap(null, null, position);
			formatter.stateChange(classStack.pop());
		}
		
		else
			readCustomObject(object, position, false);
	}
	
	private void readCustomObject(Object object, Position position, boolean cameFromCustomObject) {
		
		if(checkFail(object, true))
			return;
		
		classStack.push(object);
		formatter.stateChange(object);
		finishedObjects.add(object);
		
		Class<?> cls = object.getClass();
		String className = cls.getSimpleName();
		
		Annotation[] annotations = cls.getDeclaredAnnotations();
		List<Method> methods = getValidMethods(cls);
		
		if(cameFromCustomObject)
			formatter.startMap(null, annotations, position);
		else
			formatter.startMap(className, annotations, position);
				
		Position mapPosition = new Position(methods.size(), -1);
		
		for(Method method : methods) {
			
			mapPosition.increasePosition();
			
			if(!method.isAccessible())
				method.setAccessible(true);
			
			if(!methodIsValid(method))
				continue;
			
			String methodKey = getMethodKey(method);
			
			if(methodKey == null)
				continue;
			
			formatter.startMapKey(mapPosition);
			formatter.addObject(new ObjectWrapper(methodKey), mapPosition);
			
			Object returnObject = null;
			formatter.startMapValue(mapPosition);
			
			try {
				returnObject = method.invoke(object, new Object[] { });
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
			if(returnObject == null) {
				formatter.addObject(new ObjectWrapper(null), mapPosition);
				continue;
			}
			
			if(
					returnObject instanceof Boolean 	||
					returnObject instanceof Character	||
					returnObject instanceof Double		||
					returnObject instanceof Enum		||			
					returnObject instanceof Float		||
					returnObject instanceof Integer 	||
					returnObject instanceof Long		||
					returnObject instanceof Short		||
					returnObject instanceof String
													) {
//				formatter.stateChange(returnObject);
				formatter.addObject(new ObjectWrapper(returnObject), mapPosition);
			}
			
			else if(returnObject instanceof Collection) {
				
				Collection<?> collection = (Collection<?>) returnObject;
				Iterator<?> collectionIterator = collection.iterator();
				Position collectionPosition = new Position(collection.size(), -1);
				
				classStack.push(returnObject);
				formatter.stateChange(returnObject);
				
				finishedObjects.add(returnObject);
				formatter.startList(mapPosition);
				
				while(collectionIterator.hasNext()) {
					Object iterObject = collectionIterator.next();
					collectionPosition.increasePosition();
					readObject(iterObject, collectionPosition);
				}
				
				formatter.endList(mapPosition);
				formatter.stateChange(classStack.pop());
			}
			
			else if(returnObject instanceof Map) {
				
				Map<?, ?> map = (Map<?, ?>) returnObject;
				Iterator<?> mapIterator = map.keySet().iterator();
				Position innerMapPosition = new Position(map.keySet().size(), -1);
				
				classStack.push(returnObject);
				formatter.stateChange(returnObject);
				
				finishedObjects.add(returnObject);
				formatter.startMap(null, null, mapPosition);
				
				while(mapIterator.hasNext()) {
					
					Object iterObject = mapIterator.next();
					innerMapPosition.increasePosition();
					
					formatter.startMapKey(innerMapPosition);
					readObject(iterObject, innerMapPosition);
					
					Object iterValue = map.get(iterObject);
					formatter.startMapValue(innerMapPosition);
					readObject(iterValue, innerMapPosition);
				}
				
				formatter.endMap(null, null, mapPosition);
				formatter.stateChange(classStack.pop());
			}
			
			else {
				readCustomObject(returnObject, mapPosition, true);
			}
		}
		formatter.stateChange(classStack.pop());
		formatter.endMap(className, annotations, position);
	}
	
	private List<Method> getValidMethods(Class<?> cls) {
		List<Method> methods = new ArrayList<>();
		
		for(Method method : cls.getDeclaredMethods()) {
			if(methodIsValid(method))
				methods.add(method);
		}
		return methods;
	}
	
	private boolean methodIsValid(Method method) {
		
		for(String str : validMethodNames) {
			if(method.getName().startsWith(str)) {
				if(method.getParameterTypes().length == 0)
					return true;
				return false;
			}
		}
		return false;
	}
	
	private String getMethodKey(Method method) {
		
		for(String str : validMethodNames) {
			if(method.getName().startsWith(str))
				return method.getName().substring(str.length());
		}
		return null;
	}
	
	private boolean checkFail(Object object, boolean failOnNull) {
		if(object == null) {
			if(failOnNull)
				return true;
			return false;
		}
		if(failed) {
			System.out.println("Failed on object " + object);
			return true;
		}
		
		if(finishedObjects.contains(object)) {
			System.out.println("Failed on object " + object);
			return true;
		}
		return false;
	}
}
