package stream.parallel;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(jvm = "2", jvmArgs={"-Xms4G", "-Xmx4G"})
public class Application {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        sequentialProcessing();
        System.out.println("Total time:"+ (System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
        parallelProcessing();
        System.out.println("Total time:"+ (System.currentTimeMillis() - startTime));
    }

    @Benchmark
    public static void sequentialProcessing() {
        LongStream.range(1, 1000000000L)
                .reduce(0, (num1, num2) -> num1 + num2);
    }

    @Benchmark
    public static void parallelProcessing() {
        LongStream.range(1, 1000000000L)
                .parallel()
                .reduce(0, (num1, num2) -> num1 + num2);
    }
}
