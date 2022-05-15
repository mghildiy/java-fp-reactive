package jdk8.bestpractices.patterns;

import java.util.function.Consumer;

public class TemplateDesignPattern {

    public static void main(String[] args) {
        Consumer<String> intermediate = input -> System.out.println(input);
        new MyBasicAlgorithm().execute("intermediate", intermediate);
    }
}

class MyBasicAlgorithm {
    public void execute(String input, Consumer<String> intermediate) {
        start();
        intermediate.accept(input);
        end();
    }

    private void start() {
        System.out.println("Start");
    }

    private void end() {
        System.out.println("End");
    }
}
