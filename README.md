# Java lambdas, streams, functional programming and reactive programming

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

### Stream API methods
* filter : take all elements which satisfy a predicate.
* distinct : take unique elements.
* takeWhile : take all elements before first element which doesn't satisfy predicate. Use instead of filter when we don't want to iterate entire stream, like in a sorted stream
* dropWhile : complement of takeWhile.
* limit : take first n elements
* skip : skip first n elements
* map : apply a function to every element of a stream to get a new stream
* flatMap : apply a function which generates a stream from an input element, and then combine elements from those streams to generate a single stream
* reduce

### Parallel streams
We can convert a stream to a parallel stream by calling method 'parallel'. It just enables a flag. Similarly, a parallel stream
can be made sequential by calling method 'sequential', and is also just sets a flag. So if we use these multiple times, then
only last invocation matters.
By default, ForkJoinPool from fork/join framework is used and it introduces
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
* Avoid operations which involve automatic boxing and unboxing. Prefer IntStream, DoubleStream,LongStream etc. instead
* Some operations, like limit, findFirst,Iterate etc., depend on order of elements, and hence they are not fit for parallelization
* Parallel streams are beneficial if either there are large number of elements OR total time taken to process each element through all the stages is large
* It also matters how easy it is to decompose underlying data-structure. For eg, it's much easier to split ArrayList than a LinkedList
* Terminal operation also impacts the performance of parallel streams depending on how cheap or expensive is the merging terminal operation

#### fork/join framework
Parallel streams internally use Java 7's fork/join mechanism to split tasks and parallelize them.
This framework is designed to recursively split a task into smaller subtasks and then assign them to worker threads from a thread pool called
ForkJoinPool which is an implementation of ExecutorService interface.

A task is defined by implementing RecursiveTask<R>(inherits from ForkJoinTask) where R is the result type of the computation of a task(or by implementing RecursiveAction
when task returns no result). Task needs to implement compute method:

`protected abstract R compute();`

This method contains the logic for dividing the task further into subtasks as well as computing the result.
We launch a task either by:
* Passing a task to ForkJoinPool.invoke
* Invoking _**fork**_ on a task to schedule it on ForkJoinPool

Few things to take care of when using forkjoin framework:
* ForkJoinPool.invoke must never be called from within a task itself, but from a client code
* We invoke _**join**_ on a task to block the caller, so it is needed that we call it after computations of all the subtasks
  has been started, else other subtasks would be made to wait for execution
* It may seem natural to invoke fork on all the subtasks, but a better approach is to call compute on one of the subtask
  as it means it would be executed on same thread and hence avoids overhead associated with allocating task to forkjoin pool
* Debugging parallel computation using forkjoin framework is difficult
* Like parallel streams, forkjoin framework doesn't always guarantee better performance than sequential,
  and we should keep in mind to: always benchmark our code, task must be decomposable into independent subtasks, 
  and our subtasks must take longer than time required to fork a subtask to be considered a candidate for parallelization,
  sequential computation may have the advantage of optimizations from compiler

#### Work stealing
Its very important that work is uniformly distributed among threads in forkjoin pool. In real world scenarios, task splitting
is not very straightforward to achieve this.
To ensure that all threads in forkjoin pool are equally busy, it uses a mechanism called _**work stealing**_.
Every thread in forkjoin pool uses a doubly linked queue to store tasks. So if a thread runs out of its tasks, it randomly
selects a thread and picks a task from its end. So a good practise is to have finegrained subtasks rather than few big tasks.

#### Spliterator
Parallel streams use forkjoin pool to execute tasks in parallel, and use Spliterator to split the stream into chunks.
Spliterator is Java8 interface, and it stands for 'splitable iterator', and like iterators they are used to traverse the
elements of a source, but they are also designed to do this in parallel. Java8 provides a default Spliterator implementation
for all datastructures in Collections framework which can be accessed using default method spliterator().
Spliterator offers following API:
* tryAdvance: sequentially consumes elements  like normal iterator
* trySplit: partitions off elements in current Spliterator to another Spliterator
* estimateSize: returns an estimate of elements remaining to be consumed
* characteristics: returns an int encoding of the set of Spliterator's characteristics which can be used to optimize and better control

Parallel stream uses Spliterator's trySplit method to recursively split the stream till it returns null.

#### Implementing Custom Spliterator

`SpliteratorDemo.java`

#### Collection factories
- Arrays.asList
- List.of
- Set.of
- Map.of
- Map.ofEntries

#### removeIf, replaceAll
Stream API methods generate new stream, while iterator based approach is error-prone if we want to modify existing collections.
From Java 8, we can use following methods from List and Set interface:
- removeIf
- replaceAll

#### Working with Map
- forEach for iteration
- sorting using Entry.comparingByValue, Entry.comparingByKey
- getOrDefault to overcome null reference issues
- computeIfAbsent, computeIfPresent, compute

#### Design patterns using lambdas
- Use lambdas to define strategies instead of defining explicit classes in strategy pattern implementation @`StrategyDesignPattern`
- Use lambdas to plugin the steps in algorithm defined by template @`TemplateDesignPattern`
- Use lambdas to define observers in observer pattern
- 924