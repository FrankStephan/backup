package org.fst.perte

class PerteCalculationService {
	
	float calculate(float optimistic, float realisitic, float pessimistic, float probability, float managementFactor) throws IllegalArgumentException {
		float pert = new PertEstimationService().pert(optimistic, realisitic, pessimistic, probability)
		return new Point5RoundingService().round((pert * managementFactor) as float)
	}

}
