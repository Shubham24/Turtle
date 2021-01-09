package com.github.shubham24;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Helps split into declaration, fields, methods, e.t.c.
 */
class Props {
	private String declaration = "";
	private String ext = "";
	private String imp = "";

	private LinkedHashSet<String> fields = new LinkedHashSet<>();
	private LinkedHashSet<String> methods = new LinkedHashSet<>();

	Props(Set<String> keys) {
		/*
		 * Split each key in keys based on specific categories
		 */
		for (String k : keys) {
			if (Constants.DECS.contains(k)) {
				this.declaration = k;
			} else if (k.equals(Constants.EXTENDS)) {
				this.ext = k;
			} else if (k.equals(Constants.IMPLEMENTS)) {
				this.imp = k;
			} else if (k.indexOf(Constants.METHOD_DELIMITER) != -1) {
				this.methods.add(k);
			} else {
				this.fields.add(k);
			}
		}
	}

	/* Getters for each category */
	public String getDeclaration() {
		return this.declaration;
	}

	public String getExt() {
		return this.ext;
	}

	public String getImp() {
		return this.imp;
	}

	public Set<String> getFields() {
		return this.fields;
	}

	public Set<String> getMethods() {
		return this.methods;
	}

}
