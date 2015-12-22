package plugins;

import play.Logger;
import play.Play;
import play.PlayPlugin;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.Router.Route;
import plugins.ApiHelpInventory;
import play.mvc.Router;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This plugin adds two new entries to play route table for swagger help api.<br/>
 * help.route key can be used to application.conf to specify base help api path. 
 * By default this is "/help"<br/>
 *
 * @author abhishek
 * 
 */
public class SwaggerPlugin  extends PlayPlugin {

	private static final String JSON = ".json";
    private static final String APPLICATION_JSON = "application/json";
    private static final String UTF_8 = "UTF-8";
    private static final String RESOURCES_JSON = "/resources.json";
    

    @Override
    public boolean rawInvocation(Request request, Response response) throws Exception {
    	
    	Logger.info("Got request for " + request.path);

        try {
            final Set<Class<?>> resourceClasses = 
            		ApiHelpInventory.getInstance().getRootResources();
            
            if(resourceClasses.size() > 0){
            	            	            	
                if (request.path.equals(RESOURCES_JSON)) {
                    response.contentType = APPLICATION_JSON;
                    String jaxRsResponseJson = ApiHelpInventory.getInstance().getRootHelpJson();                    
                    response.out.write(jaxRsResponseJson.getBytes(UTF_8));
                    return true;
                } 
            }

        } catch (Exception e) {
            Logger.error(e, "Error in SwaggerPlugin");
        }

        return false;
    }

    
    @Override
    public void onApplicationStart() {
    	
    	final Set<Class<?>> resourceClasses = 
        		ApiHelpInventory.getInstance().getRootResources();
                
        if (resourceClasses.size() > 0) {
            
            Router.prependRoute("GET", RESOURCES_JSON, "ApiHelpController.catchAll");
            Logger.info("Swagger: Added ROOT help api @ " + RESOURCES_JSON);
        }
    }

    
    @Override
    public void afterApplicationStart() {
        Logger.info("afterApplicationStart");
    }
	
}
