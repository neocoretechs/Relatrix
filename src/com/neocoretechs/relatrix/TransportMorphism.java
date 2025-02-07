package com.neocoretechs.relatrix;

import java.io.Serializable;

import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;
/**
 * Set a transport object for Morphisms, which contain transient objects.
 * At the destination, recover the transient instances and set them in the morphism.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2025
 *
 */
public class TransportMorphism implements Serializable {
	private static final long serialVersionUID = 654432956755099495L;
	private Morphism morphism;
	private DBKey identity;
	private Alias alias;
	private TransactionId transactionId;
	public TransportMorphism(Morphism morphism) {
		this.morphism = morphism;
		this.identity = morphism.getIdentity();
		this.alias = morphism.getAlias();
		this.transactionId = morphism.getTransactionId();
	}
	public Morphism getMorphism() {
		if(morphism.getIdentity() == null)
			morphism.setIdentity(identity);
		if(morphism.getAlias() == null && alias != null)
			morphism.setAlias(alias);
		morphism.setTransactionId(transactionId);
		return morphism;
	}

}
