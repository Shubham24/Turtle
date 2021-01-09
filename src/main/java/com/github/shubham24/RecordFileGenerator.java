package com.github.shubham24;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Generates a java file containing a record
 */
class RecordFileGenerator {
	private Map<String, String> map;
	private String genFolderPath;
	private Props props;

	RecordFileGenerator(Map<String, String> map, String genFolderPath) {
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
			path = map.get(Constants.RECORD) + ".java";
		} else {
			path = this.genFolderPath + "/" + map.get(Constants.RECORD) + ".java";
		}
		File recordFile = new File(path);

		/*
		 * Create a new StringBuilder code that will contain the code required to make
		 * the record
		 */
		StringBuilder code = new StringBuilder(Constants.RECORD + " " + map.get(Constants.RECORD));
		if (!this.props.getImp().isEmpty()) {
			code.append(" " + Constants.IMPLEMENTS + " " + this.map.get(Constants.IMPLEMENTS));
		}
		code.append(" (");
		// Add each specified field
		int i = 0;
		for (String k : this.props.getFields()) {
			if (!k.equals("record")) {
				code.append(map.get(k) + " " + k);
				// If there are more fields to be added, add a comma
				if (i < this.props.getFields().size() - 1) {
					code.append(", ");
				}
			}
			i++;
		}
		code.append(") {");
		// Generate methods if needbe
		if (!this.props.getMethods().isEmpty()) {
			code.append(generateMethods());
		}
		code.append("\n}");

		// Write the code onto the record file.
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(recordFile))) {
			writer.write(code.toString());
			// Print a message in the console indicating that the file was generated
			System.out.println(Constants.GENERATED_MESSAGE + " " + Constants.RECORD + " " + recordFile.getName());
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

		/*
		 * Create a StringBuilder that would contain the code required to generate //
		 * methods
		 */
		StringBuilder methods = new StringBuilder("\n\n\t/* Methods */");

		// Generate code for each method
		for (String k : this.props.getMethods()) {
			String name = "";
			String returnType = "";
			String signature = "";
			String params = "";
			String returnStatement = "";

			// Set the name of the method
			name = k.substring(0, k.indexOf(Constants.METHOD_DELIMITER));

			/*
			 * If the yml file contains a signature demlimiter, then assign a value to the
			 * signature and a value to the return type.
			 * 
			 * Otherwise just assign a value to the return type
			 */
			if (k.indexOf(Constants.SIGNATURE_DELIMITER) != -1) {
				returnType = k.substring(k.indexOf(Constants.METHOD_DELIMITER) + 1, k.indexOf(" > "));
				signature = k.substring(k.indexOf(" > ") + 3);
			} else {
				returnType = k.substring(k.indexOf(Constants.METHOD_DELIMITER) + 1);
				signature = Constants.PUBLIC_SIGNATURE;
			}

			/*
			 * If there is a return delmiter included assign the params and the return
			 * statement.
			 * 
			 * Otherwise, just assign the params.
			 */
			if (map.get(k).indexOf(" -> ") != -1) {
				params = map.get(k).substring(0, map.get(k).indexOf(" -> "));
				returnStatement = map.get(k).substring(map.get(k).indexOf(" -> ") + 4);
			} else {
				params = map.get(k);
			}

			/*
			 * Format the method based on return statement and return type.
			 * 
			 * If there is no return statement create a method signature like:
			 * "public int shape (int length) {}"
			 * 
			 * If there is a return statement but return type is void, don't include the
			 * return keyword
			 * 
			 * If there is a return statement and the return type is not void include the
			 * return keyword
			 */
			if (returnStatement.isEmpty()) {
				methods.append("\n\n\t" + signature + " " + returnType + " " + name + params + " {}");
			} else if (returnType.equals("void")) {
				methods.append("\n\n\t" + signature + " " + returnType + " " + name + params + " {");
				methods.append("\n\t\t" + returnStatement + ";");
				methods.append("\n\t}");
			} else {
				methods.append("\n\n\t" + signature + " " + returnType + " " + name + params + " {");
				methods.append("\n\t\treturn " + returnStatement + ";");
				methods.append("\n\t}");
			}

		}

		// return String containing all of the code
		return methods.toString();
	}
}
