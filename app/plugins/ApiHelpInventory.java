package plugins;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.fabric.xmlrpc.base.Array;

import io.swagger.*;
import io.swagger.annotations.Api;
import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.DefaultReaderConfig;
import io.swagger.jaxrs.config.ReaderConfig;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import io.swagger.models.Swagger;
import io.swagger.util.Json;
import play.*;
import play.classloading.ApplicationClasses.ApplicationClass;
import play.mvc.Router;
import play.mvc.Router.Route;

/**
 * Exposes two primary methods: 
 * to get a list of available resources and to get details on a given resource
 *
 * @author abhishek
 *
 */
public class ApiHelpInventory {

	// Read various configurable properties. These can be specified in application.conf
	private static final String API_VERSION = 
			Play.configuration.getProperty("api.version", "beta");

	private static final String basePath = 
			Play.configuration.getProperty("swagger.api.basepath", "http://localhost/");

	private static final String apiFilterClassName = 
			Play.configuration.getProperty("swagger.security.filter");

	private Set<Class<?>> controllerClasses = new HashSet<>();

	private static ApiHelpInventory apiHelpInventory = new ApiHelpInventory();

	private ApiHelpInventory(){

	}

	/* Static 'instance' method */
	public static ApiHelpInventory getInstance() {
		return apiHelpInventory;
	}

	private Set<Class<?>> getControllerClasses(){

		controllerClasses.clear();

		List<ApplicationClass> allClasses = Play.classes.all();

		for (ApplicationClass clazz : allClasses) {

			if (clazz.name.startsWith("controllers.")) {
				if (isRessource(clazz.javaClass)) {
					controllerClasses.add(clazz.javaClass); 
				}
			}
		}

		return controllerClasses;

	}

	public String getRootHelpJson(){
		
		SwaggerSerializers.setPrettyPrint(true);
		Swagger swagger = new Swagger();			
		ReaderConfig readerConfig = new ReaderConfig() {
			
			@Override
			public boolean isScanAllResources() {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public Collection<String> getIgnoredRoutes() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		
		PlayReader reader = new PlayReader(swagger, readerConfig);
		swagger = reader.read(controllerClasses);
			   
		ObjectMapper commonMapper = Json.mapper();
		
		try {
			return commonMapper.writeValueAsString(swagger);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * Get a list of all top level resources
	 */
	public Set<Class<?>> getRootResources() {	

		if(controllerClasses.isEmpty()){			
			getControllerClasses();			
		} 

		return controllerClasses;

	}


	private boolean isRessource(Class<?> javaClass) {
		// ignore interfaces and abstract classes
		if (javaClass.isInterface()
				|| Modifier.isAbstract(javaClass.getModifiers()))
			return false;

		// check for @Path or @Provider annotation
		if (hasAnnotation(javaClass, Path.class))
			return true;

		return false;
	}

	private boolean hasAnnotation(Class<?> type, Class<? extends Annotation> annotation) {
		// null cannot have annotations
		if (type == null)
			return false;

		// class annotation
		if (type.isAnnotationPresent(annotation))
			return true;

		// annotation on interface
		for (Class<?> interfaceType : type.getInterfaces()) {
			if (hasAnnotation(interfaceType, annotation))
				return true;
		}

		// annotation on superclass
		return hasAnnotation(type.getSuperclass(), annotation);

	}

} 
