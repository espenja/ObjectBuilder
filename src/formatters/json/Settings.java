package formatters.json;

import rules.ObjectRule;

class Settings {
	
	//Rules for numbers
	public static ObjectRule numberDecimalRule = new ObjectRule(Double.class, Integer.class, Long.class, Short.class, Float.class);
	public static int decimalFormatLength = 2;
	
	public static Class<?>[] getNumberDecimalRuleClasses() {
		Class<?>[] numberDecimalRuleClasses = new Class<?>[numberDecimalRule.getClasses().size()];
		int counter = 0;
		for(Object object : numberDecimalRule.getClasses()) {
			numberDecimalRuleClasses[counter] = (Class<?>)object;
			counter++;
		}
		return numberDecimalRuleClasses;
	}
	//End rules for numbers
}
