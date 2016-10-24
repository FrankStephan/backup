package org.fst.backup.misc


println args

CliBuilder cli = new CliBuilder()

cli.s('Default source directory', args: 1, argName: 'source directory')
cli.t('Default target directory', args: 1, argName: 'target directory')

cli.usage()

def options = cli.parse(args)

println options.s
println options.t



