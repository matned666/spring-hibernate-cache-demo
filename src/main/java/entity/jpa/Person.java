package entity.jpa;

import javax.persistence.*;

@Entity(name = "testujemyNazwe")
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="street", column = @Column (name = "billing_street")),
            @AttributeOverride(name="city", column = @Column (name = "billing_city")),
            @AttributeOverride(name="zipCode", column = @Column (name = "billing_zip_code"))
    })
    private Address billingAddress;

    private Address address;

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", billingAddress=" + billingAddress +
                ", address=" + address +
                '}';
    }
}
