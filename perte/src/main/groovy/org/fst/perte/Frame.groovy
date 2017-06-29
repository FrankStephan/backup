package org.fst.perte

import groovy.swing.SwingBuilder

import java.awt.Color
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingConstants
import javax.swing.WindowConstants
import javax.swing.border.EmptyBorder


class Frame {

	def width = 450
	def height = 400
	def spacing = 10


	JFrame createComponent(SwingBuilder swing, ViewModel viewModel, Float defaultProbability, Float defaultManagementFactor) {

		JFrame f
		swing.edt {
			lookAndFeel('system')
			f = frame(
					title: 'pertE',
					size: [width, height],
					locationRelativeTo: null,
					iconImage: new ImageIcon(getClass().getResource('/icon.png')).getImage(),
					defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE) {
						JPanel p = panel() {
							gridLayout(cols: 2, rows: 7, vgap: spacing, hgap: spacing)
							label('Optimistisch', horizontalAlignment: SwingConstants.RIGHT)
							textField(text: bind(target: viewModel, targetProperty: 'optimistic', converter: new StringFloatConverter().stringToFloat))
							label('Realistisch', horizontalAlignment: SwingConstants.RIGHT)
							textField(text: bind(target: viewModel, targetProperty: 'realistic', converter: new StringFloatConverter().stringToFloat))
							label('Pessimistisch', horizontalAlignment: SwingConstants.RIGHT)
							textField(text: bind(target: viewModel, targetProperty: 'pessimistic', converter: new StringFloatConverter().stringToFloat))
							label('Sicherheit', horizontalAlignment: SwingConstants.RIGHT)
							textField(id:'probability', text: bind(target: viewModel, targetProperty: 'probability', converter: new StringFloatConverter().stringToFloat))
							label('Managementfaktor', horizontalAlignment: SwingConstants.RIGHT)
							textField(id:'managementFactor', text: bind(target: viewModel, targetProperty: 'managementFactor', converter: new StringFloatConverter().stringToFloat))
							separator()
							separator()
							label('PERT-Sch\u00E4tzung', horizontalAlignment: SwingConstants.RIGHT)
							textField(text: bind(source: viewModel, sourceProperty: 'pertCalculation'), editable: false, background: Color.WHITE)
							
							probability.text = String.valueOf(defaultProbability)
							managementFactor.text = String.valueOf(defaultManagementFactor)
						}
						p.setBorder(new EmptyBorder(spacing, spacing, spacing, spacing))
					}
		}
		updateEstimationOnChange(viewModel)
		return f
	}

	private void updateEstimationOnChange(ViewModel viewModel) {
		viewModel.addPropertyChangeListener(new PropertyChangeListener() {
					void propertyChange(PropertyChangeEvent evt) {
						if (evt.propertyName != 'pertCalculation') {
							if ([
								viewModel.optimistic,
								viewModel.realistic,
								viewModel.pessimistic,
								viewModel.probability,
								viewModel.managementFactor
							].every { it != null }) {
								try {
								viewModel.pertCalculation = new PerteCalculationService().calculate(viewModel.optimistic, viewModel.realistic, viewModel.pessimistic, viewModel.probability, viewModel.managementFactor)
								} catch (IllegalArgumentException iae) {
									viewModel.pertCalculation = null
								}
							} else {
								viewModel.pertCalculation = null
							}
						}
					}
				})
	}
}
