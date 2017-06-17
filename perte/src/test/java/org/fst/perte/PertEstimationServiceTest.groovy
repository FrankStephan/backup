package org.fst.perte;

import static org.junit.Assert.*;

import org.junit.Test;

class PertEstimationServiceTest {

	@Test(expected=IllegalArgumentException.class)
	void testThrowsIllegalArgumentExceptionOnInputZero_O() {
		new PertEstimationService().pert(0, 1, 2, 0.8)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void testThrowsIllegalArgumentExceptionOnInputZero_R() {
		new PertEstimationService().pert(1, 0, 2, 0.8)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void testThrowsIllegalArgumentExceptionOnInputZero_P() {
		new PertEstimationService().pert(1, 2, 0, 0.8)
	}

}
