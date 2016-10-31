package org.fst.backup.gui.frame

import groovy.swing.SwingBuilder

import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.WindowConstants

import org.fst.backup.gui.CommonViewModel


class Frame {

	def width = 1100
	def height = 400

	JFrame createComponent(CommonViewModel commonViewModel, SwingBuilder swing) {
		JFrame f
		swing.edt {
			lookAndFeel('nimbus')
			f = frame(
					title: 'rdigg',
					size: [width, height],
					locationRelativeTo: null,
					defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE,
					iconImage: new ImageIcon(getClass().getResource('/icon.gif')).getImage()
					) {
						new TabsPane().createComponent(commonViewModel, swing)
					}
		}
		return f
	}
}
