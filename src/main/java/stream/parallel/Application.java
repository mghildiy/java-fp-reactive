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
        System.out.println("Total time seq:"+ (System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
        parallelProcessing();
        System.out.println("Total time parallel:"+ (System.currentTimeMillis() - startTime));
    }

    @Benchmark
    public static void sequentialProcessing() {
        LongStream.rangeClosed(1, 100000000)
                .reduce(0, (num1, num2) -> num1 + num2);
    }

    @Benchmark
    public static void parallelProcessing() {
        LongStream.rangeClosed(1, 100000000)
                .parallel()
                .reduce(0, (num1, num2) -> num1 + num2);
    }
}
