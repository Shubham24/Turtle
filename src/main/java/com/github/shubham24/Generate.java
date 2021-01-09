package com.github.shubham24;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Set;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import org.yaml.snakeyaml.Yaml;

/**
 * Generate command for Turtle.
 */
@Command(name = "generate", aliases = { "g", "gen" })
class Generate implements Runnable {

	@Parameters(paramLabel = "<ymlFolderPath>", description = "Path of folder where yml files to be generated exists")
	private String ymlFolderPath;

	@Parameters(paramLabel = "<genFolderPath>", description = "Path of folder where generated files will be dropped")
	private String genFolderPath;

	@Override
	public void run() {
		// Print a message to the Console indicating that files are generating
		System.out.println("Generating files...");

		/*
		 * Create a folder path based on directory given
		 */
		File ymlFolder = new File("./" + ymlFolderPath);

		/*
		 * Read each file from the folder and map the YAML onto a map
		 */
		Yaml yaml = new Yaml();
		Map<String, String> map;
		if (ymlFolder.isDirectory()) {
			for (File file : ymlFolder.listFiles()) {
				try {
					map = yaml.load(new FileInputStream(file));
					if (hasOnlyOneDeclaration(map.keySet())) {
						generateFiles(map, "./" + genFolderPath);
					}
				} catch (FileNotFoundException fe) {
					System.out.println("The File was not found");
					fe.printStackTrace();
				} catch (SecurityException se) {
					System.out.println("There was a security violation");
					se.printStackTrace();
				}
			}
		}
	}

	/**
	 * Checks to see if there is only one declarataion of class, interface, record,
	 * e.t.c. There should not be a file that references both class and record for
	 * example
	 * 
	 * @param keys Takes a set of Keys from a map of the YAML
	 * @return True if there is only declaration of class, interface, record, e.t.c.
	 */
	private static boolean hasOnlyOneDeclaration(Set<String> keys) {
		int numDecs = 0;
		if (keys.contains("class"))
			numDecs++;
		if (keys.contains("interface"))
			numDecs++;
		if (keys.contains("record"))
			numDecs++;

		return numDecs == 1;
	}

	/**
	 * Given a map and the folder path of where to drop generated files generate the
	 * file
	 * 
	 * @param map           Map<String,String> of yaml file
	 * @param genFolderPath Path of folder where all generated files will be dropped
	 */
	private static void generateFiles(Map<String, String> map, String genFolderPath) {
		Set<String> keys = map.keySet();
		if (keys.contains(Constants.RECORD)) {
			new RecordFileGenerator(map, genFolderPath).generateFile();
		} else if (keys.contains(Constants.CLASS)) {
			new ClassFileGenerator(map, genFolderPath).generateFile();
		} else if (keys.contains(Constants.INTERFACE)) {
			new InterfaceFileGenerator(map, genFolderPath).generateFile();
		}

	}

}
