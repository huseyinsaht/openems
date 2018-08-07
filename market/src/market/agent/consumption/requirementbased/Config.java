package market.agent.consumption.requirementbased;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition( //
		name = "MarketAgent Requirement-Based Consumption", //
		description = "Represents consumption at the market.")
@interface Config {
	String service_pid();

	String id() default "agent2";

	@AttributeDefinition(name = "Meter-ID", description = "ID of represented meter.")
	String meter_id();

	boolean enabled() default true;

	@AttributeDefinition(name = "Speed-Factor", description = "Each time-constant like 'day' is multiplied by this factor.")
	double speedFactor() default 1.0;

	@AttributeDefinition(name = "Price", description = "The price the underlying consumer is willing to pay per kWh in 1€.")
	double price();

	String webconsole_configurationFactory_nameHint() default "MarketAgent Requirement-Based Consumption [{id}]";
}