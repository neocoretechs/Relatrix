package com.neocoretechs.relatrix;

import java.io.Serializable;
import java.util.List;

import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;
/**
 * Set a transport object for Morphisms, which contain transient objects.
 * At the destination, recover the transient instances and set them in the morphism.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2025
 *
 */
public class TransportMorphism implements Serializable, Comparable {
	private static final long serialVersionUID = 654432956755099495L;
	// Morphism will store the keys to original Morphism, domain, map, range instances are transient
	private Morphism morphism;
	private DBKey identity;
	private Alias alias;
	private TransactionId transactionId;
	// Comparables are transient in Morphism, so we need to store them here
	protected Comparable domain;
	protected Comparable map;
	protected Comparable range;
	public TransportMorphism(Morphism morphism) {
		this.morphism = morphism;
		this.identity = morphism.getIdentity();
		this.alias = morphism.getAlias();
		this.transactionId = morphism.getTransactionId();
		this.domain = morphism.domain;
		this.map = morphism.map;
		this.range = morphism.range;
	}
	
	public static TransportMorphism createTransport(Morphism m) {
		TransportMorphism t = new TransportMorphism(m);
		resolve(m,t);
		return t;
	}
	
	public static Morphism createMorphism(TransportMorphism t) {
		Morphism m = t.getMorphism();
		resolve(t,m);
		return m;
	}
	
	public Morphism getMorphism() {
		if(morphism.getIdentity() == null)
			morphism.setIdentity(identity);
		if(morphism.getAlias() == null && alias != null)
			morphism.setAlias(alias);
		morphism.setTransactionId(transactionId);
		return morphism;
	}
	/**
	 * Enter with target being the instance contained in newTransport
	 * @param target
	 * @param newTransport
	 */
	public static void resolve(Morphism target, TransportMorphism newTransport) {
		if(target.domain instanceof Morphism) {
			newTransport.setDomain(new TransportMorphism((Morphism) target.domain));
			resolve((Morphism) target.domain, newTransport);
		}	
		if(target.map instanceof Morphism) {
			newTransport.setMap(new TransportMorphism((Morphism) target.map));
			resolve((Morphism) target.map, newTransport);
		}
		if(target.range instanceof Morphism) {
			newTransport.setRange(new TransportMorphism((Morphism) target.range));
			resolve((Morphism) target.range, newTransport);
		}
	}
	/**
	 * Enter with target being the instance that contained newTransport
	 * @param target
	 * @param newTransport
	 */
	public static void resolve(TransportMorphism target, Morphism newTransport) {
		if(target.domain instanceof TransportMorphism) {
			newTransport.setDomainResolved( ((TransportMorphism)target.domain).getMorphism());
			resolve((TransportMorphism) target.domain, newTransport);
		}	
		if(target.map instanceof Morphism) {
			newTransport.setMapResolved( ((TransportMorphism)target.map).getMap());
			resolve((TransportMorphism) target.map, newTransport);
		}
		if(target.range instanceof Morphism) {
			newTransport.setRangeResolved( ((TransportMorphism)target.range).getRange());
			resolve((TransportMorphism) target.range, newTransport);
		}
	}
	public void setDomain(Comparable domain) {
		this.domain = domain;
	}
	public void setMap(Comparable map) {
		this.map = map;
	}
	public void setRange(Comparable range) {
		this.range = range;
	}
	public Comparable getDomain() {
		return domain;
	}
	public Comparable getMap() {
		return map;
	}
	public Comparable getRange() {
		return range;
	}
	@Override
	public int compareTo(Object o) {
		return identity.compareTo(((TransportMorphism)o).identity);
	}

}
