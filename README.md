<h1>NEW! Compliance with Java 8 streams, lambdas, and functional programming paradigm!</h1>
<h1> A true Function Oriented Database integrated with the language!</h1>
The Relatrix:
<h4>Full featured Function Oriented Database, Key/Value Deep Store with transaction checkpointing and recovery, embedded and server modes!</h4>
Toward a Category Theoretic data management paradigm.
In order to provide structure to unstructured data, category theory provides us with a perfect description of how to add increasing
levels of structure through algebraic rules.
The Relatrix is a new data management system based on the branch of mathematics called 'Category Theory'. 
The Relatrix is a Java framework that manages unstructured data by the mapping of the functional relationships within the data sets. 
Relationships are defined as objects comprised of a â€˜domainâ€™ Java object, with a functional â€˜mappingâ€™ object, that relates the domain to a â€˜rangeâ€™ object.  
Data is retrieved using 'functors' that take categories to sets presented as standard Java Iterators. 
More plainly, imagine that rather than being able to map keys one-to-one like a relational database you can map them through a function that adds more relevance. 
We can then assign equivalence through isomorphism, or functional similarities between data. The relationships themselves are objects which can be composed into higher level relationships that
harness the power of Category theory to perform analysis not possible with conventional databases.
<br/><br/><i> Building relationships is as easy as saying:</i><br/>
Relatrix.store([fromObject],[mapObject],[toObject]); // This stores a functional relationship<br/>
<i>and a query for that set is as simple as:</i><p/>
Stream<Comparable[]> stream = (Stream<Comparable[]>) Relatrix.findStream("?", "?", "?");<br/>
stream.forEach(e -> Stream.of(e).forEach(g -> System.out.println("Element A:"+g)));<p/>
Or using the old Iterator model:<br/>
Iterator iterator = Relatrix.findSet("?",[mapObject],"?"); // This retrieves all domain objects and range objects mapped through [mapObject]<p/>
<i>To compose two relationships to an association:</i><br/>
Relatrix.store([fromObject1],[mapObject1],Relatrix.store([fromObject2],[mapObject2].[toObject2])); // This composes relationships<p/>
Stream<Comparable[]> stream = (Stream<Comparable[]>) Relatrix.findStream([fromObject1],â€�*â€�,â€�?â€�, true); // This returns all range objects mapped to [fromObject1] through ANY map object in parallel, including the relationship stored above<p/>
Stream<Comparable[]> stream = (Stream<Comparable[]>) Relatrix.findStream(("*","*","*"); // This makes ready for consumption by stream all relationships as identity objects<br/>


public class VisualCortex {<br/>
public static void main(String[] args) throws Exception {<br/>
&nbsp;&nbsp;&nbsp;Relatrix.setTablespaceDirectory(args[0]);<br/>
&nbsp;&nbsp;&nbsp;Stream<Comparable[]> stream = (Stream<Comparable[]>) Relatrix.findStream("?", "?", "?", true);<br/>
&nbsp;&nbsp;&nbsp;	
&nbsp;&nbsp;&nbsp;Map<Object, Map<Object, Map<Object, Long>>> nameCount = stream.collect(Collectors.groupingBy(b -> b[0].toString(),<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Collectors.groupingBy(d -> d[1].toString(),<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Collectors.groupingBy(e -> e[2].toString(), Collectors.counting()))));<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;nameCount.forEach((name, count) -> {<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println(name + ":" + count);<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; });<br/>
}<br/>
}
