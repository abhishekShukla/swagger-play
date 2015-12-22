package plugins;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.fabric.xmlrpc.base.Array;

import io.swagger.*;
import io.swagger.annotations.Api;
import io.swagger.models.Swagger;
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
			Play.configuration.getProperty("swagger.api.basepath", "http://localhost");

	private static final String apiFilterClassName = 
			Play.configuration.getProperty("swagger.security.filter");

	private SwaggerContext swaggerContext = SwaggerContext.
			getInstance().registerClassLoader(Play.classloader);

	private Map<String, Class> resourceMap = new HashMap<>();

	private List<Class> controllerClasses = new ArrayList<>();

	private boolean filterOutTopLevelApi = true;

	private static ApiHelpInventory apiHelpInventory = new ApiHelpInventory();

	private ApiHelpInventory(){

	}

	/* Static 'instance' method */
	public static ApiHelpInventory getInstance() {
		return apiHelpInventory;
	}

	private List<Class> getControllerClasses(){

		if(controllerClasses.isEmpty()) {

			List<ApplicationClass> allClasses = Play.classes.all();

			for (ApplicationClass clazz : allClasses) {
				if (clazz.name.startsWith("controllers.")) {
					if (clazz.javaClass != null && !clazz.javaClass.isInterface() 
							&& !clazz.javaClass.isAnnotation()) {

						controllerClasses.add(clazz.javaClass); 

					}
				}
			}

			if(!controllerClasses.isEmpty()) {

				for (Class clazz : controllerClasses) {

					Api apiAnnotation = (Api) clazz.getAnnotation(Api.class);

					if (apiAnnotation != null) {
						Logger.debug("Identified Resource " + 
								clazz.toString() + " :: " + 
								apiAnnotation.value());

						resourceMap.put(apiAnnotation.value(), clazz); 
					}
				}

			}
		}

		return controllerClasses;

	}

	public List<String> getResourceNames(){

		List<String> resourceNames = new ArrayList<>();
		Map<String, Class> resourceMap = getResourceMap();

		for(String key : resourceMap.keySet()){
			resourceNames.add(key);
		}

		return resourceNames;

	}
	public String getRootHelpJson(String apiPath){

		Swagger swagger = getRootResources("json");		

		if(swagger != null){
			ObjectMapper jacksonObjectMapper = new ObjectMapper();
			try {
				
				return jacksonObjectMapper.writeValueAsString(swagger);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return "";
			}
		}
		return "";		
	}

	/**
	 * Get a list of all top level resources
	 */
	private Swagger getRootResources(String format) {	

		PlayReader playReader = new PlayReader(null);

		Set<Class<?>> classes = new HashSet<>();

		for(String string : resourceMap.keySet()){
			classes.add(resourceMap.get(string));
		}

		Swagger swagger = playReader.read(classes);
		return swagger;

	}

	/**
	 * Get detailed API/models for a given resource
	 */
	private Swagger getResource(String resourceName){

		Swagger swagger = null;

		Class clazz = getResourceMap().get(resourceName);

		if(clazz != null){

			Api apiAnnotation = (Api) clazz.getAnnotation(Api.class);

			String currentApiPath = null;

			if(apiAnnotation != null && filterOutTopLevelApi){
				currentApiPath = apiAnnotation.value();
			}

			swagger = PlayApiReader.read(clazz);			

		}			

		return swagger;

	}

	public String getPathHelpJson(String path){

		Swagger swagger = getResource(path);
		if(swagger != null){
			ObjectMapper jacksonObjectMapper = new ObjectMapper();
			try {
				return jacksonObjectMapper.writeValueAsString(swagger);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return "";
			}
		}
		return "";
	}

	public Map<String, Class> getResourceMap(){

		if(controllerClasses.isEmpty()){			
			getControllerClasses();			
		} 

		return resourceMap;

	}

} 
