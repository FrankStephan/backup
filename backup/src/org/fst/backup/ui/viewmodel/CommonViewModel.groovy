package org.fst.backup.ui.viewmodel

import groovy.beans.Bindable

import java.awt.Color

import javax.swing.DefaultListModel
import javax.swing.DefaultListSelectionModel
import javax.swing.SingleSelectionModel
import javax.swing.text.PlainDocument

class CommonViewModel {

	SingleSelectionModel tabsModel

	File sourceDir
	File targetDir

	DefaultListModel<IncrementListEntry> incrementsListModel
	DefaultListSelectionModel incrementsListSelectionModel

	@Bindable
	IncrementListEntry selectedIncrement

	PlainDocument consoleDocument
	@Bindable
	String consoleStatus
	@Bindable
	Color consoleStatusColor
}
