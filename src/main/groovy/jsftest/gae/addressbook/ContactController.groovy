package jsftest.gae.addressbook

import com.googlecode.objectify.Key
import java.util.logging.Logger
import javax.inject.Inject
import javax.validation.constraints.NotNull
import jsftest.gae.addressbook.domain.Address
import jsftest.gae.addressbook.domain.Contact
import jsftest.gae.addressbook.domain.ContactImage
import jsftest.gae.addressbook.service.ContactService
import org.hibernate.validator.constraints.Email
import org.primefaces.model.map.DefaultMapModel
import org.primefaces.model.map.LatLng
import org.primefaces.model.map.MapModel
import org.primefaces.model.map.Marker
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Controller

@Controller
@Scope("view")
class ContactController implements Serializable {
    private static final long serialVersionUID = 1999L

    @Email
    @NotNull
    String email
    @NotNull
    String name
    @NotNull
    String street
    @NotNull
    String city
    @NotNull
    String country

    @Inject
    private ContactService contactService

    @Inject
    private FacesUtils facesUtils;

    Contact selectedContact

    Address selectedAddress

    String searchText

    Long imageId

    Long addressId

    Long contactId

    LatLng coordinates

    @Inject
    private Identity identity

    private static final Logger log = Logger.getLogger(ContactController.class.getName())


    public void createContact() {
        Contact contact = new Contact()
        contact.setEmail(email)
        contact.setName(name)
        contact.setOwnerId(identity.currentUser().getUserId())
        if (imageId) {
            contact.setContactImage(new Key<ContactImage>(ContactImage.class, imageId))
        }
        Address address = new Address()
        address.setId(new Date().getTime())
        address.setStreet(street)
        address.setCity(city)
        address.setCountry(country)
        contact.addAddress(address)
        contactService.save(contact)
        facesUtils.addSuccessMessage("Added contact " + contact.getEmail())
        imageId = null
        log.info("Contact created " + contact.getEmail())
    }

    public void addAddressToContact() {
        Address address = new Address()
        address.setId(new Date().getTime())
        address.setStreet(street)
        address.setCity(city)
        address.setCountry(country)
        selectedContact = contactService.findContactById(selectedContact.getId(), identity.currentUser())
        selectedContact.addAddress(address)
        contactService.save(selectedContact)
        facesUtils.addSuccessMessage("Added address to " + address)
    }

    public void loadContact() {
        if (contactId != null) {
            selectedContact = contactService.findContactById(contactId, identity.currentUser())
            if (selectedContact != null) {
                if (addressId != null) {
                    selectedAddress = selectedContact.findAddress(addressId)
                    updateCoordinates()
                }
            } else {
                log.warning("Could not find contact with id " + email)
            }
        }
    }

    void updateCoordinates() {
        def params = [sensor: false,
                output: 'csv',
                q: [selectedAddress.street, selectedAddress.city, selectedAddress.country].collect { URLEncoder.encode(it, 'UTF-8') }.join(',+'),
                key: "ABQIAAAAFMDsOJxuGy-K-07T2nAzFBTzfnZNXHTyxnKjJvSCdzbWAZV-gxRh_bCWF_55CXbOMEwRMoDH1cg5Iw"
        ]

        def url = "http://maps.google.com/maps/geo?" + params.collect { k, v -> "$k=$v" }.join('&')
        def result = url.toURL().text.split(',')
        double lat = Double.parseDouble(result[-2])
        double lng = Double.parseDouble(result[-1])
        coordinates = new LatLng(lat, lng)

    }

    public void updateContact() {
        contactService.updateContact(selectedContact, imageId);
        facesUtils.addSuccessMessage("Contact updated");
    }


    MapModel getMapModel() {
        MapModel simpleModel = new DefaultMapModel()
        simpleModel.addOverlay(new Marker(coordinates))
        return simpleModel
    }


    public void deleteAddressForContact() {
        selectedContact = contactService.deleteAddressForContact(selectedAddress, selectedContact.id, identity.currentUser())
    }

    public void updateAddressForContact() {
        contactService.updateAddressForContactWithId(selectedAddress, selectedContact.id, identity.currentUser())
        facesUtils.addSuccessMessage("Address updated")
    }


    public String findContact() {
        Contact foundContact = contactService.findContactByEmail(searchText.trim(), identity.currentUser())
        if (foundContact != null) {
            selectedContact = foundContact
            return "/contactInfo.xhtml?faces-redirect=true&contactId=" + selectedContact.getId();
        } else {
            facesUtils.addErrorMessage("Could not find " + searchText)
            return null
        }
    }
}
