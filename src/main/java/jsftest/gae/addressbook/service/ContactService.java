package jsftest.gae.addressbook.service;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import jsftest.gae.addressbook.dao.ContactDao;
import jsftest.gae.addressbook.domain.Address;
import jsftest.gae.addressbook.domain.Contact;
import jsftest.gae.addressbook.domain.ContactImage;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@Service
public class ContactService implements Serializable {

    private static final long serialVersionUID = 1999L;

    @Inject
    private ContactDao contactDao;

    public void updateContact(Contact contact, Long imageId) {
        if (imageId != null) {
            contact.setContactImage(new Key<ContactImage>(ContactImage.class, imageId));
        }
        contactDao.save(contact);
    }

    public List<Contact> getAllContacts(int first, int pageSize, User user) {
        return contactDao.getAllContactsForUser(first, pageSize, user);
    }

    public int getNumberOfContacts(User user) {
        return contactDao.getNumberOfContactsForUser(user);
    }

    public void delete(Contact contact) {
        contactDao.delete(contact);
    }

    public void save(Contact contact) {
        contactDao.save(contact);
    }

    public void updateAddressForContactWithId(Address address, Long contactId, User user) {
        Contact foundContact = contactDao.findContactById(contactId, user);
        foundContact.updateAddress(address);
        contactDao.save(foundContact);
    }

    public Contact findContactById(Long id, User user) {
        return contactDao.findContactById(id, user);
    }

    public Contact findContactByEmail(String email, User user) {
        return contactDao.findContactByEmail(email, user);
    }

    public List<String> findContactsEmailAddresses(String name, User user) {
        return contactDao.findContactsEmailAddresses(name, user);
    }

    public Contact deleteAddressForContact(Address address, Long id, User user) {
        Contact selectedContact = contactDao.findContactById(id, user);
        if (selectedContact != null) {
            selectedContact.removeAddress(address);
            contactDao.save(selectedContact);
        }

        return selectedContact;
    }

    public void deleteAllContacts(User user) {
        contactDao.deleteAllContactsForUser(user);
    }
}
