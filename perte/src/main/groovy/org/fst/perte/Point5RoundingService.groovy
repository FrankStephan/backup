package org.fst.perte

class Point5RoundingService {
	
	float round (float number) {
		if (number != Float.NaN) {
			int decimals = extract2Decimals(number)
			int preDecimals = extractPreDecimals(number)
			if (decimals < 25) {
				return preDecimals as float
			} else if (decimals >= 25 && decimals < 75 ) {
				return preDecimals + 0.5f
			} else {
				return preDecimals + 1.0f
			}
		} else {
			return Float.NaN
		}
	}
	
	private int extract2Decimals(float number) {
		char[] digits = Math.floor(number * 100).toInteger().toString().toCharArray()
		digits[digits.length-1]
		String decimals = new String(digits[digits.length-2..digits.length-1] as char[])
		return Integer.parseInt(decimals)
	}
	
	private int extractPreDecimals(float number) {
		return Math.floor(number) as int
	}

}
