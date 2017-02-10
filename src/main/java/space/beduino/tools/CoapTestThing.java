/*******************************************************************************
 * Copyright (c) 2016 Stefan Oberdörfer.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *    http://www.eclipse.org/org/documents/edl-v10.html.
 * 
 * Inspired by example of:
 *    Matthias Kovatsch - creator and main architect
 ******************************************************************************/
package space.beduino.tools;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;

import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.EndpointManager;
import org.eclipse.californium.core.network.config.NetworkConfig;
import space.beduino.tools.resources.DimmerResource;
import space.beduino.tools.resources.SensorResource;
import space.beduino.tools.resources.SwitchResource;

/**
 * This is an test server that contains resources to use from openHAB2 coap binding.
 */
public class CoapTestThing {

	private static final int COAP_PORT = NetworkConfig.getStandard().getInt(NetworkConfig.Keys.COAP_PORT);

	public static void main(String[] args) throws Exception {
		CoapServer server = new CoapServer();
		server.setExecutor(Executors.newScheduledThreadPool(4));

		server.add(new SwitchResource("lightswitch", "Light Switch"));
		server.add(new SensorResource("windspeed", "Light Illuminance", false));
		server.add(new CoapResource("actuators",false).add(new SwitchResource("lock1", "Security Lock")));
		server.add(new CoapResource("sensors",false).add(new SensorResource("temp1","Windspeed", true)));
		server.add(new DimmerResource("dim","LoungeLight",50f));
		addEndpoints(server);

		server.start();

		new UdpDiscoverySender().start();
	}

	/**
	 * Add individual endpoints listening on default CoAP port on all IPv6 addresses of all network interfaces.
	 */
	private static void addEndpoints(CoapServer server) {
		for (InetAddress addr : EndpointManager.getEndpointManager().getNetworkInterfaces()) {
			// only binds to IPv6 addresses and localhost (coap://[0:0:0:0:0:0:0:1]:5683)
			if (addr instanceof Inet6Address || addr.isLoopbackAddress()) {
				InetSocketAddress bindToAddress = new InetSocketAddress(addr, COAP_PORT);
				server.addEndpoint(new CoapEndpoint(bindToAddress));
			}
		}
	}
}
