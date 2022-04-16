package model;

public record Calories(int calories) {
    public Calories(int calories) {
        assert calories > 0 : "Can't be less than 0";

        this.calories = calories;
    }
}
