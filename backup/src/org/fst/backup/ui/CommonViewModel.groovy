package org.fst.backup.ui

import groovy.beans.Bindable

import java.awt.Color

import javax.swing.SingleSelectionModel;
import javax.swing.text.PlainDocument

class CommonViewModel {

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
	SingleSelectionModel tabsModel
	
	
	
	
	
	
	
}
