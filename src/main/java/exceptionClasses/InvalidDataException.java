/**
 *
 * @author rifa 20220701
 */

package exceptionClasses;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class InvalidDataException extends WebApplicationException {

    public InvalidDataException(String message) {
        super(Response.status(Response.Status.BAD_REQUEST).entity(message).build());
    }
}

