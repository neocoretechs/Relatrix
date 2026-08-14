package com.neocoretechs.relatrix;

import java.io.Serializable;

import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
 * Set a transport object for Morphisms, which contain transient objects. When we want to serialize objects to the
 * database, we dont want the fields, just the keys, however, when we want to transport them over the wire, etc, we need those
 * formerly transient fields to be serializable.<p>
 * At the destination, recover the transient instances and set them in the {@link AbstractRelation}.<p>
 * We are careful to maintain references to fields only as we dont want to resolve keys, and hence no
 * reliance on the IndexResolver or IndexInstanceTables. We assume all relations have been resolved.
 * We dont want to transport a relation to the thats less than fully resolved as we disallow resolution from the client.
 * Coming from the client we dont necessarily have access to the keys so again we wont try resolution from this class.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2025,2026
 *
 */
public class TransportMorphism implements Serializable, Comparable {
	private static boolean DEBUG = false;
	private static final long serialVersionUID = 654432956755099495L;
	private Class<? extends AbstractRelation> oType;
	private DBKey identity;
	private Alias alias;
	private TransactionId transactionId;
	// Comparables are transient in AbstractRelation, so we need to store them here
	protected Comparable domain;
	protected DBKey domainKey;
	protected Comparable map;
	protected DBKey mapKey;
	protected Comparable range;
	protected DBKey rangeKey;
	private TransportMorphism(AbstractRelation abstractRelation) {
		oType = abstractRelation.getClass();
		this.identity = abstractRelation.getIdentity();
		this.alias = abstractRelation.getAlias();
		this.transactionId = abstractRelation.getTransactionId();
		this.domain = abstractRelation.getDomain();
		this.domainKey = abstractRelation.getDomainKey();
		this.map = abstractRelation.getMap();
		this.mapKey = abstractRelation.getMapKey();
		this.range = abstractRelation.getRange();
		this.rangeKey = abstractRelation.getRangeKey();
	}
	
	public TransportMorphism() {}
	
