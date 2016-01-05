package plugins;

import play.Play;

public class PlaySwaggerConfig {
	
	// Read various configurable properties. These can be specified in application.conf
	
	private String[] schemes;
	private String filterClass;
	
	private String version = Play.configuration.getProperty("api.version");
	private String basePath = Play.configuration.getProperty("swagger.api.basepath");
	private String host = Play.configuration.getProperty("swagger.api.host");
	private String title = Play.configuration.getProperty("swagger.api.info.title");
	private String description = Play.configuration.getProperty("swagger.api.info.description");
	private String termsOfServiceUrl = Play.configuration.getProperty("swagger.api.info.termsOfServiceUrl");
	private String contact = Play.configuration.getProperty("swagger.api.info.contact");
	private String license = Play.configuration.getProperty("swagger.api.info.license");
	private String licenseUrl = Play.configuration.getProperty("swagger.api.info.licenseUrl");

	public String[] getSchemes() {
		return schemes;
	}

	public void setSchemes(String[] schemes) {
		this.schemes = schemes;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTermsOfServiceUrl() {
		return termsOfServiceUrl;
	}

	public void setTermsOfServiceUrl(String termsOfServiceUrl) {
		this.termsOfServiceUrl = termsOfServiceUrl;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getLicenseUrl() {
		return licenseUrl;
	}

	public void setLicenseUrl(String licenseUrl) {
		this.licenseUrl = licenseUrl;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getFilterClass() {
		return filterClass;
	}

	public void setFilterClass(String filterClass) {
		this.filterClass = filterClass;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
}