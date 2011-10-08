package jsftest.gae.addressbook

import javax.annotation.ManagedBean
import javax.inject.Inject
import jsftest.gae.addressbook.dao.ContactDao
import jsftest.gae.addressbook.domain.ContactImage
import org.springframework.context.annotation.Scope

@ManagedBean("imageController")
@Scope("request")
class ImageController implements Serializable {
    @Inject
    private ContactDao contactDao

    @Inject
    private Identity identity

    public List<ContactImage> getContactImages() {
        return contactDao.getAllContactImagesForUser(identity.currentUser())
    }


    public void deleteImage(ContactImage contactImage) {
        contactDao.fact().begin().delete(contactImage)
    }
}
