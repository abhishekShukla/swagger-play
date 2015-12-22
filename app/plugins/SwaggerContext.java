package plugins;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ThisExpression;

public class SwaggerContext {	

	public boolean suffixResponseFormat = true;

	private List<ClassLoader> classLoaders = new ArrayList<>();
	
	private static SwaggerContext swaggerContext = new SwaggerContext();
	
	/* Static 'instance' method */
	public static SwaggerContext getInstance() {
		return swaggerContext;
	}

	private SwaggerContext (){
		classLoaders.add(this.getClass().getClassLoader());
	}

	public SwaggerContext registerClassLoader(ClassLoader cl){
		this.classLoaders.add(cl);
		return swaggerContext;
	}

	public void loadClass(String className){

	}

}
