package org.fst.backup.test.manual

import java.awt.Window

import javax.swing.JFrame

import org.fst.backup.Application
import org.fst.backup.service.ShutdownSystemService

println 'Manual test for system shutdown'

new Application(null)
Window[] windows = JFrame.getOwnerlessWindows()
assert 1 == windows.size()
JFrame frame = windows[0]
assert 'rdigg' == frame.getTitle()
new ShutdownSystemService().shutdown(frame)


/*
 * Expected result: 
 *  - System is shutdown
 *  - Console contains entry '>> Initiating system shutdown'
 */






