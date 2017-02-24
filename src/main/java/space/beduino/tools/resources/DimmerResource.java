package space.beduino.tools.resources;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.ResourceAttributes;

/**
 * Created by Stefan Oberdörfer on 02.02.17.
 */
public class DimmerResource extends CoapResource {

    private float value;

    public DimmerResource(String endpointName, String title, float initialValue) {
        super(endpointName);
        value = initialValue;

        ResourceAttributes attributes = getAttributes();
        attributes.setTitle(title);
        attributes.addResourceType("oic.r.light.dimming");
    }

    @Override
    public void handleGET(CoapExchange exchange) {
        exchange.respond(String.valueOf(value));
    }

    @Override
    public void handlePOST(CoapExchange exchange) {
        String payload = exchange.getRequestText();
        float percentPayload;

        try {
            percentPayload = Float.parseFloat(payload);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            exchange.respond(new Response(CoAP.ResponseCode.BAD_REQUEST));
            return;
        }

        value = percentPayload;

        exchange.respond(new Response(CoAP.ResponseCode.CHANGED));
    }
}
