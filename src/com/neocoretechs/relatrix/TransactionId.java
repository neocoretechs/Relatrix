package com.neocoretechs.relatrix;

import java.io.Serializable;
import java.util.Objects;
/**
 * Encapsulates a transaction id guaranteed to be unique across all usage via UUID.
 * Typically represented in string form and generated via TransactionId.generate() static factory method.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024,2025
 */
public class TransactionId implements Serializable {
	private static final long serialVersionUID = -4900917167930271807L;
	private String transactionId;
	public TransactionId() {}
	public TransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public com.neocoretechs.rocksack.TransactionId getRocksackTransactionId() {
		return new com.neocoretechs.rocksack.TransactionId(transactionId);
	}
	@Override
	public int hashCode() {
		return Objects.hash(transactionId);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof TransactionId)) {
			return false;
		}
		TransactionId other = (TransactionId) obj;
		return Objects.equals(transactionId, other.transactionId);
	}
	@Override
	public String toString() {
		return transactionId;
	}
	
}
