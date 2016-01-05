package plugins;

public class PlayConfigFactory {

    private static PlaySwaggerConfig instance;

    public static void setConfig(PlaySwaggerConfig playSwaggerConfig) {
        instance = playSwaggerConfig;
    }

    public static PlaySwaggerConfig getConfig() {
        return instance;
    }
}