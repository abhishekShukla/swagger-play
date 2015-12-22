package plugins;

import java.util.Map;

import play.mvc.Router.Route;

public class RouteWrapper {

    private Map<String, Route> router;

    public RouteWrapper(Map<String, Route> router) {
        this.router = router;
    }

    public Route get(String routeName) {
        return router.get(routeName);
    }

    public boolean exists(String routeName) {
        return router.containsKey(routeName);
    }

    public Map<String, Route> getAll() {
        return router;
    }


}