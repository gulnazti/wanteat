package org.gulnaz.wanteat.model;

import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.gulnaz.wanteat.View;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author gulnaz
 */
@Entity
@Table(name = "dishes")
public class Dish extends AbstractNamedEntity {

    @NotNull
    @Range(min = 1, max = 100000)
    @Column(name = "price", nullable = false)
    private long price;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created", nullable = false, columnDefinition = "timestamp default current_date")
    private LocalDate created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    @NotNull(groups = View.Persist.class)
    private Restaurant restaurant;

    public Dish() {
    }

    public Dish(String name, long price) {
        this(null, name, price);
    }

    public Dish(Integer id, String name, long price) {
        this(id, name, price, LocalDate.now());
    }

    public Dish(Integer id, String name, long price, LocalDate created) {
        super(id, name);
        this.price = price;
        this.created = created;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
