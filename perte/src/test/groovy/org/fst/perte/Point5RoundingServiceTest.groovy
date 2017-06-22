package org.fst.perte;

import static org.junit.Assert.*

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized.class)
class Point5RoundingServiceTest {
	
	float input
	float expectedResult

	@Parameters
	public static Collection<Object[]> params() {
	Collection<Object[]> list = [
			[1.24f, 1.0f].toArray(), 
			[1.241f, 1.0f].toArray(),
			[1.249f, 1.0f].toArray()
			[1.25f, 1.5f].toArray(), 
			[1.26f, 1.5f].toArray(), 
			[1.5f, 1.5f].toArray(), 
			[1.74f, 1.5f].toArray(), 
			[1.75f, 2.0f].toArray(), 
			[1.76f, 2.0f].toArray(), 
			[2.0f, 2.0f].toArray(),
			[Float.NaN, Float.NaN].toArray(),
			]
		return list
	}
	
	public Point5RoundingServiceTest(float input, float expectedResult) {
		this.input = input
		this.expectedResult = expectedResult
	}

	@Test
	void test() {
		assert expectedResult == new Point5RoundingService().round(input)
	}
	
}
