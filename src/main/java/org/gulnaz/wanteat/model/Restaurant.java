package org.gulnaz.wanteat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
