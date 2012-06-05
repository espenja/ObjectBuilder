package rules.formatting;

import interfaces.CustomObjectFormatter;

public class StringCasingFormatter implements CustomObjectFormatter {

	@Override
	public String format(Object object) {
		if(object instanceof String) {
			String formatted = "";
			
			boolean uppercase = true;
			
			for(int i = 0; i < ((String)object).length(); i++) {
				uppercase = !uppercase;
				formatted += uppercase ? ("" + ((String)object).charAt(i)).toLowerCase() : ("" + ((String)object).charAt(i)).toUpperCase();
			}
			return formatted;
		}
		throw new RuntimeException("Cannot format non-String objects");
	}
}
