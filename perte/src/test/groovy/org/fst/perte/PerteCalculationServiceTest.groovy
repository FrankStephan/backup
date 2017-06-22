package org.fst.perte
import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import org.fst.perte.PertEstimationService
import org.fst.perte.PerteCalculationService
import org.fst.perte.Point5RoundingService;
import org.junit.Test

class PerteCalculationServiceTest {

	float optimistic = 1.0
	float realisitic = 2.0
	float pessimistic = 3.0f
	float probability = 0.8f
	float pert = 2.367544468f
	float managementFactor = 1.2f
	float calculatedValue = 3.0f

	@Test
	public void testInvokesPertEstimationService() {
		MockFor pertEstimationService = new MockFor(PertEstimationService.class)
		pertEstimationService.demand.pert(1) {float optimistic, float realisitic, float pessimistic, float probability ->
			assert this.optimistic == optimistic
			assert this.realisitic == realisitic
			assert this.pessimistic == pessimistic
			assert this.probability == probability
			return pert
		}

		pertEstimationService.use {
			new PerteCalculationService().calculate(optimistic, realisitic, pessimistic, probability, managementFactor)
		}
	}

	@Test
	public void testInvokesPoint5RoundingService() {
		MockFor pertEstimationService = new MockFor(PertEstimationService.class)
		pertEstimationService.demand.pert(1) {float optimistic, float realisitic, float pessimistic, float probability ->
			return pert
		}

		MockFor point5RoundingService = new MockFor(Point5RoundingService.class)
		point5RoundingService.demand.round (1) { float number ->
			return calculatedValue
		}

		pertEstimationService.use {
			point5RoundingService.use {
				new PerteCalculationService().calculate(optimistic, realisitic, pessimistic, probability, managementFactor)
			}
		}
	}
	
	@Test
	public void testMultipliesManagementFactortoPert() {
		MockFor pertEstimationService = new MockFor(PertEstimationService.class)
		pertEstimationService.demand.pert(1) {float optimistic, float realisitic, float pessimistic, float probability ->
			return pert
		}

		MockFor point5RoundingService = new MockFor(Point5RoundingService.class)
		point5RoundingService.demand.round (1) { float number ->
			assert (this.pert * managementFactor) as float == number
			return calculatedValue
		}

		pertEstimationService.use {
			point5RoundingService.use {
				new PerteCalculationService().calculate(optimistic, realisitic, pessimistic, probability, managementFactor)
			}
		}
	}
}
