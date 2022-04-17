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

###Stream API methods
* filter : take all elements which satisfy a predicate.
* distinct : take unique elements.
* takeWhile : take all elements before first element which doesn't satisfy predicate. Use instead of filter when we don't want to iterate entire stream, like in a sorted stream
* dropWhile : complement of takeWhile.
* limit : take first n elements
* skip : skip first n elements
* map : apply a function to every element of a stream to get a new stream
* flatMap : apply a function which generates a stream from an input element, and then combine elements from those streams to generate a single stream
* reduce

###Parallel streams
We can convert a stream to a parallel stream by calling method 'parallel'. It just enables a flag. Similarly, a parallel stream
can be made sequential by calling method 'sequential', and is also just sets a flag. So if we use these multiple times, then
only last invocation matters.
By default ForkJoinPool from fork/join framework is used and it introduces
number of threads same as number of processors. But we can change the size of this pool using the system property
java.util .concurrent.ForkJoinPool.common.parallelism

`System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism","8");`

This is a global setting, so it will affect all the parallel streams in the code. Conversely, it currently isn’t possible
to specify this value for a single parallel stream. In general, having the size of the ForkJoinPool equal to the number 
of processors on machine is a meaningful default.

But not all stream operations are parallelizable. Attempt to parallelize such operations can be counterproductive and 
worsen the performance. Also, there must be no shared mutable state, else results are unexpected.

Points to take into account while using parallel streams:

* Always try to measure the performance while making transition form sequential to parallel stream processing
* Avoid operations which involve automatic boxing and unboxing. Prefer IntStream, DoubleStream,LongStream etc instead
* Some operations, like limit, findFirst,Iterate etc., depend on order of elements, and hence they are not fit for parallelization
* Parallel streams are beneficial if either there are large number of elements OR total time taken to process each element through all the stages is large
* It also matters how easily it is to decompose underlying data-structure. For eg, its easier to split ArrayList than a LinkedList
* Terminal operation also impacts the performance of parallel streams depending on how cheap or expensive is the merging terminal operation