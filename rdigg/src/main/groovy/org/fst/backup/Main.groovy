package org.fst.backup

import org.fst.backup.gui.frame.Frame
import org.fst.backup.model.Configuration
import org.fst.backup.service.ReadCliService

Configuration configuration
try {
	configuration = new ReadCliService().read(args)
} catch (Exception e) {
}

Frame frame = new Frame()
frame.show()
