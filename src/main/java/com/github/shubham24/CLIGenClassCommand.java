package com.github.shubham24;

import java.util.Map;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Generate a java class file through the command line
 */
@Command(name = "generate-class", aliases = { "gen-class", "gen-c", "g-class", "g-c", "gc" })
class CLIGenClassCommand implements Runnable {

	/**
	 * Path of folder where the generated file will be dropped
	 */
	@Option(names = { "-p",
			"--path" }, paramLabel = "PATH", description = "Path of folder where file will be generated", defaultValue = "")
	private String genFolderPath;

	/**
	 * Name of the class
	 */
	@Parameters(index = "0", paramLabel = "<Name>", description = "Name of the class to be generated")
	private String name;

	/**
	 * Any fields/methods associated with the class
	 */
	@Parameters(index = "1", arity = "0..*", paramLabel = "<Fields>", description = "0 or more fields associated with the class")
	private String[] fields = {};

	/**
	 * Generate the class file
	 */
	@Override
	public void run() {
		String yaml = Utility.createYAMLString(Constants.CLASS, this.name, this.fields);
		Map<String, String> map = Utility.generateFromYAMLString(yaml);
		new ClassFileGenerator(map, genFolderPath).generateFile();
	}
}