package jsftest.gae.addressbook

import javax.inject.Inject
import jsftest.gae.addressbook.domain.Contact
import jsftest.gae.addressbook.service.ContactService
import org.primefaces.model.LazyDataModel
import org.primefaces.model.SortOrder
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Controller

@Controller
@Scope("view")
class ContactTableManager implements Serializable {

    private static final long serialVersionUID = 1999L

    ContactService contactService

    LazyDataModel<Contact> contactModel

    FacesUtils facesUtils

    Identity identity

    Contact selectedContact

    String searchText



    @Inject
    public ContactTableManager(final ContactService contactService, FacesUtils facesUtils, final Identity identity) {
        this.facesUtils = facesUtils

        contactModel = new LazyDataModel<Contact>() {


            @Override
            public List<Contact> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
                return contactService.getAllContacts(first, pageSize, identity.currentUser())
            }
        };

        contactModel.setRowCount(contactService.getNumberOfContacts(identity.currentUser()))

        this.contactService = contactService
        this.facesUtils = facesUtils
        this.identity = identity
    }

    void deleteContact() {
        contactService.delete(selectedContact);
        contactModel.setWrappedData(contactService.getAllContacts(contactModel.getRowIndex(),
                contactModel.getRowCount(), identity.currentUser()))
    }


    void updateContact() {
        contactService.updateContact(selectedContact, null)
        facesUtils.addSuccessMessage("contact updated")
    }

    void findContact() {
        List<Contact> foundContacts = contactService.findContactByName(searchText.trim(), identity.currentUser())
        if (!foundContacts.isEmpty()) {
            contactModel = new LazyDataModel<Contact>() {

                @Override
                public List<Contact> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
                    return foundContacts;
                }
            }

            contactModel.setRowCount(foundContacts.size())
        } else {
            facesUtils.addErrorMessage("Could not find " + searchText)
        }
    }

}
