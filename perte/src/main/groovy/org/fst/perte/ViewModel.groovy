package org.fst.perte

import groovy.beans.Bindable;
import groovy.transform.ToString;

@Bindable
@ToString
class ViewModel {
	
	Float optimistic = null
	Float realistic = null
	Float pessimistic = null
	Float probability = null
	Float managementFactor = null
	Float pertCalculation = null

}
