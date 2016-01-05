# swagger-play
Swagger 2.0 support for Play 1.x (1.3 and above) with swagger and jaxrs annotations.

How to use: 

1. Clone this repository
2. Change your Play dependencies file to contain the module
3. Run "play dependencies"
4. You should now be able to use the annotations in your controllers. 
5. Start the play application
6. If you have annotated controller methods which are actually route actions, you should be able to go to {application url}/resources.json and see the Swagger 2.0 spec json. 
7. Add swagger ui to your application (https://github.com/swagger-api/swagger-ui)
8. Modify the index.html to call the {application url/resources.json}. The UI should generate the rest for you. 

Note: 
You should have the following keys in the application conf file 

```
# Swagger options
api.version=
swagger.api.basePath=
swagger.api.host=
swagger.api.info.title=
swagger.api.info.description=
swagger.api.info.termsOfServiceUrl=
swagger.api.info.contact=
swagger.api.info.license=
swagger.api.info.licenseUrl=
```

Note: 
Whenever you change the annotations, you should make a call to the application before you call {application url}/resources.json to see the changes. 
