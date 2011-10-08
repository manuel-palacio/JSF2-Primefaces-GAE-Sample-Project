import com.google.appengine.api.images.Image
import com.google.appengine.api.images.ImagesService
import com.google.appengine.api.images.ImagesServiceFactory
import com.google.appengine.api.images.Transform
import com.google.appengine.api.users.UserService
import com.google.appengine.api.users.UserServiceFactory
import com.googlecode.objectify.ObjectifyService
import jsftest.gae.addressbook.domain.ContactImage
import org.apache.commons.fileupload.FileItemIterator
import org.apache.commons.fileupload.FileItemStream
import org.apache.commons.fileupload.FileUploadException
import org.apache.commons.fileupload.servlet.ServletFileUpload
import org.apache.commons.io.IOUtils

//ObjectifyService.register(Contact.class)
//ObjectifyService.register(ContactImage.class)

boolean isMultipart = ServletFileUpload.isMultipartContent(request)

if (isMultipart) {
    ServletFileUpload upload = new ServletFileUpload()
    try {
        FileItemIterator iter = upload.getItemIterator(request)
        FileItemStream item = iter.next()
        InputStream stream = item.openStream()
        ImagesService imagesService = ImagesServiceFactory.getImagesService()
        byte[] uploadImageData = IOUtils.toByteArray(stream)

        Image uploadImage = ImagesServiceFactory.makeImage(uploadImageData)
        Transform resize = ImagesServiceFactory.makeResize(150, 250)

        Image newImage = imagesService.applyTransform(resize, uploadImage)

        byte[] newImageData = newImage.getImageData()
        ContactImage image = new ContactImage()
        UserService userService = UserServiceFactory.getUserService()
        image.setOwnerId(userService.getCurrentUser().getUserId())
        image.setData(newImageData)
        ObjectifyService.begin().put(image)
    } catch (FileUploadException e) {
        throw new IOException(e);
    }
}

html.html {
    head {
        title("File upload")
    }
    body {
        b("File uploaded and stored")
    }
}
