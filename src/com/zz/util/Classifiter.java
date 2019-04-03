package com.zz.util;

import com.zz.chart.data.ClassData;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/30.
 */
public class Classifiter {
    public static JSONArray getClassIntervalJson(double minValue, double maxValue, int breakNum, ArrayList<ClassData> classList,String[] colors,String model){
        double[] classInterval = getIntervals(minValue,maxValue,breakNum,model);
//        if(breakNum>1){
//            switch (model){
//                case "界限等分模型":classInterval = ClassModles.modelA1(minValue,maxValue,breakNum);
//                    break;
//                case "间隔等分模型":classInterval = ClassModles.modelB1(minValue,maxValue,breakNum);
//                    break;
//                case "界限等比模型":classInterval = ClassModles.modelC1(minValue,maxValue,breakNum);
//                    break;
//                case "间隔等比模型":classInterval = ClassModles.modelD1(minValue,maxValue,breakNum);
//                    break;
//                default:break;
//
//            }
//        }
        return classiFiter(classList,colors,classInterval);
    }

    public static double[] getIntervals(double minValue, double maxValue, int breakNum,String model){
        double[] classInterval = null;
        if(breakNum>1){
            switch (model){
                case "界限等分模型":classInterval = ClassModles.modelA1(minValue,maxValue,breakNum);
                    break;
                case "间隔等分模型":classInterval = ClassModles.modelB1(minValue,maxValue,breakNum);
                    break;
                case "界限等比模型":classInterval = ClassModles.modelC1(minValue,maxValue,breakNum);
                    break;
                case "间隔等比模型":classInterval = ClassModles.modelD1(minValue,maxValue,breakNum);
                    break;
                default:break;
            }
        }
        return classInterval;
    }

//    public double[] getClassInterval(double minValue, double maxValue, String breakNum){
//        double interval = (maxValue-minValue)/Double.parseDouble(breakNum);
//        int intervalNum = Integer.parseInt(breakNum)-1;
//        double[] classInterval = new double[intervalNum];
//        for (int i=0;i<intervalNum;i++){
//            classInterval[i] = minValue + interval*(i+1);
//        }
//        return classInterval;
//    }

    public static JSONArray classiFiter(ArrayList<ClassData> classList,String[] colors,double[] classInterval){
        JSONArray classDataArray = new JSONArray();
        for (int i=0;i<classList.size();i++){
            JSONObject classObject = new JSONObject();
            JSONObject classObjectAttr = new JSONObject();
            classObjectAttr.put("rgn_code",classList.get(i).getCode());
            classObjectAttr.put("rgn_name",classList.get(i).getName());
            classObjectAttr.put("data",classList.get(i).getData());
            classObjectAttr.put("centerX",classList.get(i).getX());
            classObjectAttr.put("centerY",classList.get(i).getY());
            classObjectAttr.put("dataTime",classList.get(i).getDataTime());
            classObjectAttr.put("label",classList.get(i).getLabel());
            if(classInterval == null){
                classObject.put("color",colors[0]);
                classObjectAttr.put("rgn_class","第1级");
            }
            else if(classInterval.length ==1){
                if(Double.parseDouble(classList.get(i).getData())<classInterval[0]){
                    classObject.put("color",colors[0]);
                    classObjectAttr.put("rgn_class","第1级");
                }
                else {
                    classObject.put("color",colors[1]);
                    classObjectAttr.put("rgn_class","第2级");
                }
            }
            else {
                for(int j=0;j<classInterval.length;j++){
                    if(j==0){
                        if(Double.parseDouble(classList.get(i).getData())<classInterval[j]){
                            classObject.put("color",colors[0]);
                            classObjectAttr.put("rgn_class","第1级");
                        }
                        if(Double.parseDouble(classList.get(i).getData())>=classInterval[j] && Double.parseDouble(classList.get(i).getData())<classInterval[j+1]){
                            classObject.put("color",colors[1]);
                            classObjectAttr.put("rgn_class","第2级");
                        }
                    }
                    else if(j==classInterval.length-1){
                        if(Double.parseDouble(classList.get(i).getData())>=classInterval[j]){
                            classObject.put("color",colors[j+1]);
                            int number = j+2;
                            String classRGN = "第" + number + "级";
                            classObjectAttr.put("rgn_class",classRGN);
                        }
                    }
                    else {
                        if(Double.parseDouble(classList.get(i).getData())>=classInterval[j] && Double.parseDouble(classList.get(i).getData())<classInterval[j+1]){
                            classObject.put("color",colors[j+1]);
                            int number = j+2;
                            String classRGN = "第" + number + "级";
                            classObjectAttr.put("rgn_class",classRGN);
                        }
                    }
                }
            }

            classObject.put("attributes",classObjectAttr);
            classObject.put("geometry",classList.get(i).getGeometry());
//            if(Double.parseDouble(classList.get(i).getData())<classInterval[0]){
//                classObject.put("color",colors[0]);
//            }
//            else if(Double.parseDouble(classList.get(i).getData())>=classInterval[0] && Double.parseDouble(classList.get(i).getData())<classInterval[1]){
//                classObject.put("color",colors[1]);
//            }
//            else {
//                classObject.put("color",colors[2]);
//            }
            classDataArray.add(classObject);
        }
        return classDataArray;
    }

    public JSONArray classiFiter4(ArrayList<ClassData> classList,String[] colors,double[] classInterval){
        JSONArray classDataArray = new JSONArray();
        for (int i=0;i<classList.size();i++){
            JSONObject classObject = new JSONObject();
            JSONObject classObjectAttr = new JSONObject();
            classObjectAttr.put("rgn_code",classList.get(i).getCode());
            classObjectAttr.put("rgn_name",classList.get(i).getName());
            classObjectAttr.put("data",classList.get(i).getData());
            classObjectAttr.put("centerX",classList.get(i).getX());
            classObjectAttr.put("centerY",classList.get(i).getY());
            classObject.put("attributes",classObjectAttr);
            classObject.put("geometry",classList.get(i).getGeometry());
            if(Double.parseDouble(classList.get(i).getData())<classInterval[0]){
                classObject.put("color",colors[0]);
            }
            else if(Double.parseDouble(classList.get(i).getData())>=classInterval[0] && Double.parseDouble(classList.get(i).getData())<classInterval[1]){
                classObject.put("color",colors[1]);
            }
            else if(Double.parseDouble(classList.get(i).getData())>=classInterval[1] &&Double.parseDouble(classList.get(i).getData())<classInterval[2]){
                classObject.put("color",colors[2]);
            }
            else {
                classObject.put("color",colors[3]);
            }
            classDataArray.add(classObject);
        }
        return classDataArray;
    }
}
