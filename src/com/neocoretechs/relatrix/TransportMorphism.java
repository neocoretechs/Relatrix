package com.neocoretechs.relatrix;

import java.io.Serializable;
import java.util.List;

import com.neocoretechs.relatrix.key.DBKey;


/**
 * Set a transport object for Morphisms, which contain transient objects.
 * At the destination, recover the transient instances and set them in the abstractRelation.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2025
 *
 */
public class TransportMorphism implements Serializable, Comparable {
	private static final long serialVersionUID = 654432956755099495L;
	// AbstractRelation will store the keys to original AbstractRelation, domain, map, range instances are transient
	private Relation abstractRelation;
	private DBKey identity;
	private Alias alias;
	private TransactionId transactionId;
	// Comparables are transient in AbstractRelation, so we need to store them here
	protected Comparable domain;
	protected Comparable map;
	protected Comparable range;
	private TransportMorphism(Relation abstractRelation) {
		this.abstractRelation = abstractRelation;
		this.identity = abstractRelation.getIdentity();
		this.alias = abstractRelation.getAlias();
		this.transactionId = abstractRelation.getTransactionId();
		this.domain = abstractRelation.domain;
		this.map = abstractRelation.map;
		this.range = abstractRelation.range;
	}
	
	public static TransportMorphism createTransport(Relation result) {
		if(result == null)
			return null;
		TransportMorphism t = new TransportMorphism(result);
		resolve(result,t);
		return t;
	}
	
	public static Relation createMorphism(TransportMorphism t) {
		if(t == null)
			return null;
		Relation m = t.getMorphism();
		resolve(t,m);
		return m;
	}
	
	private Relation getMorphism() {
		if(abstractRelation.getIdentity() == null && identity != null)
			abstractRelation.setIdentity(identity);
		if(abstractRelation.getAlias() == null && alias != null)
			abstractRelation.setAlias(alias);
		abstractRelation.setTransactionId(transactionId);
		return abstractRelation;
	}
	/**
	 * Enter with target being the instance contained in newTransport
	 * @param target
	 * @param newTransport
	 */
	private static void resolve(AbstractRelation target, TransportMorphism newTransport) {
		if(target.domain instanceof AbstractRelation) {
			newTransport.setDomain(new TransportMorphism((Relation) target.domain));
			resolve((AbstractRelation) target.domain, newTransport);
		}	
		if(target.map instanceof AbstractRelation) {
			newTransport.setMap(new TransportMorphism((Relation) target.map));
			resolve((AbstractRelation) target.map, newTransport);
		}
		if(target.range instanceof AbstractRelation) {
			newTransport.setRange(new TransportMorphism((Relation) target.range));
			resolve((AbstractRelation) target.range, newTransport);
		}
	}
	/**
	 * Enter with target being the instance that contained newTransport
	 * @param target
	 * @param newTransport
	 */
	private static void resolve(TransportMorphism target, AbstractRelation newTransport) {
		if(target.domain instanceof TransportMorphism) {
			newTransport.setDomainResolved( ((TransportMorphism)target.domain).getMorphism());
			resolve((TransportMorphism) target.domain, newTransport);
		}	
		if(target.map instanceof AbstractRelation) {
			newTransport.setMapResolved( ((TransportMorphism)target.map).getMap());
			resolve((TransportMorphism) target.map, newTransport);
		}
		if(target.range instanceof AbstractRelation) {
			newTransport.setRangeResolved( ((TransportMorphism)target.range).getRange());
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
