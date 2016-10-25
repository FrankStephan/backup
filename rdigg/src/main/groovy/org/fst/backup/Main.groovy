package org.fst.backup

import org.fst.backup.gui.frame.Frame
import org.fst.backup.model.Configuration
import org.fst.backup.service.ReadCliService

Frame frame = new Frame()

Configuration configuration = new ReadCliService().read(args)
frame.commonViewModel.sourceDir = configuration.defaultSourceDir
frame.commonViewModel.targetDir = configuration.defaultTargetDir

frame.show()
