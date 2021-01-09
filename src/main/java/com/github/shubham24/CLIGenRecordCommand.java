package com.github.shubham24;

import java.util.Map;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Generate a java record file through the command line
 */
@Command(name = "generate-record", aliases = { "gen-record", "gen-r", "g-record", "g-r", "gr" })
class CLIGenRecordCommand implements Runnable {

	/**
	 * Path of folder where the generated file will be dropped
	 */
	@Option(names = { "-p",
			"--path" }, paramLabel = "PATH", description = "Path of folder where file will be generated", defaultValue = "")
	private String genFolderPath;

	/**
	 * Name of the record
	 */
	@Parameters(index = "0", paramLabel = "<Name>", description = "Name of the record to be generated")
	private String name;

	/**
	 * Any fields/methods associated with the record
	 */
	@Parameters(index = "1", arity = "0..*", paramLabel = "<Fields>", description = "0 or more fields associated with the record")
	private String[] fields = {};

	/**
	 * Generate the record file
	 */
	@Override
	public void run() {
		String yaml = Utility.createYAMLString(Constants.RECORD, this.name, this.fields);
		Map<String, String> map = Utility.generateFromYAMLString(yaml);
		new RecordFileGenerator(map, genFolderPath).generateFile();
	}
}
