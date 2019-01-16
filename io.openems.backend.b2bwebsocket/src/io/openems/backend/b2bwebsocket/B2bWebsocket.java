package io.openems.backend.b2bwebsocket;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.metatype.annotations.Designate;

import io.openems.backend.edgewebsocket.api.EdgeWebsocket;
import io.openems.backend.metadata.api.Metadata;

@Designate(ocd = Config.class, factory = true)
@Component( //
		name = "Backend2Backend.Websocket", //
		immediate = true, //
		configurationPolicy = ConfigurationPolicy.REQUIRE //
)
public class B2bWebsocket {

	public final static int DEFAULT_PORT = 8077;

	private WebsocketServer server = null;

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.DYNAMIC)
	protected volatile EdgeWebsocket edgeWebsocket;

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.DYNAMIC)
	protected volatile Metadata metadata;

	@Activate
	void activate(Config config) {
		this.startServer(config.port());
	}

	@Deactivate
	void deactivate() {
		this.stopServer();
	}

	/**
	 * Create and start new server
	 * 
	 * @param port
	 */
	private synchronized void startServer(int port) {
		this.server = new WebsocketServer(this, "Backend2Backend.Websocket", port);
		this.server.start();
	}

	/**
	 * Stop existing websocket server
	 */
	private synchronized void stopServer() {
		if (this.server != null) {
			this.server.stop();
		}
	}

}
