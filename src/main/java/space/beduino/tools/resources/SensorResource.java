package space.beduino.tools.resources;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.ResourceAttributes;

/**
 * Created by Stefan Oberdörfer on 28.12.2016.
 */
public class SensorResource extends CoapResource {

    public SensorResource(String name) {
        super(name);

        ResourceAttributes attributes = getAttributes();
        attributes.setTitle("GET a random sensor value");
        attributes.addResourceType("Sensor");
    }

    @Override
    public void handleGET(CoapExchange exchange) {
        String payload = String.format("%1$,.2f",Math.random());
        exchange.respond(payload);
    }

}
