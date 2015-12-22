package controllers;

import play.mvc.Controller;
import play.Logger;


/**
  * This controller exposes swagger compatible help APIs.<br/>
  * The routing for the two apis supported by this controller 
  * is automatically injected by SwaggerPlugin
  *
  * @author abhishek  
  */
public class ApiHelpController extends Controller {
	
	public static void catchAll(){
		Logger.info("ApiHelpController.catchAll got called. This should not happen; "
				+ "SwaggerPlugin.rawInvocation should be intercepting "
				+ "and processing this call");
	}

}
