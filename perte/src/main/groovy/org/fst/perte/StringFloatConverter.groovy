package org.fst.perte


class StringFloatConverter {
	
	Closure<Float> stringToFloat = { String string ->
		try {
			return Float.parseFloat(convertCommaToPoint(string))
		} catch (NumberFormatException nfe) {
		} catch (NullPointerException npe) {
		}
	}

	private String convertCommaToPoint(String string) {
		string.replace(',', '.')
	}
}
