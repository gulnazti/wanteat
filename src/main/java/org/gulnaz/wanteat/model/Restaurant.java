package org.gulnaz.wanteat.model;

import java.util.List;

/**
 * @author gulnaz
 */
public class Restaurant extends AbstractNamedEntity {

    private String address;

    private List<Menu> menus;

    public Restaurant() {
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
}
