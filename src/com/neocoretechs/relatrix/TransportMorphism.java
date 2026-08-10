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
	private static final long serialVersionUID = 654432956755099495L;
	// AbstractRelation will store the keys to original AbstractRelation, domain, map, range instances are transient
	private AbstractRelation abstractRelation;
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
		this.abstractRelation = abstractRelation;
		this.identity = abstractRelation.getIdentity();
		this.alias = abstractRelation.getAlias();
		this.transactionId = abstractRelation.getTransactionId();
		this.domain = abstractRelation.domain;
		this.domainKey = abstractRelation.getDomainKey();
		this.map = abstractRelation.map;
		this.mapKey = abstractRelation.getMapKey();
		this.range = abstractRelation.range;
		this.rangeKey = abstractRelation.getRangeKey();
	}
	
	public TransportMorphism() {}
	
	public static TransportMorphism createTransport(AbstractRelation result) {
		if(result == null)
			return null;
		TransportMorphism t = new TransportMorphism(result);
		resolve(result,t);
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
		resolve(t,m);
		return m;
	}
	/**
	 * Set the field abstractRelation from TransportMorphism fields. This is part of 'deserialization'
	 * helper when we 'create' or 'resolve' morphisms from constituent elements
	 * @return The abstractRelation field once populated
	 */
	private AbstractRelation getMorphism() {
		if(abstractRelation.getIdentity() == null && identity != null)
			abstractRelation.setIdentity(identity);
		if(abstractRelation.getAlias() == null && alias != null)
			abstractRelation.setAlias(alias);
		abstractRelation.setTransactionId(transactionId);
		abstractRelation.setDomainResolved(domain);
		abstractRelation.setDomainKey(domainKey);
		abstractRelation.setMapResolved(map);
		abstractRelation.setMapKey(mapKey);
		abstractRelation.setRangeResolved(range);;
		abstractRelation.setRangeKey(rangeKey);
		return abstractRelation;
	}
	/**
	 * Recursively resolve the relationships contained in the candidate target into new TransportMorphisms
	 * @param target The Relation we are 'serializing'
	 * @param newTransport The new TransportMorphism
	 */
	private static void resolve(AbstractRelation target, TransportMorphism newTransport) {
		if(target.domain instanceof AbstractRelation) {
			newTransport.setDomain(new TransportMorphism((AbstractRelation) target.domain));
			resolve((AbstractRelation) target.domain, newTransport);
		}	
		if(target.map instanceof AbstractRelation) {
			newTransport.setMap(new TransportMorphism((AbstractRelation) target.map));
			resolve((AbstractRelation) target.map, newTransport);
		}
		if(target.range instanceof AbstractRelation) {
			newTransport.setRange(new TransportMorphism((AbstractRelation) target.range));
			resolve((AbstractRelation) target.range, newTransport);
		}
	}
	/**
	 * Recursively create the Relation from the TransportMorphism target
	 * @param target The TransportMorphism we are 'deserializing'
	 * @param newTransport The new Relation
	 */
	private static void resolve(TransportMorphism target, AbstractRelation newTransport) {
		if(target.domain instanceof TransportMorphism) {
			newTransport.setDomainResolved(((TransportMorphism)target.domain).getMorphism());
			newTransport.setDomainKey(((TransportMorphism)target.domain).domainKey);
			resolve((TransportMorphism) target.domain, newTransport);
		}	
		if(target.map instanceof TransportMorphism) {
			newTransport.setMapResolved(((TransportMorphism)target.map).getMap());
			newTransport.setMapKey(((TransportMorphism)target.map).mapKey);
			resolve((TransportMorphism) target.map, newTransport);
		}
		if(target.range instanceof TransportMorphism) {
			newTransport.setRangeResolved( ((TransportMorphism)target.range).getRange());
			newTransport.setRangeKey(((TransportMorphism)target.range).rangeKey);
			resolve((TransportMorphism) target.range, newTransport);
		}
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
	@Override
	public int compareTo(Object o) {
		return identity.compareTo(((TransportMorphism)o).identity);
	}

}
