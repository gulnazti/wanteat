package org.gulnaz.wanteat.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.gulnaz.wanteat.model.Restaurant;
import org.gulnaz.wanteat.model.Vote;
import org.gulnaz.wanteat.to.RestaurantTo;

/**
 * @author gulnaz
 */
public class RestaurantUtil {

    private RestaurantUtil() {
    }

    private static RestaurantTo createTo(Restaurant restaurant, int todayVotes) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getMenu(), todayVotes);
    }

    public static List<RestaurantTo> getTos(List<Restaurant> restaurants, List<Vote> allTodayVotes) {
        Map<Integer, Integer> results = new HashMap<>();
        allTodayVotes.forEach(vote -> results.merge(vote.getRestaurant().getId(), 1, Integer::sum));
        return restaurants.stream()
            .map(restaurant -> createTo(restaurant, results.getOrDefault(restaurant.getId(), 0)))
            .sorted(Comparator.comparing(RestaurantTo::getTodayVotes).reversed()
                .thenComparing(RestaurantTo::getName))
            .collect(Collectors.toList());
    }
}
