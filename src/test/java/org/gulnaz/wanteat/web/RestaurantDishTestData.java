package org.gulnaz.wanteat.web;

import java.time.LocalDate;
import java.util.List;

import org.gulnaz.wanteat.model.Dish;
import org.gulnaz.wanteat.model.Restaurant;
import org.gulnaz.wanteat.model.Vote;
import org.gulnaz.wanteat.to.RestaurantTo;
import org.gulnaz.wanteat.util.RestaurantUtil;

import static org.gulnaz.wanteat.model.AbstractBaseEntity.START_SEQ;
import static org.gulnaz.wanteat.web.UserTestData.admin;
import static org.gulnaz.wanteat.web.UserTestData.dev;

/**
 * @author gulnaz
 */
public class RestaurantDishTestData {
    public static TestMatcher<Restaurant> RESTAURANT_MATCHER = TestMatcher.usingEqualsComparator(Restaurant.class);
    public static TestMatcher<RestaurantTo> RESTAURANT_TO_MATCHER = TestMatcher.usingEqualsComparator(RestaurantTo.class);
    public static TestMatcher<Dish> DISH_MATCHER = TestMatcher.usingEqualsComparator(Dish.class);
    public static TestMatcher<Vote> VOTE_MATCHER = TestMatcher.usingEqualsComparator(Vote.class);

    public static final int NOT_FOUND = 10;
    public static final int RESTAURANT1_ID = START_SEQ + 3;
    public static final int DISH1_ID = START_SEQ + 7;
    public static final int VOTE1_ID = START_SEQ + 16;

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "Claude Monet", "Kazan, Peterburgskaya st., 5");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT1_ID + 1, "Voyage", "Kazan, Baumana st., 17");
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT1_ID + 2, "Java Coffee", "Kazan, Pushkina st., 5");
    public static final Restaurant restaurant4 = new Restaurant(RESTAURANT1_ID + 3, "Marinad", "Kazan, Universitetskaya st., 22");

    public static final List<Restaurant> restaurants = List.of(restaurant1, restaurant3, restaurant4, restaurant2);

    public static final Dish dish1 = new Dish(DISH1_ID, "Boeuf Bourguignon", 700);
    public static final Dish dish2 = new Dish(DISH1_ID + 1, "Blanquette de Veau", 850);
    public static final Dish dish3 = new Dish(DISH1_ID + 2, "Cassoulet", 450);
    public static final Dish dish4 = new Dish(DISH1_ID + 3, "Confit de Canard", 530);
    public static final Dish dish5 = new Dish(DISH1_ID + 4, "Spring Dish", 260);
    public static final Dish dish6 = new Dish(DISH1_ID + 5, "Beans with Salad", 310);
    public static final Dish dish7 = new Dish(DISH1_ID + 6, "Tomatoes", 180);
    public static final Dish dish8 = new Dish(DISH1_ID + 7, "Cucumber Roll", 200);

    public static final List<Dish> dishes = List.of(dish8, dish7, dish6, dish5, dish4, dish3, dish2, dish1);

    public static final Vote vote1 = new Vote(VOTE1_ID, restaurant3, dev, LocalDate.of(2020, 12, 24));
    public static final Vote vote2 = new Vote(VOTE1_ID + 1, restaurant3, dev);

    public static Restaurant getRestaurant1WithMenu() {
        return getRestaurantWithMenu(restaurant1, dish2, dish1);
    }

    public static List<RestaurantTo> getRestaurantsWithMenu() {
        Restaurant copy1 = getRestaurant1WithMenu();
        Restaurant copy2 = getRestaurantWithMenu(restaurant2, dish3, dish4);
        Restaurant copy3 = getRestaurantWithMenu(restaurant3, dish6, dish5);
        Restaurant copy4 = getRestaurantWithMenu(restaurant4, dish8, dish7);
        return RestaurantUtil.getTos(List.of(copy1, copy2, copy3, copy4),
            List.of(vote2, new Vote(restaurant3, admin)));
    }

    public static Restaurant getRestaurantWithMenu(Restaurant restaurant, Dish... dishes) {
        Restaurant copy = new Restaurant(restaurant);
        copy.setMenu(List.of(dishes));
        return copy;
    }

    public static Restaurant getNewRestaurant() {
        return new Restaurant(null, "New", "Address");
    }

    public static Restaurant getUpdatedRestaurant() {
        return new Restaurant(RESTAURANT1_ID, "Updated Name", "New Address");
    }

    public static Dish getNewDish() {
        return new Dish(null, "New", 100);
    }

    public static Dish getUpdatedDish() {
        return new Dish(DISH1_ID, "Updated Name", 200);
    }
}
