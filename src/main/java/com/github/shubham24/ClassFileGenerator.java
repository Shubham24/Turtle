package com.github.shubham24;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Generates a class file
 */
class ClassFileGenerator {

	// Fields
	private Map<String, String> map;
	private String genFolderPath;
	private Props props;

	// Constructor
	ClassFileGenerator(Map<String, String> map, String genFolderPath) {
		this.map = map;
		this.genFolderPath = genFolderPath;
		this.props = new Props(map.keySet());
	}

	/**
	 * Generates class file
	 */
	public void generateFile() {
		String path = "";
		if (!this.genFolderPath.isEmpty()) {
			path = this.genFolderPath + "/" + map.get(Constants.CLASS) + ".java";
		} else {
			path = map.get(Constants.CLASS) + ".java";
		}
		File classFile = new File(path);
		StringBuilder code = new StringBuilder("class " + this.map.get(Constants.CLASS));
		if (!this.props.getExt().isEmpty())
			code.append(" " + Constants.EXTENDS + " " + this.map.get(Constants.EXTENDS));
		if (!this.props.getExt().isEmpty())
			code.append(" " + Constants.IMPLEMENTS + " " + this.map.get(Constants.IMPLEMENTS));
		code.append(" {\n");
		if (!this.props.getFields().isEmpty())
			code.append(generateFields());
		code.append(generateConstructors());
		if (!this.props.getFields().isEmpty())
			code.append(generateGettersSetters());
		if (!this.props.getMethods().isEmpty())
			code.append(generateMethods());
		code.append("\n}");
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(classFile))) {
			writer.write(code.toString());
			System.out.println(Constants.GENERATED_MESSAGE + " " + Constants.CLASS + " " + classFile.getName());
		} catch (IOException ie) {
			System.out.println(Constants.ERROR_MESSAGE + ": Something went wrong while generating file");
			ie.printStackTrace();
		}
	}

	/**
	 * Generates all associated fields
	 * 
	 * @return A string containing well fields in java syntax
	 */
	private String generateFields() {
		StringBuilder fields = new StringBuilder("\n\t/* Fields */\n\n");
		for (String k : this.props.getFields()) {
			fields.append("\tprivate " + this.map.get(k) + " " + k + ";\n");
		}
		return fields.toString();
	}

	/**
	 * Generates all constructors
	 * 
	 * @return A string containing constructors in java syntax
	 */
	private String generateConstructors() {
		StringBuilder constructors = new StringBuilder("\n\t/* Constructors */\n\n");

		/* Create default Constructor */
		constructors.append("\t" + this.map.get(Constants.CLASS) + "(){}\n");

		/* Create Constructor with all fields */
		if (!this.props.getFields().isEmpty()) {
			constructors.append("\n\t" + this.map.get(Constants.CLASS) + "(");
			int i = 0;
			for (String k : this.props.getFields()) {
				if (k.indexOf(Constants.DEFAULT_VALUE_DELIMITER) != -1) {
					String f = k.substring(0, k.indexOf(" " + Constants.DEFAULT_VALUE_DELIMITER));
					constructors.append(this.map.get(k) + " " + f);
				} else {
					constructors.append(this.map.get(k) + " " + k);
				}
				if (i < this.props.getFields().size() - 1) {
					constructors.append(", ");
				}
				i++;
			}
			constructors.append(") {");
			for (String k : this.props.getFields()) {
				if (k.indexOf(Constants.DEFAULT_VALUE_DELIMITER) != -1) {
					String type = k.substring(0, k.indexOf(" " + Constants.DEFAULT_VALUE_DELIMITER));
					constructors.append("\n\t\tthis." + type + " = " + type + ";");
				} else {
					constructors.append("\n\t\tthis." + k + " = " + k + ";");
				}
			}
			constructors.append("\n\t}\n");
		}

		/* Create Constructors without fields that have default values */
		if (this.props.getFields().size() != fieldsWithoutDefaultValues(this.props.getFields()).size()) {
			constructors.append("\n\t" + map.get(Constants.CLASS) + "(");
			int i = 0;
			for (String k : fieldsWithoutDefaultValues(this.props.getFields())) {
				constructors.append(map.get(k) + " " + k);
				if (i < fieldsWithoutDefaultValues(this.props.getFields()).size() - 1) {
					constructors.append(", ");
				}
				i++;
			}
			constructors.append(") {");

			for (String k : fieldsWithoutDefaultValues(this.props.getFields())) {
				constructors.append("\n\t\tthis." + k + " = " + k + ";");
			}
			constructors.append("\n\t}\n");
		}

		return constructors.toString();
	}

	/**
	 * Generates all getters and setters
	 * 
	 * @return A string containing all getters and setters in java syntax
	 */
	private String generateGettersSetters() {
		StringBuilder gettersSetters = new StringBuilder("\n\t/* Getters and Setters */");
		for (String k : this.props.getFields()) {
			String name = k;
			if (k.indexOf(Constants.DEFAULT_VALUE_DELIMITER) != -1) {
				name = k.substring(0, k.indexOf(" " + Constants.DEFAULT_VALUE_DELIMITER));
			}

			String get = Constants.GET + Utility.capitalize(name);
			String set = Constants.SET + Utility.capitalize(name);

			if (this.map.get(k).equalsIgnoreCase(Constants.BOOLEAN)) {
				get = Constants.IS + Utility.capitalize(name);
			}

			/* Getter Method */
			gettersSetters.append("\n\n\tpublic " + this.map.get(k) + " " + get + "() {");
			gettersSetters.append("\n\t\treturn this." + name + ";");
			gettersSetters.append("\n\t}");

			/* Setter Method */
			gettersSetters.append("\n\n\tpublic void " + set + "(" + this.map.get(k) + " " + name + ") {");
			gettersSetters.append("\n\t\t this." + name + " = " + name + ";");
			gettersSetters.append("\n\t}");
		}
		return gettersSetters.toString();
	}

	/**
	 * Generates all methods
	 * 
	 * @return
	 */
	private String generateMethods() {
		StringBuilder methods = new StringBuilder("\n\n\t/* Methods */");

		for (String k : this.props.getMethods()) {
			String name = "";
			String returnType = "";
			String signature = "";
			String params = "";
			String returnStatement = "";
			name = k.substring(0, k.indexOf(Constants.METHOD_DELIMITER));
			if (k.indexOf('>') != -1) {
				returnType = k.substring(k.indexOf(Constants.METHOD_DELIMITER) + 1, k.indexOf(" > "));
				signature = k.substring(k.indexOf(" > ") + 3);
			} else {
				returnType = k.substring(k.indexOf(Constants.METHOD_DELIMITER) + 1);
				signature = "public";
			}
			if (map.get(k).indexOf(" -> ") != -1) {
				params = map.get(k).substring(0, map.get(k).indexOf(" -> "));
				returnStatement = map.get(k).substring(map.get(k).indexOf(" -> ") + 4);
			} else {
				params = map.get(k);
			}
			if (returnStatement.equals("")) {
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
		return methods.toString();
	}

	private Set<String> fieldsWithoutDefaultValues(Set<String> fields) {
		Set<String> f = new HashSet<>();
		for (String k : fields) {
			if (k.indexOf(Constants.DEFAULT_VALUE_DELIMITER) == -1) {
				f.add(k);
			}
		}
		return f;
	}
}
