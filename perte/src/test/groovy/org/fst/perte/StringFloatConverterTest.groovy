package org.fst.perte;

import static org.junit.Assert.*

import org.junit.Test

class StringFloatConverterTest {

	@Test
	public void testReturnsNothingOnNull() {
		assert null == new StringFloatConverter().stringToFloat(null)
	}
	
	@Test
	public void testReturnsNothingOnEmpty() {
		assert null == new StringFloatConverter().stringToFloat('')
	}

	@Test
	public void testReturnsNothingOnNaN() {
		assert null == new StringFloatConverter().stringToFloat('One.Two')
	}
	
	@Test
	public void testParsesNumberWithPointDecimalSeparator() {
		assert 1.2f == new StringFloatConverter().stringToFloat('1.2')
	}
	
	@Test
	public void testParsesNumberWithCommaDecimalSeparator() {
		assert 1.2f == new StringFloatConverter().stringToFloat('1,2')
	}
}
