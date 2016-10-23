package org.fst.backup.ui.frame

import groovy.swing.SwingBuilder

import javax.swing.DefaultListModel
import javax.swing.DefaultListSelectionModel
import javax.swing.DefaultSingleSelectionModel
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.WindowConstants
import javax.swing.text.PlainDocument

import org.fst.backup.ui.CommonViewModel

class Frame {

	def swing = new SwingBuilder()

	CommonViewModel commonViewModel = new CommonViewModel()

	def width = 1100
	def height = 400

	JFrame window

	Frame() {
		commonViewModel.consoleDocument = new PlainDocument()
		commonViewModel.tabsModel = new DefaultSingleSelectionModel()
		commonViewModel.incrementsListModel = new DefaultListModel<String>()
		commonViewModel.incrementsListSelectionModel = new DefaultListSelectionModel()
		commonViewModel.consoleStatus = 'Status'
	}

	void show() {
		swing.edt {
			lookAndFeel('nimbus')
			window = frame(
					title: 'rdigg',
					size: [width, height],
					locationRelativeTo: null,
					show: true,
					defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE,
					iconImage: new ImageIcon(getClass().getResource('/icon.gif')).getImage()
					) {
						new TabsPane().createComponent(commonViewModel, swing)
					}
		}
	}
}
