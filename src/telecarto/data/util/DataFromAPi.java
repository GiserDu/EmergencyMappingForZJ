package telecarto.data.util;

import java.util.ArrayList;


public class DataFromAPi {



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;    //数据类型


    public ArrayList<FeaturesFromAPI> getFeatures() {
        return features;
    }

    public void setFeatures(ArrayList<FeaturesFromAPI> features) {
        this.features = features;
    }

    private ArrayList<FeaturesFromAPI> features;

}
