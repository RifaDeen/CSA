/**
 * @author rifa 20220701
 */
package exceptionClasses;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class AlreadyExistsException extends WebApplicationException {

    public AlreadyExistsException(String message) {
        super(Response.status(Response.Status.CONFLICT).entity(message).build());
    }
}
