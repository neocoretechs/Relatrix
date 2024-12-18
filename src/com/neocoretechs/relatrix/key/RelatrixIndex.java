package com.neocoretechs.relatrix.key;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.UUID;
/**
 * An index to the components of idempotent Relatrix entries. These indexes are used to uniquely identify
 * entries in the {@link DBKey} tables and are originally generated from random Java UUID's
 * but encapsulated here to give predictable behavior and offer greater flexibility.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 *
 */
public final class RelatrixIndex implements Cloneable, Comparable, Externalizable {
	private long msb;
	private long lsb;
	
	public RelatrixIndex() {}
	
	public RelatrixIndex(long msb, long lsb) {
		this.msb = msb;
		this.lsb = lsb;
	}
	
	public RelatrixIndex(RelatrixIndex o) {
		this.msb = o.msb;
		this.lsb = o.lsb;
	}
	
	/**
	 * @return the msb
	 */
	public long getMsb() {
		return msb;
	}

	/**
	 * @param msb the msb to set
	 */
	public void setMsb(long msb) {
		this.msb = msb;
	}

	/**
	 * @return the lsb
	 */
	public long getLsb() {
		return lsb;
	}

	/**
	 * @param lsb the lsb to set
	 */
	public void setLsb(long lsb) {
		this.lsb = lsb;
	}

	public UUID getAsUUID() {
		return new UUID(msb, lsb);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (lsb ^ (lsb >>> 32));
		result = prime * result + (int) (msb ^ (msb >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RelatrixIndex other = (RelatrixIndex) obj;
		if (lsb != other.lsb)
			return false;
		if (msb != other.msb)
			return false;
		return true;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeLong(msb);
		out.writeLong(lsb);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		msb = in.readLong();
		lsb = in.readLong();
	}

	@Override
	public int compareTo(Object o) {
		int i = Long.compareUnsigned(msb, ((RelatrixIndex)o).msb);
		if(i != 0)
			return i;
		return Long.compareUnsigned(lsb, ((RelatrixIndex)o).lsb);
	}
	
	@Override
	public Object clone() {
		return new RelatrixIndex(this);
	}
	
	@Override
	public String toString() {
		return String.format("[0x%16X,0x%16X]", msb,lsb);
	}

}
