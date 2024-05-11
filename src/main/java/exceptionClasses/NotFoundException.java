/**
 * @author rifa 20220701
 */
package exceptionClasses;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class NotFoundException extends WebApplicationException {

    public NotFoundException(String message) {
        super(Response.status(Response.Status.NOT_FOUND).entity(message).build());
    }
}

