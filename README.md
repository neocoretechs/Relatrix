The Relatrix:
Toward a Category Theoretic data management paradigm.
In order to provide structure to unstructured data, category theory provides us with a perfect description of how to add increasing
levels of structure through algebraic rules.
The Relatrix is a new data management system based on the branch of mathematics called 'Category Theory'. 
The Relatrix is a Java framework that manages unstructured data by the mapping of the functional relationships within the data sets. 
Relationships are defined as objects comprised of a ‘domain’ Java object, with a functional ‘mapping’ object, that relates the domain to a ‘range’ object.  
Data is retrieved using 'functors' that take categories to sets presented as standard Java Iterators. 
More plainly, imagine that rather than being able to map keys one-to-one like a relational database you can map them through a function that adds more relevance. 
We can then assign equivalence through isomorphism, or functional similarities between data. The relationships themselves are objects which can be composed into higher level relationships that
harness the power of Category theory to perform analysis not possible with conventional databases.
<br/><br/><i> Building relationships is as easy as saying:</i><br/>
Relatrix.store([fromObject],[mapObject],[toObject]); // This stores a functional relationship<br/>
<i>and a query for that set is as simple as:</i><br/>
Iterator iterator = Relatrix.findSet("?",[mapObject],"?"); // This retrieves all domain objects and range objects mapped through [mapObject]<br/>
<i>or to compose two relationships to an association:</i><br/>
Relatrix.store([fromObject1],[mapObject1],Relatrix.store([fromObject2],[mapObject2].[toObject2])); // This composes relationships<br/>
Iterator iterator = Relatrix.findSet([fromObject1],”*”,”?”); // This returns all range objects mapped to [fromObject1] through ANY map object including the relationship stored above<br/>
Iterator iterator = Relatrix.findSet(("*","*","*"); // This retrieves all relationships as identity objects<br/>


public class VisualCortex {<br/>
public static void main(String[] args) throws Exception {<br/>
<t/>	Relatrix.setTablespaceDirectory(args[0]);<br/>
<t/>	Iterator<?> iterator = Relatrix.findSet("?", "?", "?");<br/>
<t/>	int cnt = 0;<br/>
<t/>	while(iterator.hasNext()) {<br/>
<t/><t/>		Comparable[] c = (Comparable[])iterator.next();<br/>
<t/><t/>		for(int i = 0; i < c.length; i++) {<br/>
<t/><t/><t/>			System.out.println(cnt+" "+((Object)c[i]).toString());<br/>
<t/><t/><t/>		++cnt;<br/>
<t/><t/>		}<br/>
<t/>	}<br/>
<t/>	System.out.println("Count: "+cnt);<br/>
}
}
