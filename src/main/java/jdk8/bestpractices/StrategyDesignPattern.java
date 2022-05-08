package jdk8.bestpractices;

public class StrategyDesignPattern {

    public static void main(String[] args) {
        Validator numericValidator =
                new Validator((String s) -> s.matches("[0-9]+"));
        System.out.println(numericValidator.validate("12745653"));
        Validator lowerCaseValidator =
                new Validator((String s) -> s.matches("[a-z]+"));
        System.out.println(lowerCaseValidator.validate("wdawdw3434"));
    }
}

interface ValidationStrategy {
    boolean execute(String s);
}
class Validator {
    private final ValidationStrategy strategy;
    public Validator(ValidationStrategy v) {
        this.strategy = v;
    }
    public boolean validate(String s) {
        return strategy.execute(s);
    }
}
