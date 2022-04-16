#Java lambdas, streams, functional programming and reactive programming

The Streams API in Java lets write code in:
* _**Declarative**_—More concise and readable
* _**Composable**_—Greater flexibility
* _**Parallelizable**_—Better performance

A Stream is a sequence of elements from a source and supports data-processing operations. It basically provides
an interface to perform computations/data operations on a sequenced set of values. It sources its elements from Collections,
IO, arrays etc. Order of the values is maintained same as the source. Data operations available are like map, filter, sort,
find, match, reduce etc., and can be executed sequentially or in parallel. Output of an operation over a stream is another stream,
and it enables pipelining.

A stream is consumed by applying a terminal operation on it, like forEach, collect etc. A stream can only be consumed once.
With Collection interface, developer needs to iterate the collections explicitly, called external iteration.
With streams, iteration is taken care of implicitly, called internal iteration. This allows for all sort of optimizations under the hood,
which is just not possible is things are done explicitly by developers.

Stream API operations are categorized as: _**intermediate**_ operations and _**terminal**_ operations.
Intermediate operations return another stream, and they are _**lazy**_, meaning they don't process anything until a terminal
operation is invoked on stream pipeline. Laziness allows merging of individual intermediate operations into same pass by terminal operation.
Terminal operations produce a result from a stream pipeline. Result can be a List, Integer or even void.
So a stream basically has a data-source to perform query on, a set of intermediate operations to configure a pipeline, 
and finally a terminal operation which executes the pipeline and generates some result or perform some side effect returning void.