	public static TransportMorphism createTransport(AbstractRelation result) {
		if(result == null)
			return null;
		TransportMorphism t = new TransportMorphism(result);
		resolve(t);
		if(DEBUG)
			System.out.println("TransportMorphism.createTransport returning:"+t);
		return t;
	}
	/**
	 * Create a Relation from a TransportMorphism
	 * @param t The TransportMorphism to 'deserialize'
	 * @return The Relation reconstituted
	 */
	public static AbstractRelation createMorphism(TransportMorphism t) {
		if(t == null)
			return null;
		AbstractRelation m = t.getMorphism();
		resolve(m);
		if(DEBUG)
			System.out.println("TransportMorphism.createMorphisn returning:"+m);
		return m;
	}
	/**
	 * Set the field abstractRelation from TransportMorphism fields. This is part of 'deserialization'
	 * helper when we 'create' or 'resolve' morphisms from constituent elements
	 * @return The abstractRelation field once populated
	 */
	private AbstractRelation getMorphism() {
		AbstractRelation abstractRelation;
		if(oType == Relation.class) abstractRelation = new Relation();
		else
			if(oType == DomainRangeMap.class) abstractRelation = new DomainRangeMap();
			else
				if(oType == MapDomainRange.class) abstractRelation = new MapDomainRange();
				else
					if(oType == MapRangeDomain.class) abstractRelation = new MapRangeDomain();
					else
						if(oType == RangeDomainMap.class) abstractRelation = new RangeDomainMap();
						else
							if(oType == RangeMapDomain.class) abstractRelation = new RangeMapDomain();
							else
								throw new RuntimeException("Wrong type of class for getMorphism");
		abstractRelation.setIdentity(identity);
		abstractRelation.setAlias(alias);
		abstractRelation.setTransactionId(transactionId);
		abstractRelation.setDomainResolved(domain);
		abstractRelation.setDomainKey(domainKey);
		abstractRelation.setMapResolved(map);
		abstractRelation.setMapKey(mapKey);
		abstractRelation.setRangeResolved(range);
		abstractRelation.setRangeKey(rangeKey);
		return abstractRelation;
	}
	/**
	 * Recursively resolve the relationships contained in the candidate target into new TransportMorphisms
	 * @param target The Relation we are 'serializing'
	 * @param newTransport The new TransportMorphism
	 */
	private static void resolve(TransportMorphism newTransport) {
		if(newTransport.getDomain() instanceof AbstractRelation) {
			TransportMorphism newTr = new TransportMorphism((AbstractRelation) newTransport.getDomain());
			newTransport.setDomain(newTr);
			newTransport.setDomainKey(newTransport.getDomainKey());
			resolve(newTr);
		}
		if(newTransport.getMap() instanceof AbstractRelation) {
			TransportMorphism newTr = new TransportMorphism((AbstractRelation) newTransport.getMap());
			newTransport.setMap(newTr);
			newTransport.setMapKey(newTransport.getMapKey());
			resolve(newTr);
		}
		if(newTransport.getRange() instanceof AbstractRelation) {
			TransportMorphism newTr = new TransportMorphism((AbstractRelation) newTransport.getRange());
			newTransport.setRange(newTr);
			newTransport.setRangeKey(newTransport.getRangeKey());
			resolve(newTr);
		}
	}
	/**
	 * Recursively create the Relation from the TransportMorphism target
	 * @param target The TransportMorphism we are 'deserializing'
	 * @param newTransport The new Relation
	 */
	private static void resolve(AbstractRelation newTransport) {
		if(newTransport.getDomain() instanceof TransportMorphism) {
			TransportMorphism tr = (TransportMorphism)newTransport.getDomain();
			AbstractRelation ar = tr.getMorphism();
			newTransport.setDomainResolved(ar);
			newTransport.setDomainKey(tr.getDomainKey());
			resolve(ar);
		} 
		if(newTransport.getMap() instanceof TransportMorphism) {
			TransportMorphism tr = (TransportMorphism)newTransport.getMap();
			AbstractRelation ar = tr.getMorphism();
			newTransport.setMapResolved(ar);
			newTransport.setMapKey(tr.getMapKey());
			resolve(ar);
		} 
		if(newTransport.getRange() instanceof TransportMorphism) {
			TransportMorphism tr = (TransportMorphism)newTransport.getRange();
			AbstractRelation ar = tr.getMorphism();
			newTransport.setRangeResolved(ar);
			newTransport.setRangeKey(tr.getRangeKey());
			resolve(ar);
		} 
		//newTransport.setIdentity(target.getIdentity());
	}
	private void setIdentity(DBKey id) {
		this.identity = id;
	}
	private DBKey getIdentity() {
		return identity;
	}
	private void setDomain(Comparable domain) {
		this.domain = domain;
	}
	private void setMap(Comparable map) {
		this.map = map;
	}
	private void setRange(Comparable range) {
		this.range = range;
	}
	private Comparable getDomain() {
		return domain;
	}
	private Comparable getMap() {
		return map;
	}
	private Comparable getRange() {
		return range;
	}
	private void setDomainKey(DBKey domain) {
		this.domainKey = domain;
	}
	private void setMapKey(DBKey map) {
		this.mapKey = map;
	}
	private void setRangeKey(DBKey range) {
		this.rangeKey = range;
	}
	private DBKey getDomainKey() {
		return domainKey;
	}
	private DBKey getMapKey() {
		return mapKey;
	}
	private DBKey getRangeKey() {
		return rangeKey;
	}
	@Override
	public int compareTo(Object o) {
		return identity.compareTo(((TransportMorphism)o).identity);
	}
    @Override
    public String toString() {
    	String s = String.format("Class:%s Identity Key:%s%n",this.getClass(),this.getIdentity());
    	StringBuffer sb = new StringBuffer(s);
    	if(alias != null) {
    		sb.append("Alias:");
    		sb.append(alias);
    	}
    	if(transactionId != null) {
    		sb.append(" Xid:");
    		sb.append(transactionId);
    	}
    	if(alias != null || transactionId != null)
    		sb.append("\n");
    	sb.append("Class:[");
    	sb.append(getDomain() == null ? "NULL" :getDomain().getClass().getName());
    	sb.append("->");
    	sb.append(getMap() == null ? "NULL" : getMap().getClass().getName());
    	sb.append("->");
    	sb.append(getRange() == null ? "NULL" : getRange().getClass().getName());
    	sb.append("]\n");
    	sb.append("Keys:[");
    	sb.append(getDomainKey() == null ? "NULL" : getDomainKey().toString());
    	sb.append("->");
    	sb.append(getMapKey() == null ? "NULL" : getMapKey().toString());
    	sb.append("->");
    	sb.append(getRangeKey() == null ? "NULL" : getRangeKey().toString());
    	sb.append("]\n");
    	sb.append("Vals:[");
    	sb.append(getDomain() == null ? "NULL" : getDomain().toString());
    	sb.append("->");
    	sb.append(getMap() == null ? "NULL" : getMap().toString());
    	sb.append("->");
    	sb.append(getRange() == null ? "NULL" : getRange().toString());
    	sb.append("]\n");
    	return sb.toString();
    }
}
