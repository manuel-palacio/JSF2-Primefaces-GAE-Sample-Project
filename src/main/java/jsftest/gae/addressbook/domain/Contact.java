package jsftest.gae.addressbook.domain;


import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cached;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Cached
public class Contact implements Serializable {

    private static final long serialVersionUID = 1999L;

    @Id
    Long id;

    String email;

    String name;

    Key<ContactImage> contactImage;

    @Embedded
    List<Address> addresses = new ArrayList<Address>();

    String ownerId;

    public Long getId() {
        return id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }


    public List<Address> getAddresses() {
        return new ArrayList<Address>(addresses);
    }

    public void addAddress(Address address) {
        addresses.add(address);
    }

    public Key<ContactImage> getContactImage() {
        return contactImage;
    }

    public void setContactImage(Key<ContactImage> contactImage) {
        this.contactImage = contactImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Contact{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public void removeAddress(Address address) {
        addresses.remove(address);
    }

    public Address findAddress(Long addressId) {
        for (Address address : addresses) {
            if (address.getId().equals(addressId)) {
                return address;
            }
        }
        return null;
    }

    public void updateAddress(Address address) {
        Address foundAddress = findAddress(address.getId());
        foundAddress.update(address);
    }
}
