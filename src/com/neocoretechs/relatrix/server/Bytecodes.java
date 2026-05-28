package com.neocoretechs.relatrix.server;

import java.io.Serializable;
import java.util.Objects;
/**
 * Wrapper class to disambiguate Bytecode type
 * @see HandlerClassLoader
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2026
 */
public final class Bytecodes implements Comparable, Serializable {
	private static final long serialVersionUID = -8984533733866990398L;
	private String className;
	
	public Bytecodes() {}
	
	public Bytecodes(String className) {
		super();
		this.className = className;
	}
	
	@Override
	public int compareTo(Object o) {
		return className.compareTo(((Bytecodes)o).className);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(className);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bytecodes other = (Bytecodes) obj;
		return Objects.equals(className, other.className);
	}
	
	@Override
	public String toString() {
		return className;
	}

}
