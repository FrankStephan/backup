package org.fst.shuffle_my_music.gui

import groovy.swing.SwingBuilder

import javax.swing.JFrame
import javax.swing.WindowConstants

import org.fst.shuffle_my_music.service.ShuffleSongsService

class Frame {


	def width = 100
	def height = 100

	JFrame createComponent(SwingBuilder swing) {
		JFrame f
		swing.edt {
			lookAndFeel('nimbus')
			f = frame(
					title: 'shuffle-my-music',
					size: [width, height],
					locationRelativeTo: null,
					defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE,
					//					iconImage: new ImageIcon(getClass().getResource('/icon.gif')).getImage()
					) {
						button(text: 'Start shuffling', actionPerformed: {new ShuffleSongsService().createShuffledSongList(null, null, 0)} )

					}
		}
		return f
	}
}
