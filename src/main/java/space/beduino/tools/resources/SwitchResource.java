package space.beduino.tools.resources;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.ResourceAttributes;

/**
 * Created by Stefan Oberdörfer on 28.12.2016.
 */
public class SwitchResource extends CoapResource {

    private boolean status;

    public SwitchResource(String name) {
        super(name);

        ResourceAttributes attributes = getAttributes();
        attributes.setTitle("GET switch status (1 or 0) and POST (1 or 0) to control the switch");
        attributes.addResourceType("Switch");
    }

    @Override
    public void handleGET(CoapExchange exchange) {
        exchange.respond(status ? "1" : "0");
    }

    @Override
    public void handlePOST(CoapExchange exchange) {

        String payload = exchange.getRequestText();
        Response res;

        switch (payload) {
            case "0":
                status = false;
                res = new Response(CoAP.ResponseCode.CHANGED);
                break;
            case "1":
                status = true;
                res = new Response(CoAP.ResponseCode.CHANGED);
                break;
            default:
                res = new Response(CoAP.ResponseCode.BAD_REQUEST);
                break;
        }

        exchange.respond(res);
    }
}
