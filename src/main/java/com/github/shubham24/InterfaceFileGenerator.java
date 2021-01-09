package com.github.shubham24;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Generates a Java file containing an Interface
 */
class InterfaceFileGenerator {
	private Map<String, String> map;
	private String genFolderPath;
	private Props props;

	InterfaceFileGenerator(Map<String, String> map, String genFolderPath) {
		this.map = map;
		this.genFolderPath = genFolderPath;
		this.props = new Props(this.map.keySet());
	}

	/**
	 * Generates java file
	 */
	public void generateFile() {

		// Create a new java file based on what the user named the Record
		String path = "";
		if (this.genFolderPath.isEmpty()) {
			path = map.get(Constants.INTERFACE) + ".java";
		} else {
			path = this.genFolderPath + "/" + map.get(Constants.INTERFACE) + ".java";
		}
		File interfaceFile = new File(path);

		/*
		 * Create a new StringBuilder code that will contain the code required to make
		 * the record
		 */
		StringBuilder code = new StringBuilder(Constants.INTERFACE + " " + map.get(Constants.INTERFACE));

		if (!this.props.getExt().isEmpty()) {
			code.append(" " + Constants.EXTENDS + " " + this.map.get(Constants.EXTENDS));
		}
		code.append(" {\n");
		// If there are methods generate them
		code.append(generateMethods());
		code.append("\n}");

		// Write the code onto the record file.
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(interfaceFile))) {
			// Print a message in the console indicating that the file was generated
			System.out.println(Constants.GENERATED_MESSAGE + " " + Constants.INTERFACE + " " + interfaceFile.getName());
			writer.write(code.toString());
		} catch (IOException ie) {
			System.out.println(Constants.ERROR_MESSAGE + ": Something went wrong while generating file");
			ie.printStackTrace();
		}
	}

	/**
	 * Generates Methods
	 * 
	 * @return A string containing java code for methods
	 */
	private String generateMethods() {
		// Create a new StringBuilder that will contain all code for methods
		StringBuilder methods = new StringBuilder();

		// For each method key, generate method
		for (String k : this.props.getMethods()) {
			String name = k.substring(0, k.indexOf(Constants.METHOD_DELIMITER));
			String returnType = "";
			String signature = "";
			String params = this.map.get(k);

			/*
			 * If a signature delimiter is included assign returnType and signature
			 * 
			 * Otherwise just assign returnType
			 */
			if (k.indexOf(Constants.SIGNATURE_DELIMITER) != -1) {
				returnType = k.substring(k.indexOf(Constants.METHOD_DELIMITER) + 1,
						k.indexOf(" " + Constants.SIGNATURE_DELIMITER + " "));
				signature = k.substring(k.indexOf(" " + Constants.SIGNATURE_DELIMITER + " ") + 3);
			} else {
				returnType = k.substring(k.indexOf(Constants.METHOD_DELIMITER) + 1);
			}

			// Append Method signature to methods
			methods.append("\n\t" + signature + " " + returnType + " " + name + params + ";\n");
		}

		// Return methods as a String
		return methods.toString();

	}

}
