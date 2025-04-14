package com.neocoretechs.relatrix;

public interface TransportMorphismInterface {
	public void packForTransport();
	public void unpackFromTransport();
	public TransportMorphism createTransport(Relation o);
	public Relation createRelation(TransportMorphism o);
}
