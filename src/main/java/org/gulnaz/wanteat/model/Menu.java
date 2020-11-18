package org.gulnaz.wanteat.model;

import java.time.LocalDate;
import java.util.List;

/**
 * @author gulnaz
 */
public class Menu extends AbstractNamedEntity {

    private LocalDate date;

    private List<Dish> menu;

    private Restaurant restaurant;

    public Menu() {
    }

    public Menu(Integer id, LocalDate date) {
        super(id, "Menu on " + date);
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Dish> getMenu() {
        return menu;
    }

    public void setMenu(List<Dish> menu) {
        this.menu = menu;
    }
}
