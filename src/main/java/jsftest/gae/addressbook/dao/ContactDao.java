package jsftest.gae.addressbook.dao;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.googlecode.objectify.util.DAOBase;
import jsftest.gae.addressbook.domain.Contact;
import jsftest.gae.addressbook.domain.ContactImage;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ContactDao extends DAOBase implements Serializable {

    private final static long serialVersionUID = 1L;

    static {
        ObjectifyService.register(Contact.class);
        ObjectifyService.register(ContactImage.class);
    }

    public List<ContactImage> getAllContactImagesForUser(User user) {
        Query<ContactImage> query = fact().begin().query(ContactImage.class).filter("ownerId", user.getUserId());
        return query.list();
    }

    public List<Contact> getAllContactsForUser(int first, int max, User user) {
        Query<Contact> query = fact().begin().query(Contact.class).filter("ownerId", user.getUserId());
        query.offset(first);
        query.limit(max);

        return query.list();
    }

    public Contact findContactById(Long id, User user) {
        Query<Contact> query = fact().begin().query(Contact.class).filter("ownerId",
                user.getUserId()).filter("id", id);
        return query.get();
    }

    private Query<Contact> filter(String name, User user) {
        return fact().begin().query(Contact.class).filter("name =", name).filter("ownerId", user.getUserId());
    }

    public List<String> findContactsEmailAddresses(String name, User user) {
        List<String> emails = new ArrayList<String>();
        Query<Contact> q = filter(name, user);

        for (Contact contact : q) {
            emails.add(contact.getEmail());
        }

        return emails;
    }

    public int getNumberOfContactsForUser(User user) {
        Query<Contact> query = fact().begin().query(Contact.class).filter("ownerId", user.getUserId());
        return query.count();
    }

    public void deleteAllContactsForUser(User user) {
        Iterable<Key<Contact>> allKeys = fact().begin().query(Contact.class).filter("ownerId",
                user.getUserId()).fetchKeys();
        fact().begin().delete(allKeys);
    }


    public void delete(Object object) {
        fact().begin().delete(object);
    }

    public Key<Object> save(Object object) {
        return fact().begin().put(object);
    }

    public Contact findContactByEmail(String email, User user) {
        Query<Contact> query = fact().begin().query(Contact.class).filter("ownerId",
                user.getUserId()).filter("email", email);
        return query.get();
    }
}
