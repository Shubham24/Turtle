package com.github.shubham24;

import org.yaml.snakeyaml.Yaml;
import java.util.Map;

/**
 * Helper class. Contains utility methods.
 */
final class Utility {
	private Utility() {

	}

	/**
	 * Capitalizes a given String. Example: if the string "hello" was passed the
	 * method would return "Hello"
	 * 
	 * @param str String to be capitalized
	 * @return Capitalized String
	 */
	public static String capitalize(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * Given a type and a name, formats both into a yaml like syntax with type being
	 * the key and name being the value. Used when a name is specified directly
	 * through the command line when generating a certain type.
	 * 
	 * @param type Key value (class, interface, record, e.t.c.)
	 * @param name Name of the (class, interface, record, e.t.c.)
	 * @return A formatted value of type and name (in a yaml format structure)
	 */
	public static String fuseTypeAndName(String type, String name) {
		return type + ": " + name;
	}

	/**
	 * Given a String str in a key:value format, add a space between : to better
	 * match yaml syntax.
	 * 
	 * @param str String that needs to be seperated
	 * @return Returns String in a key: value format.
	 */
	public static String addSpaceBetweenKeyAndValue(String str) {
		return str.substring(0, str.indexOf(":") + 1) + " " + str.substring(str.indexOf(":") + 1);
	}

	/**
	 * Given the type of the file to be generated, the name, and the fields/methods
	 * associated with the type, creates a well formated (in yaml syntax) String
	 * that can be used loaded as yaml.
	 * 
	 * @param type   Type of the file (class, interface, record, e.t.c.)
	 * @param name   Name of the file/(class, interface, record, e.t.c.)
	 * @param fields All of the fields/methods associated with the (class, record.
	 *               interface, e.t.c.)
	 * @return A string that is formatted in the yaml syntax that can be loaded into
	 *         Yaml.load()
	 */
	public static String createYAMLString(String type, String name, String[] fields) {
		StringBuilder yaml = new StringBuilder(fuseTypeAndName(type, name) + "\n");
		for (int i = 0; i < fields.length; i++) {
			yaml.append(addSpaceBetweenKeyAndValue(fields[i]) + "\n");
		}

		return yaml.toString();
	}

	/**
	 * Given a well formatted String that is in yaml syntax load the yaml into a Map
	 * of <String, String>
	 * 
	 * @param yamlString A well formatted String in yaml syntax
	 * @return A map of <String,String> containing the loaded yaml
	 */
	public static Map<String, String> generateFromYAMLString(String yamlString) {
		Yaml yaml = new Yaml();
		Map<String, String> map = yaml.load(yamlString);
		return map;
	}
}
