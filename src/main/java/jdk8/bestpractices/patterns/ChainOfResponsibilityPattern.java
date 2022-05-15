package jdk8.bestpractices.patterns;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class ChainOfResponsibilityPattern {

    public static void main(String[] args) {
        // multiply two numbers
        BinaryOperator<Integer> product = (Integer x, Integer y) -> x * y;
        // Square a number
        UnaryOperator<Integer> square = (Integer x) -> x * x;
        // combine lambadas to make a pipeline
        BiFunction<Integer, Integer, Integer> pipeline = product.andThen(square);
        System.out.println(pipeline.apply(4, 5));
    }
}
