package com.neocoretechs.relatrix.server;
import java.io.*;
/**
 * Manifestation by which we can store a class name and it associated bytecode payload in an object database.
 * @author Groff Copyright (C) NeoCoreTechs 4/2000, 5/2020
 */
public class ClassNameAndBytes implements Serializable, Comparable {
	private static final long serialVersionUID = 1956987497277576549L;
		private String name;
        private byte[] bytes;

        public ClassNameAndBytes() {}

        public ClassNameAndBytes(String tname) {
                name = tname;
        }
        public ClassNameAndBytes(String tname, byte[] tbytes) {
                name = tname;
                bytes = tbytes;
        }
        public byte[] getBytes() { return bytes; }
        public String getName() { return name; }
        public void setBytes(byte[] tbytes) { bytes = tbytes; }
        public void setName(String tname) { name = tname; }
        
        @Override
        public int compareTo(Object o) {
        		ClassNameAndBytes tcb = (ClassNameAndBytes)o;
                return name.compareTo(tcb.getName());
        }
}
