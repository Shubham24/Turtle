package com.github.shubham24;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Helper class that contains constants used in this project
 */
import picocli.CommandLine.Help.Ansi;

public final class Constants {

	/* Static Strings */
	static final String RECORD = "record";
	static final String CLASS = "class";
	static final String INTERFACE = "interface";
	static final String EXTENDS = "extends";
	static final String IMPLEMENTS = "implements";
	static final String BOOLEAN = "boolean";
	static final String GET = "get";
	static final String SET = "set";
	static final String IS = "is";
	static final String PUBLIC_SIGNATURE = "public";

	/* Static Delimiters */
	static final String METHOD_DELIMITER = "#";
	static final String SIGNATURE_DELIMITER = ">";
	static final String DEFAULT_VALUE_DELIMITER = "=";
	static final String METHOD_RETURN_DELIMITER = "->";

	/* Console Output Messages */
	static final String GENERATED_MESSAGE = Ansi.AUTO.string("@|bold,green Generated|@");
	static final String ERROR_MESSAGE = Ansi.AUTO.string("@|bold, red Error|@");
	/* Sets containing multiple constants */
	static final Set<String> DECS = new HashSet<>(Arrays.asList(RECORD, CLASS, INTERFACE));

	private Constants() {
	}
}
