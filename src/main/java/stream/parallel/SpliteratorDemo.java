package stream.parallel;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SpliteratorDemo {

    public static void main(String[] args) {
        final String SENTENCE =
                " Nel mezzo del cammin di nostra vita " +
                        "mi ritrovai in una selva oscura" +
                        " ché la dritta via era smarrita ";
        System.out.println("Found imperatively " + countWordsImperatively(SENTENCE) + " words");

        Stream<Character> characters = IntStream.range(0, SENTENCE.length())
                        .mapToObj(SENTENCE::charAt);
        System.out.println("Found functionally and sequentially " + countWordsFunctionally(characters) + " words");

        Spliterator<Character> spliterator = new WordCounterSpliterator(SENTENCE);
        Stream<Character> parallelStream = StreamSupport.stream(spliterator, true);
        System.out.println("Found functionally and in parallel " + countWordsFunctionally(parallelStream) + " words");
    }

    private static int countWordsImperatively(String sentence) {
        int count = 0;
        boolean isLastCharacterASpace = true;
        for(char c : sentence.toCharArray()) {
            if(Character.isWhitespace(c))
                isLastCharacterASpace = true;
            else {
                if(isLastCharacterASpace)
                    ++count;
                isLastCharacterASpace = false;
            }
        }

        return count;
    }

    private static int countWordsFunctionally(Stream<Character> characters) {
        return  characters
                .reduce(new WordCounter(0, true), WordCounter::accumulate, WordCounter::combine)
                .count();
    }
}

record  WordCounter(int count, boolean isLastCharacterASpace) {

    public WordCounter accumulate(Character c) {
        if(Character.isWhitespace(c)) {
            return isLastCharacterASpace ? this : new WordCounter(count, true);
        } else {
            return isLastCharacterASpace ? new WordCounter(count+1, false) : this;
        }
    }

    public WordCounter combine(WordCounter wordCounter) {
        return new WordCounter(count + wordCounter.count, wordCounter.isLastCharacterASpace);
    }
}

class WordCounterSpliterator implements Spliterator<Character> {
    private final String toSplit;
    private int currentPosition = 0;

    public WordCounterSpliterator(String toSplit) {
        this.toSplit = toSplit;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Character> action) {
        action.accept(this.toSplit.charAt(currentPosition++));
        return currentPosition < this.toSplit.length();
    }

    @Override
    public Spliterator<Character> trySplit() {
        int remainingSize = this.toSplit.length() - this.currentPosition;
        if(remainingSize < 10) return null;

        for(int splitPos = this.currentPosition + remainingSize/2; splitPos < this.toSplit.length(); splitPos++) {
            if(Character.isWhitespace(this.toSplit.charAt(splitPos))) {
                Spliterator<Character> wordCounterSpliterator =
                        new WordCounterSpliterator(this.toSplit.substring(this.currentPosition, splitPos));
                this.currentPosition = splitPos;

                return wordCounterSpliterator;
            }

            return null;
        }
        return null;
    }

    @Override
    public long estimateSize() {
        return this.toSplit.length() - this.currentPosition;
    }

    @Override
    public int characteristics() {
        return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
    }
}
