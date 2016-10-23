
Process process =
		'''cd C:/dev/repository/backup/backup/
jar cvf classes.jar .'''.execute()
process.waitForProcessOutput(System.out, System.err)




