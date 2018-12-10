package telecarto.data.util;

public class GeometryFromAPI {
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    private String type;    //几何类型

//    public ArrayList<Integer> getCoordinates() {
//        return coordinates;
//    }
//
//    public void setCoordinates(ArrayList<Integer> coordinates) {
//        this.coordinates = coordinates;
//    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    private double[] coordinates; //几何坐标



}
