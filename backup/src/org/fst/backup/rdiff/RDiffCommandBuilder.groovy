package org.fst.backup.rdiff

class RDiffCommandBuilder {
    
    String build(RDiffCommand... commands) {
        String commandString = commands.inject('cmd /c ') { result, command -> result + command.commandString + ' ' } 
        return commandString.trim();
    }
	
	   
}
