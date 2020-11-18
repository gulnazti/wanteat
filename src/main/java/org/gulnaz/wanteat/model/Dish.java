package org.gulnaz.wanteat.model;

import java.math.BigDecimal;

/**
 * @author gulnaz
 */
public class Dish extends AbstractNamedEntity {

    private BigDecimal price;

    public Dish() {
    }

    public Dish(Integer id, String name, BigDecimal price) {
        super(id, name);
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
