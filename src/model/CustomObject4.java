package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomObject4 {
	
	private String stringField = "espen";
	private int integerField = 123;
	private char characterField = 'c';
	private boolean booleanField = true;
	private double doubleField = 123.45;
	private float floatField = 123.4567f;
	private long longField = 123456789123456789l;
	private CustomEnum enumField = CustomEnum.TEST1;
	private List<Object> listField = new ArrayList<Object>();
	private Map<String, Object> mapField = new HashMap<String, Object>();
	private CustomObject4 customObject;
	
	public CustomObject4() {
		listField.add("index1");
		listField.add("index2");
		
		List<Object> list2 = new ArrayList<Object>();
		list2.add("index11");
		list2.add("index22");
		list2.add("index33");
		listField.add(list2);
		
		List<Object> list3 = new ArrayList<Object>();
		list3.add("Index1InHashMap");
		
		mapField.put("HALLOOOOOOO", "jacobsson");
		mapField.put("espen", 123);
		mapField.put("marit", list3);
	}
	
	public void setCustomObject() {
		customObject = new CustomObject4();
	}
	
	public CustomObject4 getCustomObject() {
		return customObject;
	}

	public String getStringField() {
		return stringField;
	}

	public int getIntegerField() {
		return integerField;
	}

	public char getCharacterField() {
		return characterField;
	}

	public boolean isBooleanField() {
		return booleanField;
	}

	public double getDoubleField() {
		return doubleField;
	}

	public float getFloatField() {
		return floatField;
	}

	public long getLongField() {
		return longField;
	}

	public CustomEnum getEnumField() {
		return enumField;
	}

	public List<Object> getListField() {
		return listField;
	}

	public Map<String, Object> getMapField() {
		return mapField;
	}
}
