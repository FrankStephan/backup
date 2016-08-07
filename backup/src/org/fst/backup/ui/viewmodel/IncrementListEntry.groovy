package org.fst.backup.ui.viewmodel

class IncrementListEntry {

	String text
	Date secondsSinceTheEpoch

	public IncrementListEntry(String text, Date secondsSinceTheEpoch) {
		this.text = text
		this.secondsSinceTheEpoch = secondsSinceTheEpoch
	}

	@Override
	public String toString() {
		return text
	}
}
