package telecarto.data.util;

import java.util.Map;

public class FeaturesFromAPI {

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;
    private GeometryFromAPI geometry;

    public void setGeometry(GeometryFromAPI geometry) {
        this.geometry = geometry;
    }


    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    private Map<String,String> properties;


    public GeometryFromAPI getGeometry() {
        return geometry;
    }
    public void setGeometry(String featType) {
        this.geometry = geometry;
    }
}
