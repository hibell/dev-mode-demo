package io.openliberty.guides.rest;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;

import java.util.Properties;

@Path("/properties")
public class PropertiesResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Properties getProperties() {
        return System.getProperties();
    }

    //@GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProperty(
            @Parameter(
                    name = "name",
                    description = "Name of property to retrieve",
                    required = true,
                    in = ParameterIn.PATH
            )
            @NotNull(message = "property name cannot be null")
            @PathParam("name") final String name
    ) {
        var property = new Property(name, System.getProperty(name, "TBD"));
        return Response.ok(property).build();
    }

    public static class Property {
        private String name;
        private String value;

        public Property() {}

        public Property(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
