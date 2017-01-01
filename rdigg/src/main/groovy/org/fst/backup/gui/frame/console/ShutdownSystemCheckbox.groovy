package org.fst.backup.gui.frame.console

import groovy.swing.SwingBuilder

import java.awt.event.ItemEvent
import java.awt.event.ItemListener

import javax.swing.JCheckBox

import org.codehaus.groovy.binding.FullBinding
import org.fst.backup.gui.CommonViewModel

class ShutdownSystemCheckbox {

	JCheckBox createComponent(CommonViewModel commonViewModel, SwingBuilder swing) {
		JCheckBox checkBox = swing.checkBox(
				text: 'System nach Abschluss herunterfahren',
				)
		FullBinding binding = swing.bind(source: commonViewModel, sourceProperty: 'shutdownSystemOnFinish', target: checkBox.model, targetProperty: 'selected')
		checkBox.model.addItemListener(new ItemListener() {
					void itemStateChanged(ItemEvent e) {
						binding.reverseUpdate()
					}
				})
		return checkBox
	}
}
