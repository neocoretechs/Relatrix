package com.neocoretechs.relatrix;

public interface TransportMorphismInterface {
	public void packForTransport();
	public void unpackFromTransport();
	public TransportMorphism createTransport(AbstractRelation o);
	public AbstractRelation createRelation(TransportMorphism o);
}
