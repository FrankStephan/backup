package org.fst.backup.rdiff

class RDiffCommandBuilder {
    
    String build(RDiffCommandElement... commands) {
        String commandString = commands.inject('cmd /c ') { result, command -> result + command.string + ' ' } 
        return commandString.trim();
    }
	
	   
}
