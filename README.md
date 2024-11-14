<h1>NEW! Compliance with Java 8 streams, lambdas, and functional programming paradigm!</h1>
<h1> A newer, easier way of looking at and manipulating data.</h1>
The Relatrix:
<h4>Full featured Database, Builds on the Key/Value Deep Store concept with transaction checkpointing and recovery, embedded and server modes. </h4>
Toward an easier, more flexible, data management paradigm.
In order to provide structure to unstructured data, an otherwise esoteric branch of math called category theory provides us with a 
perfect description of how to add increasing levels of structure through its algebraic rules.
The Relatrix is a new data management system whose underlying principles are loosely based on this branch of mathematics. 
The Relatrix is a framework that manages unstructured data by the mapping of the functional relationships within the data sets.
These relationships can be simple, standard key/value relationships, or a more complex triplet that contains three objects. The objects in the triplets
can themselves be relationships that can be composed into more complex relationships. 
Relationships are defined as objects comprised of an overarching domain object, with a functional mapping object, that relates the domain to a range object.  
Data is retrieved as sets presented as standard iterators or streams. There are a number of simple yet powerful sets of retrieval methods
utilizing wildcards and concrete objects and class and object level set return indicators to get the data back out without getting lost ina deep, dark, thickets common
to schemaless data stores.
To get slightly more technical; imagine that rather than being able to map keys one-to-one like a relational database you can map them through a function that adds more relevance. 
We can then assign equivalence through isomorphism, or functional similarities between data. The relationships themselves are objects which can be composed into higher level relationships that
open up the data to perform analysis not possible with conventional databases. Here are some concrete examples:
<br/><br/><i> Building relationships is as easy as saying:</i><br/>
<code>
Relatrix.store([fromObject],[mapObject],[toObject]); // This stores a functional relationship<br/>
</code>
<i>and a query for that set is as simple as:</i><p/>
<code>
Stream<Result> stream = (Stream<Result>) Relatrix.findStream("?", "?", "?");<br/>
stream.forEach(e -> Stream.of(e).forEach(g -> System.out.println("Element A:"+g)));<p/>
</code>
Or using the old Iterator model:<br/>
<code>
Iterator iterator = Relatrix.findSet("?",[mapObject],"?"); // This retrieves all domain objects and range objects mapped through [mapObject]<p/>
</code>
<i>To compose two relationships to an association:</i><br/>
<code>
Relatrix.store([fromObject1],[mapObject1],Relatrix.store([fromObject2],[mapObject2].[toObject2])); // This composes relationships<p/>
Stream<Result> stream = (Stream<Result>) Relatrix.findStream([fromObject1],"*","?", true); // This returns all range objects mapped to [fromObject1] through ANY map object in parallel, including the relationship stored above<p/>
Stream<Result> stream = (Stream<Result>) Relatrix.findStream(("*","*","*"); // This makes ready for consumption by stream all relationships as identity objects<br/>
</code>

```
public class VisualCortex {
	public static void main(String[] args) throws Exception {
		Relatrix.setTablespaceDirectory(args[0]);
		Stream<Result> stream = (Stream<Result>) Relatrix.findStream("?", "?", "?", true);
		Map<Object, Map<Object, Map<Object, Long>>> nameCount = stream.collect(Collectors.groupingBy(b -> b[0].toString(),
		Collectors.groupingBy(d -> d[1].toString(),
		Collectors.groupingBy(e -> e[2].toString(), Collectors.counting()))));
		nameCount.forEach((name, count) -> {
			System.out.println(name + ":" + count);
		});
	}
}
```
