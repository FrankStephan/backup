package org.fst.perte

import org.codehaus.groovy.transform.NewifyASTTransformation;

class PertEstimationService {

	float pert(float optimistic, float realisitic, float pessimistic, float probability) throws IllegalArgumentException {
		if (notNaN([optimistic, realisitic, pessimistic, probability])) {
			if (pessimistic <= optimistic) {
				throw new IllegalArgumentException('Optimistic value must be greater than pessimistic')
			}

			return calculatePert(optimistic, realisitic, pessimistic, probability)
		} else {
			throw new IllegalArgumentException('NaN')
		}
	}

	private boolean notNaN(List numbers) {
		numbers.every { it != Float.NaN }
	}

	private float calculatePert(float optimistic, float realistic, float pessimistic, float probability) {
		if(probability<((realistic-optimistic)/(pessimistic-optimistic))) {
			return optimistic+Math.sqrt(probability*(realistic-optimistic)*(pessimistic-optimistic))
		}
		else {
			return pessimistic-Math.sqrt(pessimistic**2 - probability*(pessimistic-optimistic)*(pessimistic-realistic)+(realistic-optimistic)*(pessimistic-realistic)-2*pessimistic*realistic+realistic**2)
		}
	}
}
