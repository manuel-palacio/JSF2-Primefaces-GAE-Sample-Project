package jsftest.gae.addressbook.domain;

import com.googlecode.objectify.annotation.Cached;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;


@Cached
@Entity
public class ContactImage implements Serializable {
    @Id
    Long id;

    byte[] data;

    String ownerId;

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }


    public Long getId() {
        return id;
    }


}
