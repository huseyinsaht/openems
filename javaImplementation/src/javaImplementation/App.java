package javaImplementation;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Designate;


@Designate( ocd=App.Config.class, factory=true)
@Component(name="javaImplementation")
public class App {

	@ObjectClassDefinition
	@interface Config {
		String name() default "World";
	}

	private String name;

	@Activate
	void activate(Config config) {
		this.name = config.name();
	}

	@Deactivate
	void deactivate() {
	}

}
