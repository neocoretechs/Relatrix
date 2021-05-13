package com.neocoretechs.relatrix.key;

import java.io.Serializable;

public class KeySet implements Serializable {
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
    
}
