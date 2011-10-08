package jsftest.gae.addressbook;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

@ManagedBean("identity")
@Scope("request")
public class Identity implements Serializable {

    public String getLoginUrl() {
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ctx.getRequest();
        HttpServletResponse response = (HttpServletResponse) ctx.getResponse();

        UserService userService = UserServiceFactory.getUserService();
        return userService.createLoginURL(response.encodeURL(request.getRequestURI()));
    }

    public String getLogoutUrl() {
        UserService userService = UserServiceFactory.getUserService();
        return userService.createLogoutURL("/");
    }

    public boolean isLoggedIn() {
        UserService userService = UserServiceFactory.getUserService();
        return userService.getCurrentUser() != null;
    }

    public User currentUser() {
        UserService userService = UserServiceFactory.getUserService();
        return userService.getCurrentUser();
    }
}
