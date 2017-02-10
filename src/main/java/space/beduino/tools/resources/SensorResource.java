package space.beduino.tools.resources;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.ResourceAttributes;

/**
 * Created by Stefan Oberdörfer on 28.12.2016.
 */
public class SensorResource extends CoapResource {

    private boolean isNumeric;

    public SensorResource(String endpointName, String title, boolean isNumeric) {
        super(endpointName);
        this.isNumeric = isNumeric;

        ResourceAttributes attributes = getAttributes();
        attributes.setTitle(title);
        //https://openconnectivity.org/specs/OIC_Resource_Type_Specification_v1.1.0.pdf
        if(isNumeric) {
            attributes.addResourceType("oic.r.sensor.illuminance");
        } else {
            attributes.addResourceType("oic.r.sensor");
        }
    }

    @Override
    public void handleGET(CoapExchange exchange) {
        String payload = String.format("%1$,.2f",Math.random());
        if(!isNumeric) {
            payload += " km/h";
        }
        exchange.respond(payload);
    }

}
