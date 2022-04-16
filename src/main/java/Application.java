import model.Calories;
import model.Dish;
import model.Menu;
import model.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Application {

    public static void main(String[] args) {
        Menu menu = new Menu(Arrays.asList(
                new Dish("pork", false, new Calories(800), Type.MEAT),
                new Dish("beef", false, new Calories(700), Type.MEAT),
                new Dish("chicken", false, new Calories(400), Type.MEAT),
                new Dish("french fries", true, new Calories(530), Type.OTHER),
                new Dish("rice", true, new Calories(350), Type.OTHER),
                new Dish("season fruit", true, new Calories(120), Type.OTHER),
                new Dish("pizza", true, new Calories(550), Type.OTHER),
                new Dish("prawns", false, new Calories(300), Type.FISH),
                new Dish("salmon", false, new Calories(450), Type.FISH) ));

        List<String> highCaloriesDishes = menu.dishes().stream()
                .filter(Application::highCalorieDish)
                .map(dish -> {
                    System.out.println("Map operation:"+ dish.name());
                    return dish.name();
                })
                .limit(3)
                .collect(toList());
        System.out.println("First three high calorie dishes:"+highCaloriesDishes);

        // A stream can only be consumed once, like Iterator
        List<String> title = Arrays.asList("USA", "India", "Germany", "UK");
        Stream<String> s = title.stream();
        s.forEach(System.out::println);
        try {
            s.forEach(System.out::println);
        } catch(Exception e) {
            // consuming a stream second time throws an exception
            System.out.println(e.getMessage());
        }

        // Collection needs explicit iteration by developer
        List<String> dishNames = new ArrayList<>();
        // for-each construct is a syntactic sugar for iterator based approach
        for(Dish dish : menu.dishes()) {
            dishNames.add(dish.name());
        }
        dishNames = new ArrayList<>();
        Iterator<Dish> itr = menu.dishes().iterator();
        while(itr.hasNext()) {
            dishNames.add(itr.next().name());
        }
        // Stream performs internal iteration
        dishNames = menu.dishes().stream()
                .map(Dish::name)
                .collect(toList());
    }

    static private boolean highCalorieDish(Dish dish) {
        System.out.println("Filter operation:"+ dish.name());
        return dish.calories().calories() > 300;
    }
}
