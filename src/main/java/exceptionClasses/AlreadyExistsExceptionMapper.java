/**
 * @author rifad 20220701
 */
package exceptionClasses;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AlreadyExistsExceptionMapper implements ExceptionMapper<AlreadyExistsException> {

    @Override
    public Response toResponse(AlreadyExistsException exception) {
        return Response.status(Response.Status.CONFLICT)
                .entity("Resource already exists: " + exception.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
