package jdk8.bestpractices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionFactories {
    private static final Logger LOGGER = LoggerFactory.getLogger(stream.parallel.Application.class.getSimpleName());

    public static void main(String[] args) {
        listFactories();
        setFactories();
        mapFactories();
    }

    private static void listFactories() {
        List<String> friends = Arrays.asList("Raphael", "Olivia", "Thibaut");
        friends.set(0, "Richard");
        try {
            friends.remove("Thibaut");
        } catch (UnsupportedOperationException e) {
            LOGGER.info("Can't change length of list returned by Arrays.asList");
        }
        friends = List.of("Messi", "Ronaldo");
        try {
            friends.set(1, "Maradona");
        } catch (UnsupportedOperationException e) {
            LOGGER.info("Can't modify list returned by List.of");
        }

    }

    private static void setFactories() {
        try {
            Set.of("Messi", "Ronaldo", "Messi");
        } catch(IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private static void mapFactories() {
        // upto 10 keys, values
        Map<String, String> playersByTeam = Map.of("Argentina", "Messi", "Portugal", "Ronaldo");
        // for more than 10 pairs, use varargs based ofEntries
        playersByTeam = Map.ofEntries(Map.entry("Argentina", "Messi"), Map.entry("Portugal", "Ronaldo"));
    }
}
