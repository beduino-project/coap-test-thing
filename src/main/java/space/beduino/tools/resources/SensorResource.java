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
        attributes.setTitle("GET a random light sensor value");
        //https://openconnectivity.org/specs/OIC_Resource_Type_Specification_v1.1.0.pdf
        attributes.addResourceType("oic.r.sensor.illuminance");
    }

    @Override
    public void handleGET(CoapExchange exchange) {
        String payload = String.format("%1$,.2f",Math.random());
        exchange.respond(payload);
    }

}
