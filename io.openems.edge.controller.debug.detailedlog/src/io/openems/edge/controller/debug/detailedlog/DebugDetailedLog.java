package io.openems.edge.controller.debug.detailedlog;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.CaseFormat;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.controller.api.Controller;

/**
 * This controller prints all channels and their values on the console.
 * 
 * @author stefan.feilmeier
 *
 */
@Designate(ocd = Config.class, factory = true)
@Component(name = "Controller.Debug.DetailedLog", immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
public class DebugDetailedLog extends AbstractOpenemsComponent implements Controller, OpenemsComponent {

	private final Logger log = LoggerFactory.getLogger(DebugDetailedLog.class);

	private final int WIDTH_FIRST = 30;

	private List<OpenemsComponent> _components = new CopyOnWriteArrayList<>();

	@Reference
	protected ConfigurationAdmin cm;

	@Reference(policy = ReferencePolicy.DYNAMIC, policyOption = ReferencePolicyOption.GREEDY, cardinality = ReferenceCardinality.MULTIPLE)
	void addComponent(OpenemsComponent component) {
		this._components.add(component);
	}

	void removeComponent(OpenemsComponent component) {
		this._components.remove(component);
	}

	@Activate
	void activate(ComponentContext context, Config config) {
		// update filter for 'Component'
		if (OpenemsComponent.updateReferenceFilter(this.cm, config.service_pid(), "Component",
				config.component_ids())) {
			return;
		}
		super.activate(context, config.service_pid(), config.id(), config.enabled());
	}

	@Deactivate
	protected void deactivate() {
		super.deactivate();
	}

	@Override
	public void run() {
		this._components.stream().forEach(component -> {
			logInfo(this.log, "=======================================");
			this.log("ID", component.id());
			this.log("Service-PID", component.servicePid());
			this.log("Implementation", reducePackageName(component.getClass()));
			getInheritanceViaReflection(component.getClass(), null).asMap().forEach((inheritance, names) -> {
				boolean first = true;
				for (String name : names) {
					if (first) {
						log(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, inheritance.name()), name);
					} else {
						log("", name);
					}
					first = false;
				}
			});
			logInfo(this.log, "---------------------------------------");
			component.channels().stream() //
					.sorted((c1, c2) -> c1.channelId().name().compareTo(c2.channelId().name())) //
					.forEach(channel -> {
						Object valueObj = channel.getActiveValue();
						String value;
						if (valueObj == null) {
							value = "_undefined_";
						} else {
							value = valueObj.toString();
						}
						String unit = channel.channelDoc().getUnit().getSymbol();
						this.logInfo(this.log, String.format("%-" + WIDTH_FIRST + "s : %15s %s",
								channel.channelId().id(), value, unit));
					});
			logInfo(this.log, "---------------------------------------");
		});
	}

	private enum Inheritance {
		EXTEND, IMPLEMENT;
	}

	private static Multimap<Inheritance, String> getInheritanceViaReflection(Class<?> clazz,
			Multimap<Inheritance, String> map) {
		if (map == null) {
			map = HashMultimap.create();
		}
		Class<?> superClazz = clazz.getSuperclass();
		if (superClazz != null && !superClazz.equals(Object.class)) {
			map.put(Inheritance.EXTEND, reducePackageName(superClazz));
			getInheritanceViaReflection(superClazz, map);
		}
		for (Class<?> iface : clazz.getInterfaces()) {
			map.put(Inheritance.IMPLEMENT, reducePackageName(iface));
			getInheritanceViaReflection(iface, map);
		}
		return map;
	}

	private void log(String topic, String message) {
		this.logInfo(this.log, String.format("%-" + WIDTH_FIRST + "s : %s", topic, message));
	}

	private static String reducePackageName(Class<?> clazz) {
		return reducePackageName(clazz.getName());
	}

	private static String reducePackageName(String name) {
		if (name.startsWith("io.openems.edge.")) {
			return name.substring(16);
		}
		return name;
	}
}