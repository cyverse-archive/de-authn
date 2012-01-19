package org.iplantc.authn.service;

import java.util.List;
import org.apache.log4j.Logger;
import org.iplantc.authn.exception.UserNotFoundException;
import org.iplantc.authn.user.User;
import org.iplantc.security.Saml2UserDetails;
import org.mule.RequestContext;
import org.mule.api.security.Authentication;

/**
 * The User Session Service is used to obtain information regarding the user within
 * the current thread.
 * @author Donald A. Barre
 */
public class UserSessionServiceImpl implements UserSessionService {

    private static transient final Logger LOG = Logger.getLogger(UserSessionServiceImpl.class);

    // User attribute values used for development testing.
    private static final String DEV_USERNAME = "ipctest@iplantcollaborative.org";
    private static final String DEV_PASSWORD = "none";
    private static final String DEV_EMAIL = "ipctest@iplantcollaborative.org";
    private static final String DEV_SHORT_USERNAME = "ipctest";

    // The message to log if the user can't be found.
    private static final String ERROR_MSG = "Unable to find the user for the current session.";

    private boolean securityEnabled;

    /**
     * Set the security to enabled or disabled.
     * @param securityEnabled
     */
    public void setSecurityEnabled(boolean securityEnabled) {
        this.securityEnabled = securityEnabled;
    }

    /* (non-Javadoc)
     * @see org.iplantc.authn.service.UserSessionService#getUser()
     */
    @Override
    public User getUser() {
        if (!securityEnabled) {
            User user = new User();
            user.setUsername(DEV_USERNAME);
            user.setPassword(DEV_PASSWORD);
            user.setEmail(DEV_EMAIL);
            user.setShortUsername(DEV_SHORT_USERNAME);
            return user;
        }
        else {
            Saml2UserDetails userDetails = getUserDetails();
            User user = new User();
            user.setUsername(userDetails.getUsername());
            user.setPassword(userDetails.getPassword());
            user.setEmail(getAttribute(userDetails, "Mail"));
            user.setShortUsername(getAttribute(userDetails, "Uid"));
            return user;
        }
    }

    /**
     * Gets an attribute value.
     * 
     * @param userDetails the user details.
     * @param name the user name.
     * @return the attribute value or null if the attribute has no value.
     */
    private String getAttribute(Saml2UserDetails userDetails, String name) {
        List<String> values = userDetails.getAttribute(name);
        String value = values.isEmpty() ? null : values.get(0);
        return value;
    }

    /**
     * Get the user's details from the Spring Security Context.
     * @return the user's details
     */
    private Saml2UserDetails getUserDetails() {
        Authentication authn = RequestContext.getEvent().getSession().getSecurityContext().getAuthentication();
        Object principal = authn.getPrincipal();
        if (principal != null && principal instanceof Saml2UserDetails) {
            return (Saml2UserDetails) principal;
        }
        LOG.fatal(ERROR_MSG);
        throw new UserNotFoundException();
    }
}
