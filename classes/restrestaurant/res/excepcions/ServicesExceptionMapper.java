package restrestaurant.res.excepcions;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class ServicesExceptionMapper implements ExceptionMapper<ServicesException> {
    @Override
    public Response toResponse(ServicesException exception) {
        return Response.status(Status.BAD_REQUEST).entity(exception.getMensaje()).build();
    }
}
