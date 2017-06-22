package org.fst.perte;

import static org.junit.Assert.*;

import org.junit.Test;

class PertEstimationServiceTest {
	
	@Test(expected=IllegalArgumentException.class)
	void testThrowsIAEOnInputNull_Opti() {
		pert(Float.NaN, 2, 3, 0.8)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void testThrowsIAEOnInputNaN_Real() {
		pert(1, Float.NaN, 3, 0.8)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void testThrowsIAEOnInputNaN_Pess() {
		pert(1, 2, Float.NaN, 0.8)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void testThrowsIAEOnInputNaN_Prob() {
		pert(1, 2, 3, Float.NaN)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void testThrowsIAEWhenOptimisticIsGreaterThanPessimistic() {
		pert(3.1, 2, 3, 0.8)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void testThrowsIAEWhenOptimisticEqualsPessimistic() {
		pert(3, 2, 3, 0.8)
	}
	
	@Test
	void testCalcualtionAboveRealisticEstimate () {
		assert String.valueOf(pert(2, 3, 5, 0.8)).startsWith('3.90')
	}
	
	@Test
	void testCalcualtionBelowRealisticEstimate () {
		assert String.valueOf(pert(1, 2, 4, 0.2)).startsWith('1.77')
	} 
	
	@Test
	void testCalcualtionAtRealisticEstimate () {
		assert pert(2, 5, 8, 0.5) == 5.0
	}
		
	float pert(float optimistic, float realisitic, float pessimistic, float probability) {
		new PertEstimationService().pert(optimistic, realisitic, pessimistic, probability)
	}

}
