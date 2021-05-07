package com.neocoretechs.relatrix.key;

import java.io.Serializable;

public final class InstanceIndex implements Comparable<InstanceIndex>, Serializable {
	private static final long serialVersionUID = 7241928009601092784L;
	private Integer index = -1;
	public InstanceIndex(int index) {
		this.index = index;
	}
	public Integer getIndex() {
		return index;
	}
	public boolean isValid() {
		return index == -1 ? false : true;
	}
	@Override
	public String toString() {
		return String.format("%s: key:%d%n", this.getClass().getName(), index);
	}
	@Override
	public int compareTo(InstanceIndex o) {
		return index.compareTo(o.index);
	}

}
