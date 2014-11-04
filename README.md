The Relatrix:
Toward a Category Theoretic data management paradigm.
In order to provide structure to unstructured data, category theory provides us with a perfect description of how to add increasing
levels of structure through algebraic rules.
The Relatrix is the worlds first Functor Object Oriented Database Management System (FOODBMS).
Using the theoretical underpinnings of a branch of mathematics called 'Category Theory', The Relatrix uses the morphism identity to allow 
random Key/Value collections to be overlayed with semantic information stored as relationships. Unstructured data can be related 
with any conceivable "has", "is a" or arbitrary mapping of relationships. Data is retrieved using 'Forgetful functors' that create new group homomorphisms 
presented as standard Java Iterators. More plainly, imagine that rather than being able to map keys one-to-one like a relational database you can map 
them through a function that adds more relevance. Once this relationship is created, many set operations can easily be performed. 
Using algebraic rules such as association, commutation, and distribution, and using the concept of composition, category theory constructs 
like the 'Natural Transformation' functions as a way to impart structure to data and these data can be processed as one does a relational join,
but with far more inherent power. 
Any Key/Value store can be overlayed with The Relatrix but it uses The Java Key/Value deep store BigSack 
(another NeoCoreTechs project) as the primary durable storage mechanism. Building relationships is as easy as saying:

Relatrix.store([fromObject],"relates to",[toObject]);
and a query for that set is as simple as:
Iterator iterator = Relatrix.findSet("?","relates to","?");

and each iteration returning an array of Comparable[] keys of arity "?"=2 for your 2 "?" parameters in the findSet call.
The previous is just one extremely simplified example, there are a multitude of options for returning new sets using variations of the same simple 
query method calls.

Object relationObject = Relatrix.store([fromObject],"relates to",[toObject]);
Relatrix.Store([relationObject],"composes with",[anotherObject]
and a query for that set is as simple as:
Iterator iterator = Relatrix.findSet("?","relates to","?");
which, using the above iterator, would produce:
[fromObject] [toObject] "composes with" [anotherObject]