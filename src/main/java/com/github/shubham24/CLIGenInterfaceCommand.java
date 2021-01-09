package com.github.shubham24;

import java.util.Map;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Generate a java interface file through the command line
 */
@Command(name = "generate-interface", aliases = { "gen-interface", "gen-i", "g-interface", "g-i", "gi" })
class CLIGenInterfaceCommand implements Runnable {

	/**
	 * Path of folder where the generated file will be dropped
	 */
	@Option(names = { "-p",
			"--path" }, paramLabel = "PATH", description = "Path of folder where file will be generated", defaultValue = "")
	private String genFolderPath;

	/**
	 * Name of the interface
	 */
	@Parameters(index = "0", paramLabel = "<Name>", description = "Name of the interface to be generated")
	private String name;

	/**
	 * Any methods associated with the interface
	 */
	@Parameters(index = "1", arity = "0..*", paramLabel = "<Fields>", description = "0 or more methods associated with the interface")
	private String[] methods = {};

	/**
	 * Generate the interface
	 */
	@Override
	public void run() {
		String yaml = Utility.createYAMLString(Constants.INTERFACE, this.name, this.methods);
		Map<String, String> map = Utility.generateFromYAMLString(yaml);
		new InterfaceFileGenerator(map, genFolderPath).generateFile();
	}
}
