package io.openems.backend.timedata.influx;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition( //
		name = "Timedata.InfluxDB", //
		description = "Configures the InfluxDB Timedata provider")
@interface Config {
	@AttributeDefinition(name = "Database", description = "The database name")
	String database();

	@AttributeDefinition(name = "URL", description = "The InfluxDB server URL")
	String url();

	@AttributeDefinition(name = "Port", description = "The InfluxDB server port")
	int port() default 8086;

	@AttributeDefinition(name = "Username", description = "The login username")
	String username();

	@AttributeDefinition(name = "Password", description = "The login password")
	String password();

	@AttributeDefinition(name = "Measurement", description = "The InfluxDB measurement")
	String measurement() default "data";

	String webconsole_configurationFactory_nameHint() default "Timedata.InfluxDB";
}
