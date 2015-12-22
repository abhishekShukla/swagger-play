package plugins;

import java.util.HashMap;
import java.util.Map;

import io.swagger.models.Swagger;


public class PlayApiReader {
	
	private static final Map<Class, Swagger> endpointsCache = new HashMap<>();
	
	public static Swagger read(Class hostClass){
		
		Swagger swagger = endpointsCache.get(hostClass);
				
		if(swagger == null){
			
			PlayReader playReader = new PlayReader(swagger);
			swagger = playReader.read(hostClass);
			return swagger;
			
		} else {
			return swagger;
		}
		
	} 

}
