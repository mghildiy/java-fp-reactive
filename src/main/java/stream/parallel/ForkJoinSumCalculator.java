package stream.parallel;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

public class ForkJoinSumCalculator extends RecursiveTask<Long> {
    private static final ForkJoinPool FORKJOINPOOL = new ForkJoinPool();
    private final long[] numbers;
    private final int start;
    private final int end;
    public static final long THRESHOLD = 10_000;

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        int numLoops = 40;
        long[] numbers = LongStream.rangeClosed(1, 100000000).toArray();
        for(int i = 1; i <= numLoops; i++) {
            ForkJoinSumCalculator forkJoinSumCalculator = new ForkJoinSumCalculator(numbers);
            FORKJOINPOOL.invoke(forkJoinSumCalculator);
        }
        System.out.println("Total time parallel:"+ (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        for(int i = 1; i <= numLoops ; i++) {
            long seqSum = 0L;
            for(int j = 0; j < 100000000 ; j++) {
                seqSum += numbers[j];
            }
        }
        System.out.println("Total time sequential:"+ (System.currentTimeMillis() - startTime));
    }

    public ForkJoinSumCalculator(long[] numbers) {
        this(numbers, 0, numbers.length);
    }
    private ForkJoinSumCalculator(long[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }
    @Override
    protected Long compute() {
        int length = end - start;
        if (length <= THRESHOLD) {
            return computeSequentially();
        }
        ForkJoinSumCalculator leftTask =
                new ForkJoinSumCalculator(numbers, start, start + length/2);
        leftTask.fork();
        ForkJoinSumCalculator rightTask =
                new ForkJoinSumCalculator(numbers, start + length/2, end);
        Long rightResult = rightTask.compute();
        Long leftResult = leftTask.join();
        return leftResult + rightResult;
    }

    private long computeSequentially() {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += numbers[i];
        }
        return sum;
    }
}
