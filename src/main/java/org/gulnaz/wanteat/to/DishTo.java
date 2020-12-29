package org.gulnaz.wanteat.to;

import java.beans.ConstructorProperties;
import java.util.Objects;

/**
 * @author gulnaz
 */
public class DishTo extends BaseTo {

    private String name;

    private long price;

    @ConstructorProperties({"id", "name", "price"})
    public DishTo(Integer id, String name, long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DishTo that = (DishTo) o;
        return price == that.price &&
               Objects.equals(id, that.id) &&
               Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }

    @Override
    public String toString() {
        return "DishTo{" +
               "name='" + name + '\'' +
               ", price=" + price +
               ", id=" + id +
               '}';
    }
}
