/**
 * @author rifad 20220701
 */
package exceptionClasses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidDataExceptionMapper implements ExceptionMapper<InvalidDataException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvalidDataExceptionMapper.class);

    @Override
    public Response toResponse(InvalidDataException exception) {
        LOGGER.error("InvalidDataException caught: {}", exception.getMessage(), exception);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(exception.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}

