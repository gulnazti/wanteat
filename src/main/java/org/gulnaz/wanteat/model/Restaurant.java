package org.gulnaz.wanteat.model;

import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * @author gulnaz
 */
@Entity
@Table(name = "restaurants")
public class Restaurant extends AbstractNamedEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "address", nullable = false)
    private String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OrderBy(value = "name")
    @JsonManagedReference
    private List<Dish> menu;

    public Restaurant() {
    }

    public Restaurant(Restaurant r) {
        this(r.getId(), r.getName(), r.getAddress());
    }

    public Restaurant(String name, String address) {
        this(null, name, address);
    }

    public Restaurant(Integer id, String name, String address) {
        super(id, name);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Dish> getMenu() {
        return menu;
    }

    public void setMenu(List<Dish> menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", address='" + address + '\'' +
               '}';
    }
}
