package org.gulnaz.wanteat.model;

import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author gulnaz
 */
@Entity
@Table(name = "votes")
public class Vote extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    @NotNull
    private User user;

    @Column(name = "date", columnDefinition = "timestamp default current_date")
    @NotNull
    private LocalDate date;

    public Vote() {
    }

    public Vote(Restaurant restaurant, User user) {
        this(null, restaurant, user);
    }

    public Vote(Integer id, Restaurant restaurant, User user) {
        this(id, restaurant, user, LocalDate.now());
    }

    public Vote(Integer id, Restaurant restaurant, User user, LocalDate date) {
        this.id = id;
        this.restaurant = restaurant;
        this.user = user;
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Vote{" +
               "id=" + id +
               ", date=" + date +
               '}';
    }
}
