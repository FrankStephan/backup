package org.fst.backup.rdiff

enum RDiffCommandElement {
	RDIFF_COMMAND('rdiff-backup'),
	VERSION_ARG('--version'),
	HIGHEST_VERBOSITY('-v9'),
	LIST_INCREMENTS_ARG('-l'),
	PARSABLE_OUTPUT_ARG('--parsable-output'),
	LIST_AT_TIME_ARG('--list-at-time'),
	RESTORE('-r'),
	VERIFY('--verify-at-time')

	String string

	RDiffCommandElement(String string) {
		this.string = string
	}
}
