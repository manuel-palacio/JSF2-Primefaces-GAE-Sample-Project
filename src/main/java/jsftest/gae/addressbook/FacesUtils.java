package jsftest.gae.addressbook;


import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;


@Component
public class FacesUtils implements Serializable {
    public void addSuccessMessage(String msg) {
        addMessage(FacesMessage.SEVERITY_INFO, msg);
    }

    public void addErrorMessage(String msg) {
        addMessage(FacesMessage.SEVERITY_ERROR, msg);
    }

    private void addMessage(FacesMessage.Severity severity, String msg) {
        final FacesMessage facesMsg = new FacesMessage(severity, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

}
