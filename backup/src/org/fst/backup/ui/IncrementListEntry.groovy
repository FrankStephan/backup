package org.fst.backup.ui

import org.fst.backup.model.Increment

class IncrementListEntry {

	String text
	Increment increment

	public IncrementListEntry(String text, Increment increment) {
		this.text = text
		this.increment = increment
	}

	@Override
	public String toString() {
		return text
	}
}
