package it.io.openliberty.guides.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.openliberty.guides.rest.PropertiesResource;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.Properties;

public class EndpointIT {
    private static final String port = System.getProperty("http.port");
    private static final String context = System.getProperty("context.root");
    private static final String url = "/".equals(context) ?
            String.format("http://localhost:%s/", port) :
            String.format("http://localhost:%s/%s/", port, context);

    private static final Jsonb JSONB = JsonbBuilder.create();

    @Test
    public void testGetProperties() {
        var client = ClientBuilder.newClient();

        var target = client.target(url + "api/properties");
        var response = target.request().get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus(),
                "Incorrect response code from " + url);

        var json = response.readEntity(String.class);
        var sysProps = JSONB.fromJson(json, Properties.class);

        assertEquals(System.getProperty("os.name"), sysProps.getProperty("os.name"),
                "The system property for the local and remote JVM should match");
        response.close();
        client.close();
    }

    @Test
    public void testGetProperty() {
        var client = ClientBuilder.newClient();

        var target = client.target(url + "api/properties/os.name");
        var response = target.request().get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus(),
                "Incorrect response code from " + url);

        var json = response.readEntity(String.class);
        var property = JSONB.fromJson(json, PropertiesResource.Property.class);

        assertEquals(System.getProperty("os.name"), property.getValue(),
                "The system property for the local and remote JVM should match");
        response.close();
        client.close();
    }
}
