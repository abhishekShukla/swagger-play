package plugins;

import java.util.Collection;

import io.swagger.jaxrs.config.ReaderConfig;

public class PlayReaderConfig implements ReaderConfig {

	@Override
	public boolean isScanAllResources() {
		return true;
	}

	@Override
	public Collection<String> getIgnoredRoutes() {		
		return null;
	}

}
