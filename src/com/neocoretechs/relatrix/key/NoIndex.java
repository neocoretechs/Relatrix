package com.neocoretechs.relatrix.key;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
/**
 * Can be passed to the range of a relation to create a non-indexed range value 
 * whose instance is stored in DBKey alone to conserve space, enhance performance. e.g:
 * Relatrix.store(d,m,NoIndex.create(object)); <p>
 * Instance of NoIndex will be returned from range selection operations, and getInstance() can be used to retrieve the
 * non-Comprable Object.<p>
 * The implementation of Comparable is merely to conform to the d,m,r types, and Serializable is merely for wire transport to client.
 * Instances of this class will NOT be stored in the tables, nor used for ordering.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2025
 */
public class NoIndex implements Comparable, Serializable {
	private static final long serialVersionUID = 1L;
	private DBKey dbKey;
	private Object instance;
	
	public NoIndex() {}
	
	private NoIndex(Object instance) {
		this.instance = instance;
		this.dbKey = new DBKey(UUID.randomUUID());
	}
	public NoIndex(DBKey key, Object o) {
		this.dbKey = key;
		this.instance = o;
	}
	public Object getInstance() {
		return instance;
	}
	public DBKey getDBKey() {
		return dbKey;
	}
	public static NoIndex create(Object instance) {
		return new NoIndex(instance);
	}
	public void update(Object instance) {
		this.instance = instance;
	}
	@Override
	public int hashCode() {
		return dbKey.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof NoIndex)) {
			return false;
		}
		NoIndex other = (NoIndex) obj;
		return Objects.equals(dbKey, other.dbKey);
	}
	@Override
	public int compareTo(Object o) {
		return dbKey.compareTo(((NoIndex)o).dbKey);
	}

}
