package com.zz.chart.data;

/**
 * Created by Administrator on 2017/10/30.
 */
public class ClassData {
    private String rgn_code;
    private String rgn_name;
    private String geometry;
    private String thematic_data;
    private String region_x;
    private String region_y;
    private String dataSource;
    private String label;


    private String dataTime;


    public ClassData(String name,String code,String region_x,String region_y,String geometry,String data,String label){
        this.rgn_name = name;
        this.rgn_code = code;
        this.region_x = region_x;
        this.region_y = region_y;
        this.geometry = geometry;
        this.thematic_data = data;
        this.label = label;

//        this.dataSource = dataSource;
    }
    public String getLabel(){
        return this.label;
    }
    public void setLabel(String label){
        this.label = label;
    }
    public String getCode(){
        return this.rgn_code;
    }
    public void setCode(String code){
        this.rgn_code = code;
    }
    public String getName(){
        return this.rgn_name;
    }
    public void setName(String name){
        this.rgn_name = name;
    }
    public String getGeometry(){
        return this.geometry;
    }
    public void setGeometry(String geometry){
        this.geometry = geometry;
    }
    public String getData(){
        return this.thematic_data;
    }
    public void setData(String data){
        this.thematic_data = data;
    }
    public String getX(){
        return this.region_x;
    }
    public void setX(String region_x){
        this.region_x = region_x;
    }
    public String getY(){
        return this.region_y;
    }
    public void setY(String region_y){
        this.region_y = region_y;
    }
    public String getDataSource(){
        return this.dataSource;
    }
    public void setDataSource(String dataSource){
        this.dataSource = dataSource;
    }
    public String getDataTime() {
        return dataTime;
    }
    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }
}
