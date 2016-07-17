package org.fst.backup.ui

import groovy.beans.Bindable

import java.awt.Color

import javax.swing.text.PlainDocument

class CreateBackupModel {

	@Bindable
	File sourceDir
	@Bindable
	File targetDir
	@Bindable
	PlainDocument consoleDocument
	@Bindable
	String consoleStatus
	@Bindable
	Color consoleStatusColor
	@Bindable
	int tabIndex
	
	
	
	
	
	
	
}
