package rules.formatting;

import interfaces.CustomObjectFormatter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashSet;

public class NumberWithXDecimalPlaces implements CustomObjectFormatter {
	
	private String numberFormat;
	private HashSet<Class<?>> supportedClasses;
	
	public NumberWithXDecimalPlaces(int numberOfDecimals) {
		
		supportedClasses = new HashSet<>();
		supportedClasses.addAll(Arrays.asList(Double.class, Integer.class, Float.class, Short.class, Long.class));
		
		numberFormat = "#0.";
		for(int i = 0; i < numberOfDecimals; i++) {
			numberFormat += "0";
		}
	}
	
	public HashSet<Class<?>> getSupportedClasses() {
		return supportedClasses;
	}

	public String format(Object object) {
		if(supportedClasses.contains(object.getClass())) {
			NumberFormat decimalFormat = new DecimalFormat(numberFormat);
			
			double number = 0;
			
			if(object instanceof Integer)
				number = number + (Integer)object;
			else if(object instanceof Float)
				number = number + (Float)object;
			else if(object instanceof Short)
				number = number + (Short)object;
			else if(object instanceof Long)
				number = number + (Long)object;
			return decimalFormat.format(number);
		}
		throw new RuntimeException("Cannot format non-String objects");
	}
}
