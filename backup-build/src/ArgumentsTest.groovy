


def CliBuilder cliBuilder = new CliBuilder(usage: 'build [options]')

cliBuilder.o(longOpt:'remote-script', 'Invokes the remote script during startup')
cliBuilder.s(longOpt:'source-dir', 'Default source dir')
cliBuilder.t(longOpt:'target-dir', 'Default target dir')

cliBuilder.usage()



