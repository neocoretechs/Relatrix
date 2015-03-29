package com.neocoretechs.relatrix.iterator;
import java.io.IOException;
import java.util.Iterator;
import com.neocoretechs.relatrix.Relatrix;

	/**
	 * Abstract factory pattern to create the proper iterator
	 * @author jg
	 *
	 */
	public abstract class IteratorFactory
	{
		private static boolean DEBUG = true; 
		public abstract Iterator<?> createIterator() throws IllegalAccessException, IOException;
	
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
		* Check operator for Relatrix Findset, determine legality return corresponding value for our dmr_return structure
		* @param marg the char operator
		* @return the translated ordinal
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
		
	}