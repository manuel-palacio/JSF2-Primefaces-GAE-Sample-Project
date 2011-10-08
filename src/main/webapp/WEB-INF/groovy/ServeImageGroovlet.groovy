import com.google.appengine.api.images.Image
import com.google.appengine.api.images.ImagesService
import com.google.appengine.api.images.ImagesServiceFactory
import com.google.appengine.api.images.Transform
import com.googlecode.objectify.Key
import com.googlecode.objectify.Objectify
import com.googlecode.objectify.ObjectifyService
import jsftest.gae.addressbook.domain.ContactImage
import org.apache.commons.lang.StringUtils

//ObjectifyService.register(Contact.class)
//ObjectifyService.register(ContactImage.class)

String id = params.id
String crop = params.crop

Objectify ofy = ObjectifyService.begin()
if (StringUtils.isNotEmpty(id)) {
    ContactImage contactImage = ofy.get(new Key<ContactImage>(ContactImage.class, new Long(id)))
    if (contactImage != null) {
        byte[] img = contactImage.getData()
        if (crop != null) {
            ImagesService imagesService = ImagesServiceFactory.getImagesService()
            Transform cropped = ImagesServiceFactory.makeCrop(0.3, 0.3, 0.7, 0.7)
            Image newImage = imagesService.applyTransform(cropped, ImagesServiceFactory.makeImage(contactImage.getData()))
            img = newImage.getImageData()
        }
        response.setContentType("contactImage/jpeg")
        sout << img
        response.flushBuffer()
    }
}