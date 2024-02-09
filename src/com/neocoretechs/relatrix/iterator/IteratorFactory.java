package com.neocoretechs.relatrix.iterator;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.Relatrix;

	/**
	 * Abstract factory pattern to create the proper Relatrix iterator for set retrieval from the various flavors
	 * of findSet: HeadSet from selected result set,SubSet from result set, or tailSet from findSet return ordered set.
	 * The iterator will, in general, return an array of Comparable corresponding to the number of elements specified 
	 * in the findSet retrieval indicated by the "?" parameter. 
	 * {@link Relatrix.OPERATOR_TUPLE_CHAR} {@link Relatrix.OPERATOR_WILDCARD_CHAR} <br/>
	 * This factory generates the proper iterator based on our findSet semantics.<p/>
	 * Overloaded methods support transaction context.
	 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2022
	 *
	 */
	public abstract class IteratorFactory {
		private static boolean DEBUG = false; 
		/**
		 * Create the iterator. Factory method, abstract.
		 * @return RelatrixIterator subclass that returns Comparable[] tuples/morphisms
		 * @throws IllegalAccessException
		 * @throws IOException
		 */
		public abstract Iterator<?> createIterator() throws IllegalAccessException, IOException;
		
		/**
		* Create the iterator. Factory method, abstract.
		* @param alias the database alias
		* @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
		* @throws IllegalAccessException
		* @throws IOException
		* @throws NoSuchElementException if the alias is not found
		*/
		public abstract Iterator<?> createIterator(String alias) throws IllegalAccessException, IOException, NoSuchElementException;
		
		/**
		 * Create the iterator. Factory method, abstract, subclass. Allows subclasses to create specific types of RelatrixIterator
		 * @param tdmr the Morphism template that defines the selection parameters for the iterator
		 * @return RelatrixIterator subclass that return Comparable[] tuples/morphisms
		 * @throws IllegalAccessException
		 * @throws IOException
		 */
		protected abstract Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException;
		
		/**
		 * Create the iterator. Factory method, abstract, subclass. Allows subclasses to create specific types of RelatrixIterator
		 * @param alias the database alias
		 * @param tdmr the Morphism template that defines the selection parameters for the iterator
		 * @return RelatrixIterator subclass that return Comparable[] tuples/morphisms
		 * @throws IllegalAccessException
		 * @throws IOException
		 */
		protected abstract Iterator<?> createRelatrixIterator(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException;
		
		/**
		* Check operator for Relatrix Findset, determine legality return corresponding value for our dmr_return structure
		* @param marg the char operator that specifies a wildcard or tuple return (* or ?)
		* @return the translated ordinal, either 1 for ? {@link Relatrix.OPERATOR_TUPLE_CHAR} or 2 for * {@link Relatrix.OPERATOR_WILDCARD_CHAR} 
		* @exception IllegalArgumentException the operator is invalid
		*/
		protected static short checkOp(char marg) throws IllegalArgumentException
		{
		        if( marg == Relatrix.OPERATOR_TUPLE_CHAR )
		                return 1;
		        else
		        	if( marg == Relatrix.OPERATOR_WILDCARD_CHAR)
		                	return 2;
		        throw new IllegalArgumentException("findSet takes only objects, '?' or '*' for Relatrix operators");
		}
		/**
		 * Determine if we are returning identity relationship morphisms. {@link Relatrix.OPERATOR_WILDCARD_CHAR} 
		 * @param dop The domain predicate from retrieval operation, either wildcard or tuple return
		 * @param mop Map predicate, wildcard or tuple return
		 * @param rop Range predicate, wildcard or tuple return
		 * @return true if all arguments are wildcard values
		 */
		protected static boolean isReturnRelationships(char dop, char mop, char rop) {
			return( dop == Relatrix.OPERATOR_WILDCARD_CHAR && 
					mop == Relatrix.OPERATOR_WILDCARD_CHAR && 
					rop == Relatrix.OPERATOR_WILDCARD_CHAR );
		}
		/**
		 * Determine if we are returning relationships; i.e. if we have specified all wildcard operators in our
		 * parameters. (*,*,*)
		 * @param dmr_return the retrun value domain/map/range template
		 * @return true if all 3 values are 0 indicating all wildcards specified
		 */
		protected static boolean isReturnRelationships(short[] dmr_return) {
			return( dmr_return[1] == 0 && dmr_return[2] == 0 && dmr_return[3] == 0 );
		}

		/**
		 * Factory method, create the abstract factory which will manufacture our specific iterator instances.
		 * @param darg The domain argument from the driving findSet method being invoked. 
		 * @param marg The map argument from the driving findSet method being invoked. 
		 * @param rarg The range (codomain) argument from the driving findSet method being invoked. 
		 * @return The abstract factory that will manufacture the specific instance of our Iterator
		 * @throws IllegalArgumentException
		 * @throws IOException
		 */
		public static IteratorFactory createFactory(Object darg, Object marg, Object rarg) throws IllegalArgumentException, IOException  {
		    char dop, mop, rop;
		    byte mode = 0;
			//
		    dop = mop = rop = ' ';
			//
		    if( (darg instanceof String) ) {
		          	// see if its user operator
		         	if( ((String)darg).compareTo( Relatrix.OPERATOR_TUPLE ) == 0 )
		         		dop = '?';
		            else
		            	if( ((String)darg).compareTo( Relatrix.OPERATOR_WILDCARD ) == 0)
		                     	dop = '*';
		                else
		                        mode = 4;                
		    } else
		        mode = 4;
		    if( (marg instanceof String) ) {
		         	// see if its user operator
		           	if( ((String)marg).compareTo( Relatrix.OPERATOR_TUPLE ) == 0 )
		           		mop = '?';
		            else
		            	if( ((String)marg).compareTo( Relatrix.OPERATOR_WILDCARD ) == 0)
		                      	mop = '*';
		                else
		                       	mode ^= 2;                
		    } else
			   mode ^= 2;
		    if( (rarg instanceof String) ) {
		            // see if its user operator
		        	if( ((String)rarg).compareTo( Relatrix.OPERATOR_TUPLE ) == 0 )
		        		rop = '?';
		            else
		            	if( ((String)rarg).compareTo( Relatrix.OPERATOR_WILDCARD ) == 0)
		            		rop = '*';
		            	else
		            		mode ^= 1;                
		    } else
			   mode ^= 1;
		    
		    if( DEBUG )
		        System.out.println("Relatrix IteratorFactory findSet setting mode "+String.valueOf(mode)+" for "+darg+" "+marg+" "+rarg);
			
		    switch(mode) {
               case 0:
                       return new FindSetMode0(dop, mop, rop);
               case 1:
                       return new FindSetMode1(dop, mop, rarg);
               case 2:
                       return new FindSetMode2(dop, marg, rop);
               case 3:
                       return new FindSetMode3(dop, marg, rarg);
               case 4:
                       return new FindSetMode4(darg, mop, rop);
               case 5:
                       return new FindSetMode5(darg, mop, rarg);
               case 6:
                       return new FindSetMode6(darg, marg, rop);
               case 7:
                   	   return new FindSetMode7(darg, marg, rarg);
        	    default:
                    throw new IllegalArgumentException("The findSet factory mode "+mode+" is not supported.");
		    }
		}
		
		/**
		 * Factory method, create the abstract factory which will manufacture our specific transactional iterator instances.
		 * @param xid Transaction Id
		 * @param darg The domain argument from the driving findSet method being invoked. 
		 * @param marg The map argument from the driving findSet method being invoked. 
		 * @param rarg The range (codomain) argument from the driving findSet method being invoked. 
		 * @return The abstract factory that will manufacture the specific instance of our Iterator
		 * @throws IllegalArgumentException
		 * @throws IOException
		 */
		public static IteratorFactory createFactory(String xid, Object darg, Object marg, Object rarg) throws IllegalArgumentException, IOException  {
		    char dop, mop, rop;
		    byte mode = 0;
			//
		    dop = mop = rop = ' ';
			//
		    if( (darg instanceof String) ) {
		          	// see if its user operator
		         	if( ((String)darg).compareTo( Relatrix.OPERATOR_TUPLE ) == 0 )
		         		dop = '?';
		            else
		            	if( ((String)darg).compareTo( Relatrix.OPERATOR_WILDCARD ) == 0)
		                     	dop = '*';
		                else
		                        mode = 4;                
		    } else
		        mode = 4;
		    if( (marg instanceof String) ) {
		         	// see if its user operator
		           	if( ((String)marg).compareTo( Relatrix.OPERATOR_TUPLE ) == 0 )
		           		mop = '?';
		            else
		            	if( ((String)marg).compareTo( Relatrix.OPERATOR_WILDCARD ) == 0)
		                      	mop = '*';
		                else
		                       	mode ^= 2;                
		    } else
			   mode ^= 2;
		    if( (rarg instanceof String) ) {
		            // see if its user operator
		        	if( ((String)rarg).compareTo( Relatrix.OPERATOR_TUPLE ) == 0 )
		        		rop = '?';
		            else
		            	if( ((String)rarg).compareTo( Relatrix.OPERATOR_WILDCARD ) == 0)
		            		rop = '*';
		            	else
		            		mode ^= 1;                
		    } else
			   mode ^= 1;
		    
		    if( DEBUG )
		        System.out.println("Relatrix IteratorFactoryTransaction Id:"+xid+" findSet setting mode "+String.valueOf(mode)+" for "+darg+" "+marg+" "+rarg);
			
		    switch(mode) {
               case 0:
                       return new FindSetMode0Transaction(xid, dop, mop, rop);
               case 1:
                       return new FindSetMode1Transaction(xid, dop, mop, rarg);
               case 2:
                       return new FindSetMode2Transaction(xid, dop, marg, rop);
               case 3:
                       return new FindSetMode3Transaction(xid, dop, marg, rarg);
               case 4:
                       return new FindSetMode4Transaction(xid, darg, mop, rop);
               case 5:
                       return new FindSetMode5Transaction(xid, darg, mop, rarg);
               case 6:
                       return new FindSetMode6Transaction(xid, darg, marg, rop);
               case 7:
                   	   return new FindSetMode7Transaction(xid, darg, marg, rarg);
        	    default:
                    throw new IllegalArgumentException("The findSet transaction factory mode "+mode+" is not supported.");
		    }
		}
	
		/**
		 * Create a factory generating headSet sets for the specified objects
		 * @param darg
		 * @param marg
		 * @param rarg
		 * @return
		 * @throws IOException 
		 * @throws IllegalArgumentException 
		 */
		public static IteratorFactory createHeadsetFactory(Object darg, Object marg, Object rarg) throws IllegalArgumentException, IOException {
		    byte mode = 0;
		    char dop, mop, rop;
			//
			dop = mop = rop = ' ';
			//
			if( (darg instanceof String) ) {
			          	// see if its user operator
			         	if( ((String)darg).compareTo( Relatrix.OPERATOR_TUPLE ) == 0 )
			         		dop = '?';
			            else
			            	if( ((String)darg).compareTo( Relatrix.OPERATOR_WILDCARD ) == 0)
			                     	dop = '*';
			                else
			                        mode = 4;                
			} else
			        mode = 4;
			if( (marg instanceof String) ) {
			         	// see if its user operator
			           	if( ((String)marg).compareTo( Relatrix.OPERATOR_TUPLE ) == 0 )
			           		mop = '?';
			            else
			            	if( ((String)marg).compareTo( Relatrix.OPERATOR_WILDCARD ) == 0)
			                      	mop = '*';
			                else
			                       	mode ^= 2;                
			} else
				   mode ^= 2;
			if( (rarg instanceof String) ) {
			            // see if its user operator
			        	if( ((String)rarg).compareTo( Relatrix.OPERATOR_TUPLE ) == 0 )
			        		rop = '?';
			            else
			            	if( ((String)rarg).compareTo( Relatrix.OPERATOR_WILDCARD ) == 0)
			            		rop = '*';
			            	else
			            		mode ^= 1;                
			} else
				   mode ^= 1;
			    
			if( DEBUG )
			        System.out.println("Relatrix IteratorFactory findHeadSet setting mode "+String.valueOf(mode)+" for "+darg+" "+marg+" "+rarg);
				
			switch(mode) {
	               case 0:
	           			throw new IllegalArgumentException("At least one argument to findHeadSet must contain an object reference");
	               case 1:
	                       return new FindHeadSetMode1(dop, mop, rarg);
	               case 2:
	                       return new FindHeadSetMode2(dop, marg, rop);
	               case 3:
	                       return new FindHeadSetMode3(dop, marg, rarg);
	               case 4:
	                       return new FindHeadSetMode4(darg, mop, rop);
	               case 5:
	                       return new FindHeadSetMode5(darg, mop, rarg);
	               case 6:
	                       return new FindHeadSetMode6(darg, marg, rop);
	               case 7:
	            	   	   return new FindHeadSetMode7(darg, marg, rarg);
	        	    default:
	                    throw new IllegalArgumentException("The findHeadset factory mode "+mode+" is not supported.");
			}
		}
		/**
		 * Create a factory generating transactional headSet sets for the specified objects
		 * @param xid Transaction Id
		 * @param darg
		 * @param marg
		 * @param rarg
		 * @return
		 * @throws IOException 
		 * @throws IllegalArgumentException 
		 */
		public static IteratorFactory createHeadsetFactory(String xid, Object darg, Object marg, Object rarg) throws IllegalArgumentException, IOException {
		    byte mode = 0;
		    char dop, mop, rop;
			//
			dop = mop = rop = ' ';
			//
			if( (darg instanceof String) ) {
			          	// see if its user operator
			         	if( ((String)darg).compareTo( Relatrix.OPERATOR_TUPLE ) == 0 )
			         		dop = '?';
			            else
			            	if( ((String)darg).compareTo( Relatrix.OPERATOR_WILDCARD ) == 0)
			                     	dop = '*';
			                else
			                        mode = 4;                
			} else
			        mode = 4;
			if( (marg instanceof String) ) {
			         	// see if its user operator
			           	if( ((String)marg).compareTo( Relatrix.OPERATOR_TUPLE ) == 0 )
			           		mop = '?';
			            else
			            	if( ((String)marg).compareTo( Relatrix.OPERATOR_WILDCARD ) == 0)
			                      	mop = '*';
			                else
			                       	mode ^= 2;                
			} else
				   mode ^= 2;
			if( (rarg instanceof String) ) {
			            // see if its user operator
			        	if( ((String)rarg).compareTo( Relatrix.OPERATOR_TUPLE ) == 0 )
			        		rop = '?';
			            else
			            	if( ((String)rarg).compareTo( Relatrix.OPERATOR_WILDCARD ) == 0)
			            		rop = '*';
			            	else
			            		mode ^= 1;                
			} else
				   mode ^= 1;
			    
			if( DEBUG )
			        System.out.println("Relatrix IteratorFactory findHeadSet setting mode "+String.valueOf(mode)+" for "+darg+" "+marg+" "+rarg);
				
			switch(mode) {
	               case 0:
	           			throw new IllegalArgumentException("At least one argument to findHeadSet must contain an object reference");
	               case 1:
	                       return new FindHeadSetMode1Transaction(xid, dop, mop, rarg);
	               case 2:
	                       return new FindHeadSetMode2Transaction(xid, dop, marg, rop);
	               case 3:
	                       return new FindHeadSetMode3Transaction(xid, dop, marg, rarg);
	               case 4:
	                       return new FindHeadSetMode4Transaction(xid, darg, mop, rop);
	               case 5:
	                       return new FindHeadSetMode5Transaction(xid, darg, mop, rarg);
	               case 6:
	                       return new FindHeadSetMode6Transaction(xid, darg, marg, rop);
	               case 7:
	            	   	   return new FindHeadSetMode7Transaction(xid, darg, marg, rarg);
	        	    default:
	                    throw new IllegalArgumentException("The findHeadset factory mode "+mode+" is not supported.");
			}
		}

		/**
		 * Create subset iterator factory in preparation for creating a subset iterator for a findSet operation.
		 * @param darg
		 * @param marg
		 * @param rarg
		 * @param endarg
		 * @return The IteratorFactory by which we may facilitate the creation of our RelatrixIterator
		 * @throws IllegalArgumentException
		 * @throws IOException
		 */
		public static IteratorFactory createSubsetFactory(Object darg, Object marg, Object rarg, Object... endarg) throws IllegalArgumentException, IOException {
		    byte mode = 0;
		    char dop, mop, rop;
			//
			dop = mop = rop = ' ';
			//
			if( (darg instanceof String) ) {
			          	// see if its user operator
			         	if( ((String)darg).compareTo( Relatrix.OPERATOR_TUPLE ) == 0 )
			         		dop = '?';
			            else
			            	if( ((String)darg).compareTo( Relatrix.OPERATOR_WILDCARD ) == 0)
			                     	dop = '*';
			                else
			                        mode = 4;                
			} else
			        mode = 4;
			if( (marg instanceof String) ) {
			         	// see if its user operator
			           	if( ((String)marg).compareTo( Relatrix.OPERATOR_TUPLE ) == 0 )
			           		mop = '?';
			            else
			            	if( ((String)marg).compareTo( Relatrix.OPERATOR_WILDCARD ) == 0)
			                      	mop = '*';
			                else
			                       	mode ^= 2;                
			} else
				   mode ^= 2;
			if( (rarg instanceof String) ) {
			            // see if its user operator
			        	if( ((String)rarg).compareTo( Relatrix.OPERATOR_TUPLE ) == 0 )
			        		rop = '?';
			            else
			            	if( ((String)rarg).compareTo( Relatrix.OPERATOR_WILDCARD ) == 0)
			            		rop = '*';
			            	else
			            		mode ^= 1;                
			} else
				   mode ^= 1;
			    
			if( DEBUG )
			        System.out.println("Relatrix IteratorFactory findSubSet setting mode "+String.valueOf(mode)+" for "+darg+" "+marg+" "+rarg);
				
			switch(mode) {
	               case 0:
	           			throw new IllegalArgumentException("At least one argument to findSubSet must contain an object reference");
	               case 1:
	                       return new FindSubSetMode1(dop, mop, rarg, endarg);
	               case 2:
	                       return new FindSubSetMode2(dop, marg, rop, endarg);
	               case 3:
	                       return new FindSubSetMode3(dop, marg, rarg, endarg);
	               case 4:
	                       return new FindSubSetMode4(darg, mop, rop, endarg);
	               case 5:
	                       return new FindSubSetMode5(darg, mop, rarg, endarg);
	               case 6:
	                       return new FindSubSetMode6(darg, marg, rop, endarg);
	               case 7:
	           			   return new FindSubSetMode7(darg, marg, marg, endarg);
	        	    default:
	                    throw new IllegalArgumentException("The findSubset factory mode "+mode+" is not supported.");
			}	
		}
		/**
		 * Create subset iterator factory in preparation for creating a subset iterator for a transactional findSet operation.
		 * @param xid Transaction Id
		 * @param darg
		 * @param marg
		 * @param rarg
		 * @param endarg
		 * @return The IteratorFactory by which we may facilitate the creation of our RelatrixIterator
		 * @throws IllegalArgumentException
		 * @throws IOException
		 */
		public static IteratorFactory createSubsetFactory(String xid, Object darg, Object marg, Object rarg, Object... endarg) throws IllegalArgumentException, IOException {
		    byte mode = 0;
		    char dop, mop, rop;
			//
			dop = mop = rop = ' ';
			//
			if( (darg instanceof String) ) {
			          	// see if its user operator
			         	if( ((String)darg).compareTo( Relatrix.OPERATOR_TUPLE ) == 0 )
			         		dop = '?';
			            else
			            	if( ((String)darg).compareTo( Relatrix.OPERATOR_WILDCARD ) == 0)
			                     	dop = '*';
			                else
			                        mode = 4;                
			} else
			        mode = 4;
			if( (marg instanceof String) ) {
			         	// see if its user operator
			           	if( ((String)marg).compareTo( Relatrix.OPERATOR_TUPLE ) == 0 )
			           		mop = '?';
			            else
			            	if( ((String)marg).compareTo( Relatrix.OPERATOR_WILDCARD ) == 0)
			                      	mop = '*';
			                else
			                       	mode ^= 2;                
			} else
				   mode ^= 2;
			if( (rarg instanceof String) ) {
			            // see if its user operator
			        	if( ((String)rarg).compareTo( Relatrix.OPERATOR_TUPLE ) == 0 )
			        		rop = '?';
			            else
			            	if( ((String)rarg).compareTo( Relatrix.OPERATOR_WILDCARD ) == 0)
			            		rop = '*';
			            	else
			            		mode ^= 1;                
			} else
				   mode ^= 1;
			    
			if( DEBUG )
			        System.out.println("Relatrix IteratorFactory findSubSet setting mode "+String.valueOf(mode)+" for "+darg+" "+marg+" "+rarg);
				
			switch(mode) {
	               case 0:
	           			throw new IllegalArgumentException("At least one argument to findSubSet must contain an object reference");
	               case 1:
	                       return new FindSubSetMode1Transaction(xid, dop, mop, rarg, endarg);
	               case 2:
	                       return new FindSubSetMode2Transaction(xid, dop, marg, rop, endarg);
	               case 3:
	                       return new FindSubSetMode3Transaction(xid, dop, marg, rarg, endarg);
	               case 4:
	                       return new FindSubSetMode4Transaction(xid, darg, mop, rop, endarg);
	               case 5:
	                       return new FindSubSetMode5Transaction(xid, darg, mop, rarg, endarg);
	               case 6:
	                       return new FindSubSetMode6Transaction(xid, darg, marg, rop, endarg);
	               case 7:
	           			   return new FindSubSetMode7Transaction(xid, darg, marg, marg, endarg);
	        	    default:
	                    throw new IllegalArgumentException("The findSubset factory mode "+mode+" is not supported.");
			}	
		}
		
	}