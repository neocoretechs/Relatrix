package com.neocoretechs.relatrix.key;

import java.io.Serializable;
/**
 * Class to contain serialzable set of keys to maintain order of domain/map/range relationships in Relatrix.
 * @author Jonathan N. Groff Copyright (C) NeoCoreTechs 2022,2023
 *
 */
public class KeySet implements Serializable, Comparable {
	private static final long serialVersionUID = -2614468413972955193L;
	private DBKey domainKey = new DBKey();
    private DBKey mapKey = new DBKey();
    private DBKey rangeKey = new DBKey();
    public KeySet() {}
	public DBKey getDomainKey() {
		return domainKey;
	}
	public void setDomainKey(DBKey domainKey) {
		this.domainKey = domainKey;
	}
	public DBKey getMapKey() {
		return mapKey;
	}
	public void setMapKey(DBKey mapKey) {
		this.mapKey = mapKey;
	}
	public DBKey getRangeKey() {
		return rangeKey;
	}
	public void setRangeKey(DBKey rangeKey) {
		this.rangeKey = rangeKey;
	}
	@Override
	public boolean equals(Object o) {
		return domainKey.equals(((KeySet)o).domainKey) &&
				mapKey.equals(((KeySet)o).mapKey) &&
				rangeKey.equals(((KeySet)o).rangeKey);
	}
	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + domainKey.hashCode();
	    result = prime * result + (int) (mapKey.hashCode() ^ (mapKey.hashCode() >>> 32));
	    result = prime * result + rangeKey.hashCode();
	    return result;
	}
	public boolean isValid() {
		return domainKey.isValid() && mapKey.isValid() && rangeKey.isValid();
	}
	public boolean isDomainKeyValid() {
		return domainKey.isValid();
	}
	public boolean isMapKeyValid() {
		return mapKey.isValid();
	}
	public boolean isRangeKeyValid() {
		return rangeKey.isValid();
	}
	public boolean domainKeyEquals(KeySet o) {
		return domainKey.equals(o.domainKey);
	}
	public boolean mapKeyEquals(KeySet o) {
		return mapKey.equals(o.mapKey);
	}
	public boolean rangeKeyEquals(KeySet o) {
		return rangeKey.equals(o.rangeKey);
	}
	@Override
	public int compareTo(Object o) {
		int i = domainKey.compareTo(((KeySet)o).domainKey);
		if(i != 0)
			return i;
		i = mapKey.compareTo(((KeySet)o).mapKey);
		if(i != 0)
			return i;
		return rangeKey.compareTo(((KeySet)o).rangeKey);
	}   
}
