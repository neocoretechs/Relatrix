package com.neocoretechs.relatrix.stream;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.relatrix.Result;

	/**
	 * Abstract factory pattern to create the proper Relatrix stream for set retrieval from the various flavors
	 * of findSet: HeadSet from selected result set,SubSet from result set, or tailSet from findSet return ordered set.
	 * The iterator will, in general, return an array of Comparable corresponding to the number of elements specified 
	 * in the findSet retrieval indicated by the "?" parameter. <br/>
	 * This factory generates the proper iterator based on our findSet semantics.<p/>
	 * Overloaded methods support transaction context.
	 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2022
	 *
	 */
	public abstract class StreamFactory {
		private static boolean DEBUG = false; 
	    private static char dop, mop, rop;
		/**
		 * Create the stream. Factory method, abstract.
		 * @return RelatrixStream subclass that return {@link Result} tuples/morphisms
		 * @throws IllegalAccessException
		 * @throws IOException
		 */
		public abstract Stream<?> createStream() throws IllegalAccessException, IOException;
		/**
		 * Create the stream. Factory method, abstract.
		 * @param alias database alias
		 * @return RelatrixStream subclass that return {@link Result} tuples/morphisms
		 * @throws IllegalAccessException
		 * @throws IOException
		 * @throws NoSuchElementException if alias doestn exist
		 */
		public abstract Stream<?> createStream(String alias) throws IllegalAccessException, IOException, NoSuchElementException;
		/**
		 * Create the iterator. Factory method, abstract, subclass. Allows subclasses to create specific types of RelatrixStream
		 * @return RelatrixStream subclass that return {@link Result} tuples/morphisms
		 * @throws IllegalAccessException
		 * @throws IOException
		 */
		protected abstract Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException;
		/**
		 * Create the iterator. Factory method, abstract, subclass. Allows subclasses to create specific types of RelatrixStream
		 * @param alias database alias
		 * @param tdmr The template {@link Morphism}
		 * @return RelatrixStream subclass that return {@link Result} tuples/morphisms
		 * @throws IllegalAccessException
		 * @throws IOException
		 * @throws NoSuchElementException if alias doestn exist
		 */
		protected abstract Stream<?> createRelatrixStream(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException;
		
		/**
		* Check operator for Relatrix Findset, determine legality return corresponding value for our dmr_return structure
		* @param marg the char operator
		* @return the translated ordinal, either 1 for ? or 2 for *
		* @exception IllegalArgumentException the operator is invalid
		*/
		protected static short checkOp(char marg) throws IllegalArgumentException {
		        if( marg == Relatrix.OPERATOR_TUPLE_CHAR )
		                return 1;
		        else
		        	if( marg == Relatrix.OPERATOR_WILDCARD_CHAR)
		                	return 2;
		        throw new IllegalArgumentException("findSet takes only objects, '?' or '*' for Relatrix operators");
		}
		/**
		 * Determine if we are returning identity relationship morphisms
		 * @param dop The domain predicate from retrieval operation
		 * @param mop Map predicate
		 * @param rop Range
		 * @return true if all arguments are wildcard values
		 */
		protected static boolean isReturnRelationships(char dop, char mop, char rop) {
			return( dop == Relatrix.OPERATOR_WILDCARD_CHAR && 
					mop == Relatrix.OPERATOR_WILDCARD_CHAR && 
					rop == Relatrix.OPERATOR_WILDCARD_CHAR );
		}
		
		protected static boolean isReturnRelationships(short[] dmr_return) {
			return( dmr_return[1] == 0 && dmr_return[2] == 0 && dmr_return[3] == 0 );
		}
		
		protected static int processTripletParams(Object darg, Object marg, Object rarg) {
		    int mode = 0;
		    dop = mop = rop = ' ';
			//
		    if( (darg instanceof String) ) {
		    	// see if its user operator
		    	if( ((String)darg).equals( Relatrix.OPERATOR_TUPLE ))
		    		dop = '?';
		    	else
		    		if( ((String)darg).equals( Relatrix.OPERATOR_WILDCARD ))
		    			dop = '*';           
		    }
		    if( (marg instanceof String) ) {
		    	// see if its user operator
		    	if( ((String)marg).equals( Relatrix.OPERATOR_TUPLE ))
		    		mop = '?';
		    	else
		    		if( ((String)marg).equals( Relatrix.OPERATOR_WILDCARD ))
		    			mop = '*';                        
		    }
		    if( (rarg instanceof String) ) {
		    	// see if its user operator
		    	if( ((String)rarg).equals( Relatrix.OPERATOR_TUPLE ))
		    		rop = '?';
		    	else
		    		if( ((String)rarg).equals( Relatrix.OPERATOR_WILDCARD ))
		    			rop = '*';               
		    }
		    if(dop != ' ' && mop != ' ' && rop != ' ')
		    	mode = 0;
		    else
		    	if(dop != ' ' && mop != ' ' && rop == ' ')
		    		mode = 1;
		    	else
		    		if(dop != ' ' && mop == ' ' && rop != ' ')
		    			mode = 2;
		    		else
		    			if(dop != ' ' && mop == ' ' && rop == ' ')
		    				mode = 3;
		    			else
		    				if( dop == ' ' && mop != ' ' && rop != ' ')
		    					mode = 4;
		    				else
		    					if( dop == ' ' && mop != ' ' && rop == ' ')
		    						mode = 5;
		    					else
		    						if( dop == ' ' && mop == ' ' && rop != ' ')
		    							mode = 6;
		    						else
		    							if( dop == ' ' && mop == ' ' && rop == ' ')
		    								mode = 7;
		    							else
		    								throw new RuntimeException("Malformed triplet for :"+darg+"->"+marg+"->"+rarg);
		    return mode;
		}
		/**
		 * Factory method, create the abstract factory which will manufacture our specific stream instances.
		 * @param darg The domain argument from the driving findSet method being invoked. 
		 * @param marg The map argument from the driving findSet method being invoked. 
		 * @param rarg The range (codomain) argument from the driving findSet method being invoked. 
		 * @return The abstract factory that will manufacture the specific instance of our Stream
		 * @throws IllegalArgumentException
		 * @throws IOException
		 */
		public static StreamFactory createFactory(Object darg, Object marg, Object rarg) throws IllegalArgumentException, IOException  {
		    if( DEBUG )
		        System.out.println("Relatrix StreamFactory findSetStream setting mode for "+darg+" "+marg+" "+rarg);
			
		    switch(processTripletParams(darg, marg, rarg)) {
               case 0:
                       return new FindSetStreamMode0(dop, mop, rop);
               case 1:
                       return new FindSetStreamMode1(dop, mop, rarg);
               case 2:
                       return new FindSetStreamMode2(dop, marg, rop);
               case 3:
                       return new FindSetStreamMode3(dop, marg, rarg);
               case 4:
                       return new FindSetStreamMode4(darg, mop, rop);
               case 5:
                       return new FindSetStreamMode5(darg, mop, rarg);
               case 6:
                       return new FindSetStreamMode6(darg, marg, rop);
               case 7:
                   	   return new FindSetStreamMode7(darg, marg, rarg);
        	    default:
                    throw new IllegalArgumentException("The findSetStream factory mode is not supported.");
		    }
		}
		/**
		 * Factory method, create the abstract factory which will manufacture our specific stream instances.
		 * @param xid The transaction id.
		 * @param darg The domain argument from the driving findSet method being invoked. 
		 * @param marg The map argument from the driving findSet method being invoked. 
		 * @param rarg The range (codomain) argument from the driving findSet method being invoked. 
		 * @return The abstract factory that will manufacture the specific instance of our Stream
		 * @throws IllegalArgumentException
		 * @throws IOException
		 */
		public static StreamFactory createFactoryTransaction(String xid, Object darg, Object marg, Object rarg) throws IllegalArgumentException, IOException  {
		    
		    if( DEBUG )
		        System.out.println("Relatrix StreamFactory findSetStream setting mode for "+darg+" "+marg+" "+rarg);
			
		    switch(processTripletParams(darg, marg, rarg)) {
               case 0:
                       return new FindSetStreamMode0Transaction(xid, dop, mop, rop);
               case 1:
                       return new FindSetStreamMode1Transaction(xid, dop, mop, rarg);
               case 2:
                       return new FindSetStreamMode2Transaction(xid, dop, marg, rop);
               case 3:
                       return new FindSetStreamMode3Transaction(xid, dop, marg, rarg);
               case 4:
                       return new FindSetStreamMode4Transaction(xid, darg, mop, rop);
               case 5:
                       return new FindSetStreamMode5Transaction(xid, darg, mop, rarg);
               case 6:
                       return new FindSetStreamMode6Transaction(xid, darg, marg, rop);
               case 7:
                   	   return new FindSetStreamMode7Transaction(xid, darg, marg, rarg);
        	    default:
                    throw new IllegalArgumentException("The findSetStream transaction factory mode is not supported.");
		    }
		}
	
		/**
		 * Create a factory generating headSet stream sets for the specified objects
		 * @param darg
		 * @param marg
		 * @param rarg
		 * @return
		 * @throws IOException 
		 * @throws IllegalArgumentException 
		 */
		public static StreamFactory createHeadsetFactory(Object darg, Object marg, Object rarg, Object ... endarg) throws IllegalArgumentException, IOException {

			if( DEBUG )
			        System.out.println("Relatrix StreamFactory findHeadSetStream setting mode for "+darg+" "+marg+" "+rarg);
				
			switch(processTripletParams(darg, marg, rarg)) {
	               case 0:
	           			return new FindHeadSetStreamMode0(dop, mop, rop, endarg);
	               case 1:
	                    return new FindHeadSetStreamMode1(dop, mop, rarg, endarg);
	               case 2:
	                    return new FindHeadSetStreamMode2(dop, marg, rop, endarg);
	               case 3:
	                    return new FindHeadSetStreamMode3(dop, marg, rarg, endarg);
	               case 4:
	                    return new FindHeadSetStreamMode4(darg, mop, rop, endarg);
	               case 5:
	                    return new FindHeadSetStreamMode5(darg, mop, rarg, endarg);
	               case 6:
	                    return new FindHeadSetStreamMode6(darg, marg, rop, endarg);
	               case 7:
	            	   	return new FindHeadSetStreamMode7(darg, marg, rarg, endarg);
	        	    default:
	                    throw new IllegalArgumentException("The findHeadsetStream factory mode is not supported.");
			}
		}
		/**
		 * Create a factory generating headSet sets for the specified objects from a transaction context
		 * @param xid the transaction Id
		 * @param darg
		 * @param marg
		 * @param rarg
		 * @return
		 * @throws IOException 
		 * @throws IllegalArgumentException 
		 */
		public static StreamFactory createHeadsetFactoryTransaction(String xid, Object darg, Object marg, Object rarg, Object ... endarg) throws IllegalArgumentException, IOException {

			if( DEBUG )
			        System.out.println("Relatrix StreamFactoryTransaction findHeadSetStream setting mode for "+darg+" "+marg+" "+rarg);
				
			switch(processTripletParams(darg, marg, rarg)) {
	               case 0:
	            	   return new FindHeadSetStreamMode0Transaction(xid, dop, mop, rop, endarg);
	               case 1:
	                    return new FindHeadSetStreamMode1Transaction(xid, dop, mop, rarg, endarg);
	               case 2:
	                    return new FindHeadSetStreamMode2Transaction(xid, dop, marg, rop, endarg);
	               case 3:
	                    return new FindHeadSetStreamMode3Transaction(xid, dop, marg, rarg, endarg);
	               case 4:
	                    return new FindHeadSetStreamMode4Transaction(xid, darg, mop, rop, endarg);
	               case 5:
	                    return new FindHeadSetStreamMode5Transaction(xid, darg, mop, rarg, endarg);
	               case 6:
	                    return new FindHeadSetStreamMode6Transaction(xid, darg, marg, rop, endarg);
	               case 7:
	            	   	return new FindHeadSetStreamMode7Transaction(xid, darg, marg, rarg, endarg);
	        	    default:
	                    throw new IllegalArgumentException("The findHeadsetStream transaction factory mode is not supported.");
			}
		}
		/**
		 * Create subset stream factory in preparation for creating a subset stream for a findSet operation.
		 * @param darg
		 * @param marg
		 * @param rarg
		 * @param endarg
		 * @return The StreamFactory by which we may facilitate the creation of our RelatrixStream
		 * @throws IllegalArgumentException
		 * @throws IOException
		 */
		public static StreamFactory createSubsetFactory(Object darg, Object marg, Object rarg, Object... endarg) throws IllegalArgumentException, IOException {

			if( DEBUG )
			        System.out.println("Relatrix StreamFactory findSubSetStream setting mode for "+darg+" "+marg+" "+rarg);
				
			switch(processTripletParams(darg, marg, rarg)) {
	               case 0:
	            	   return new FindSubSetStreamMode0(dop, mop, rop, endarg);
	               case 1:
	                   return new FindSubSetStreamMode1(dop, mop, rarg, endarg);
	               case 2:
	                   return new FindSubSetStreamMode2(dop, marg, rop, endarg);
	               case 3:
	                   return new FindSubSetStreamMode3(dop, marg, rarg, endarg);
	               case 4:
	                   return new FindSubSetStreamMode4(darg, mop, rop, endarg);
	               case 5:
	                   return new FindSubSetStreamMode5(darg, mop, rarg, endarg);
	               case 6:
	                   return new FindSubSetStreamMode6(darg, marg, rop, endarg);
	               case 7:
	            	   return new FindSubSetStreamMode7(darg, marg, marg, endarg);
	        	    default:
	                    throw new IllegalArgumentException("The findSubsetSteam factory mode is not supported.");
			}	
		}
		/**
		 * Create subset stream factory in preparation for creating a subset stream for a findSet operation.
		 * @param xid Transaction id
		 * @param darg
		 * @param marg
		 * @param rarg
		 * @param endarg
		 * @return The StreamFactory by which we may facilitate the creation of our RelatrixStream
		 * @throws IllegalArgumentException
		 * @throws IOException
		 */
		public static StreamFactory createSubsetFactoryTransaction(String xid, Object darg, Object marg, Object rarg, Object... endarg) throws IllegalArgumentException, IOException {
			    
			if( DEBUG )
			        System.out.println("Relatrix StreamFactory findSubSet setting mode for "+darg+" "+marg+" "+rarg);
				
			switch(processTripletParams(darg, marg, rarg)) {
	               case 0:
	            	   return new FindSubSetStreamMode0Transaction(xid, dop, mop, rop, endarg);
	               case 1:
	                   return new FindSubSetStreamMode1Transaction(xid, dop, mop, rarg, endarg);
	               case 2:
	                   return new FindSubSetStreamMode2Transaction(xid, dop, marg, rop, endarg);
	               case 3:
	                   return new FindSubSetStreamMode3Transaction(xid, dop, marg, rarg, endarg);
	               case 4:
	                   return new FindSubSetStreamMode4Transaction(xid, darg, mop, rop, endarg);
	               case 5:
	                   return new FindSubSetStreamMode5Transaction(xid, darg, mop, rarg, endarg);
	               case 6:
	                   return new FindSubSetStreamMode6Transaction(xid, darg, marg, rop, endarg);
	               case 7:
	           			return new FindSubSetStreamMode7Transaction(xid, darg, marg, marg, endarg);
	        	    default:
	                    throw new IllegalArgumentException("The findSubsetStream factory mode is not supported.");
			}	
		}
		
	}