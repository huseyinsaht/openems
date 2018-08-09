package market.agent.production.historybased;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition( //
		name = "MarketAgent History-Based Production", //
		description = "Represents production at the market.")
@interface Config {
	String service_pid();

	String id() default "agent0";

	@AttributeDefinition(name = "Meter-ID", description = "ID of represented meter.")
	String meter_id();

	boolean enabled() default true;

	@AttributeDefinition(name = "Speed-Factor", description = "Each time-constant like 'day' is multiplied by this factor.")
	double speedFactor() default 1.0;

	@AttributeDefinition(name = "Price", description = "The price each kWh produced by the underlying producer costs in 1€.")
	double price();

	String webconsole_configurationFactory_nameHint() default "MarketAgent History-Based Production [{id}]";
}