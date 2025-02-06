package com.neocoretechs.relatrix.iterator;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.rocksack.Alias;

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
	    protected static char dop, mop, rop;
		
		/**
		 * Create the iterator. Factory method, abstract.
		 * Instantiates the proper subclass of {@link Morphism}
		 * to pass to the createRelatrixIterator method to act as a retrieval template for the proper sequence
		 * of 'findSet' operators for each permutation.
		 * @return RelatrixIterator subclass that returns {@link com.neocoretechs.relatrix.Result} tuples/morphisms
		 * @throws IllegalAccessException
		 * @throws IOException
		 */
		public abstract Iterator<?> createIterator() throws IllegalAccessException, IOException;
		
		/**
		* Create the iterator. Factory method, abstract. Instantiates the proper subclass of {@link Morphism}
		* to pass to the createRelatrixIterator method to act as a retrieval template for the proper sequence
		* of 'findSet' operators for each permutation.
		* @param alias the database alias
		* @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
		* @throws IllegalAccessException
		* @throws IOException
		* @throws NoSuchElementException if the alias is not found
		*/
		public abstract Iterator<?> createIterator(Alias alias) throws IllegalAccessException, IOException, NoSuchElementException;
		
		/**
		 * Create the iterator. Factory method, abstract, subclass. Allows subclasses to create specific types of RelatrixIterator
		 * @param tdmr the Morphism template that defines the selection parameters for the iterator, created by createIterator
		 * @return RelatrixIterator subclass that returns {@link com.neocoretechs.relatrix.Result} tuples/morphisms
		 * @throws IllegalAccessException
		 * @throws IOException
		 */
		protected abstract Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException;
		
		/**
		 * Create the iterator. Factory method, abstract, subclass. Allows subclasses to create specific types of RelatrixIterator
		 * @param alias the database alias
		 * @param tdmr the Morphism template that defines the selection parameters for the iterator
		 * @return RelatrixIterator subclass that returns {@link com.neocoretechs.relatrix.Result} tuples/morphisms
		 * @throws IllegalAccessException
		 * @throws IOException
		 */
		protected abstract Iterator<?> createRelatrixIterator(Alias alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException;
		
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
		 * Determine if we are returning identity relationship {@link Morphism}s. {@link Relatrix.OPERATOR_WILDCARD_CHAR} 
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
		 * Determine if we are returning singleton relationship; i.e. if we have specified all object operators in our
		 * parameters. (<object>,<object>,<object>)
		 * @param dmr_return  For each element of the dmr_return array elements 1-3, 0 means object, 1 means its a return tuple ?, 2 means its a wildcard *
		 * @return true if all 1-3 values of dmr_return are 0 indicating all instances of objects specified for elements of a relationship, resulting in identity {@link Morphism}
		 */
		protected static boolean isReturnRelationships(short[] dmr_return) {
			return( dmr_return[1] == 0 && dmr_return[2] == 0 && dmr_return[3] == 0 );
		}
		
		
}