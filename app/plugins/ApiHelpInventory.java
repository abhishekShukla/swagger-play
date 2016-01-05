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
import io.swagger.models.Contact;
import io.swagger.models.Info;
import io.swagger.models.License;
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

	public String getResourceSwaggerJson(){
		
		SwaggerSerializers.setPrettyPrint(true);				
		ReaderConfig readerConfig = new PlayReaderConfig();
		
		Swagger swagger = new Swagger();
		PlaySwaggerConfig config = PlayConfigFactory.getConfig();
		
		swagger.setHost(config.getHost());
		swagger.setBasePath(config.getBasePath());
		
		Info info = new Info();
		info.setVersion(config.getVersion());
		info.setTitle(config.getTitle());
		info.setContact(new Contact().name(config.getContact()));
		info.setLicense(new License().name(config.getLicense()).url(config.getLicenseUrl()));
		info.setDescription(config.getDescription());
		info.setTermsOfService(config.getTermsOfServiceUrl());
				
		swagger.setInfo(info);
		
		PlayReader reader = new PlayReader(swagger, readerConfig);
		swagger = reader.read(controllerClasses);
			   
		ObjectMapper commonMapper = Json.mapper();
		
		try {
			return commonMapper.writeValueAsString(swagger);
		} catch (JsonProcessingException e) {
			Logger.error(e.getMessage());			
			return "";
		}
	}

	/**
	 * Get a list of all top level resources
	 */
	public Set<Class<?>> getRootResources() {
		
		controllerClasses.clear();				
		getControllerClasses();					 
		return controllerClasses;

	}


	private boolean isRessource(Class<?> javaClass) {
		
		// ignore interfaces and abstract classes
		if (javaClass.isInterface()
				|| Modifier.isAbstract(javaClass.getModifiers())){
			return false;
		}
			

		// check for @Path or @Provider annotation
		if (hasAnnotation(javaClass, Path.class)){
			return true;
		}
			

		return false;
	}

	private boolean hasAnnotation(Class<?> type, Class<? extends Annotation> annotation) {
		
		// null cannot have annotations
		if (type == null) {
			return false;
		}
			

		// class annotation
		if (type.isAnnotationPresent(annotation)) {
			return true;
		}
			

		// annotation on interface
		for (Class<?> interfaceType : type.getInterfaces()) {
			if (hasAnnotation(interfaceType, annotation))
				return true;
		}

		// annotation on superclass
		return hasAnnotation(type.getSuperclass(), annotation);

	}

} 
