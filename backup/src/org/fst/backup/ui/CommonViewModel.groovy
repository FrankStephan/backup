package org.fst.backup.ui

import groovy.beans.Bindable

import java.awt.Color

import javax.swing.DefaultListModel
import javax.swing.SingleSelectionModel
import javax.swing.text.PlainDocument

class CommonViewModel {

	SingleSelectionModel tabsModel

	File sourceDir
	File targetDir
	Date targetDate
	DefaultListModel<String> incrementsListModel

	PlainDocument consoleDocument
	@Bindable
	String consoleStatus
	@Bindable
	Color consoleStatusColor
}
