package org.fst.perte

import groovy.swing.SwingBuilder

class Application {

	public static final float DEFAULT_PROBABILITY = 0.8f

	public static final float DEFAULT_MANAGEMENT_FACTOR = 1.15f
	
	Application() {
		def viewModel = new ViewModel()
		new Frame().createComponent(new SwingBuilder(), viewModel, DEFAULT_PROBABILITY, DEFAULT_MANAGEMENT_FACTOR).setVisible(true)
	} 

}
