package com.neocoretechs.relatrix.key;

/**
 * Subclass of KeySet that checks primary key of domain and map.
 * @author Jonathan Groff Copyright NeoCoreTechs 2024
 *
 */
public final class PrimaryKeySet extends KeySet {
	private static boolean DEBUG = false;
	
	public PrimaryKeySet() {}
	
	public PrimaryKeySet(KeySet dmr) {
		setDomainKey(dmr.getDomainKey());
		setMapKey(dmr.getMapKey());
		setRangeKey(new DBKey(DBKey.nullKey, DBKey.nullKey));
	}
	
	@Override
	public int compareTo(Object o) {
		if(DEBUG)
			System.out.println("PrimaryKeyset CompareTo "+this+", "+o+" domain this:"+this.getDomainKey()+" domain o:"+((KeySet)o).getDomainKey()+" map this:"+getMapKey()+", map o:"+((KeySet)o).getMapKey());
		int i = getDomainKey().compareTo(((KeySet)o).getDomainKey());
		if(i != 0) {
			if(DEBUG)
				System.out.println("PrimaryKeyset CompareTo returning "+i+" at DomainKey");
			return i;
		}
		i = getMapKey().compareTo(((KeySet)o).getMapKey());
		if(DEBUG)
			System.out.println("PrimaryKeyset CompareTo returning "+i+" at MapKey");
		return i;
	}
	
	
	@Override
	public boolean equals(Object o) {
	return getDomainKey().equals(((KeySet)o).getDomainKey()) &&
			getMapKey().equals(((KeySet)o).getMapKey());
	}
	
	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
		result = prime * result + getDomainKey().hashCode();
		result = prime * result + (int) (getMapKey().hashCode() ^ (getMapKey().hashCode() >>> 32));
	    return result;
	}
	
}
