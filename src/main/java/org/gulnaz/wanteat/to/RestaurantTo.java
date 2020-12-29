package org.gulnaz.wanteat.to;

import java.beans.ConstructorProperties;
import java.util.List;
import java.util.Objects;

/**
 * @author gulnaz
 */
public class RestaurantTo extends BaseTo {

    private final String name;

    private final List<DishTo> menu;

    private final int todayVotes;

    @ConstructorProperties({"id", "name", "menu", "todayVotes"})
    public RestaurantTo(Integer id, String name, List<DishTo> menu, int todayVotes) {
        this.id = id;
        this.name = name;
        this.menu = menu;
        this.todayVotes = todayVotes;
    }

    public String getName() {
        return name;
    }

    public List<DishTo> getMenu() {
        return menu;
    }

    public int getTodayVotes() {
        return todayVotes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantTo that = (RestaurantTo) o;
        return todayVotes == that.todayVotes &&
               Objects.equals(id, that.id) &&
               Objects.equals(name, that.name) &&
               Objects.equals(menu, that.menu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, menu, todayVotes);
    }

    @Override
    public String toString() {
        return "RestaurantTo{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", menu=" + menu +
               ", votesToday=" + todayVotes +
               '}';
    }
}
