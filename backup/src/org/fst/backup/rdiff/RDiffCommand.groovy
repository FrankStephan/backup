package org.fst.backup.rdiff

enum RDiffCommand {
    RDIFF_COMMAND('rdiff-backup'),
    VERSION_ARG('--version'),
    LIST_INCREMENTS_ARG('-l'),
    PARSABLE_OUTPUT_ARG('--parsable-output'),
	HIGHEST_VERBOSITY('-v9')

    String commandString;

    RDiffCommand(String command) {
            this.commandString = command;
    }
}
