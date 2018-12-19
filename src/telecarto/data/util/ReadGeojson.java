package telecarto.data.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadGeojson {


    public static List<String> getPropertiesName() {
        return propertiesName;
    }

    public static void setPropertiesName(List<String> propertiesName) {
        ReadGeojson.propertiesName = propertiesName;
    }

    private static List<String> propertiesName;


    public  static void doReadGeojson(String url){
        String inputJson=getResultStrFromAPI(url);
        List<String> keys=new ArrayList<>();
        JSONObject myJson = JSONObject.fromObject(inputJson);

        JSONArray features = myJson.getJSONArray("features");
        JSONObject firstFeat = features.getJSONObject(0);
        JSONObject firstFeatPro = firstFeat.getJSONObject("properties");
        Iterator iterator = firstFeatPro.keys();

        while(iterator.hasNext()){
            String key = (String) iterator.next();
            keys.add(key);
            System.out.println(key);
            String value = firstFeatPro.getString(key);
            System.out.println(value);
        }
        propertiesName=keys;
    }
    public static void main(String[] args){
//        String jsonMessage = "{\n" +
//                "  \"type\": \"FeatureCollection\",\n" +
//                "  \"features\": [\n" +
//                "    {\n" +
//                "      \"type\": \"Feature\",\n" +
//                "      \"geometry\": {\n" +
//                "        \"type\": \"Point\",\n" +
//                "        \"coordinates\": [\n" +
//                "          119.52814,\n" +
//                "          29.879936\n" +
//                "        ]\n" +
//                "      },\n" +
//                "      \"properties\": {\n" +
//                "        \"市\": \"杭州市\",\n" +
//                "        \"隐患点\": 781,\n" +
//                "        \"避让搬迁\": 115,\n" +
//                "        \"工程治理\": 218\n" +
//                "      }\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"type\": \"Feature\",\n" +
//                "      \"geometry\": {\n" +
//                "        \"type\": \"Point\",\n" +
//                "        \"coordinates\": [\n" +
//                "          121.565296,\n" +
//                "          29.658292\n" +
//                "        ]\n" +
//                "      },\n" +
//                "      \"properties\": {\n" +
//                "        \"市\": \"宁波市\",\n" +
//                "        \"隐患点\": 276,\n" +
//                "        \"避让搬迁\": 43,\n" +
//                "        \"工程治理\": 37\n" +
//                "      }\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"type\": \"Feature\",\n" +
//                "      \"geometry\": {\n" +
//                "        \"type\": \"Point\",\n" +
//                "        \"coordinates\": [\n" +
//                "          120.438594,\n" +
//                "          27.882016\n" +
//                "        ]\n" +
//                "      },\n" +
//                "      \"properties\": {\n" +
//                "        \"市\": \"温州市\",\n" +
//                "        \"隐患点\": 1545,\n" +
//                "        \"避让搬迁\": 290,\n" +
//                "        \"工程治理\": 258\n" +
//                "      }\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"type\": \"Feature\",\n" +
//                "      \"geometry\": {\n" +
//                "        \"type\": \"Point\",\n" +
//                "        \"coordinates\": [\n" +
//                "          119.859578,\n" +
//                "          30.780919\n" +
//                "        ]\n" +
//                "      },\n" +
//                "      \"properties\": {\n" +
//                "        \"市\": \"湖州市\",\n" +
//                "        \"隐患点\": 160,\n" +
//                "        \"避让搬迁\": 45,\n" +
//                "        \"工程治理\": 62\n" +
//                "      }\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"type\": \"Feature\",\n" +
//                "      \"geometry\": {\n" +
//                "        \"type\": \"Point\",\n" +
//                "        \"coordinates\": [\n" +
//                "          120.556913,\n" +
//                "          29.7612356\n" +
//                "        ]\n" +
//                "      },\n" +
//                "      \"properties\": {\n" +
//                "        \"市\": \"绍兴市\",\n" +
//                "        \"隐患点\": 289,\n" +
//                "        \"避让搬迁\": 63,\n" +
//                "        \"工程治理\": 218\n" +
//                "      }\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"type\": \"Feature\",\n" +
//                "      \"geometry\": {\n" +
//                "        \"type\": \"Point\",\n" +
//                "        \"coordinates\": [\n" +
//                "          119.9997208,\n" +
//                "          29.1041079\n" +
//                "        ]\n" +
//                "      },\n" +
//                "      \"properties\": {\n" +
//                "        \"市\": \"金华市\",\n" +
//                "        \"隐患点\": 643,\n" +
//                "        \"避让搬迁\": 70,\n" +
//                "        \"工程治理\": 97\n" +
//                "      }\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"type\": \"Feature\",\n" +
//                "      \"geometry\": {\n" +
//                "        \"type\": \"Point\",\n" +
//                "        \"coordinates\": [\n" +
//                "          118.6833012,\n" +
//                "          28.8731117\n" +
//                "        ]\n" +
//                "      },\n" +
//                "      \"properties\": {\n" +
//                "        \"市\": \"衢州市\",\n" +
//                "        \"隐患点\": 616,\n" +
//                "        \"避让搬迁\": 141,\n" +
//                "        \"工程治理\": 102\n" +
//                "      }\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"type\": \"Feature\",\n" +
//                "      \"geometry\": {\n" +
//                "        \"type\": \"Point\",\n" +
//                "        \"coordinates\": [\n" +
//                "          122.225178,\n" +
//                "          30.238917\n" +
//                "        ]\n" +
//                "      },\n" +
//                "      \"properties\": {\n" +
//                "        \"市\": \"舟山市\",\n" +
//                "        \"隐患点\": 220,\n" +
//                "        \"避让搬迁\": 0,\n" +
//                "        \"工程治理\": 0\n" +
//                "      }\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"type\": \"Feature\",\n" +
//                "      \"geometry\": {\n" +
//                "        \"type\": \"Point\",\n" +
//                "        \"coordinates\": [\n" +
//                "          121.1135732,\n" +
//                "          28.683662\n" +
//                "        ]\n" +
//                "      },\n" +
//                "      \"properties\": {\n" +
//                "        \"市\": \"台州市\",\n" +
//                "        \"隐患点\": 330,\n" +
//                "        \"避让搬迁\": 51,\n" +
//                "        \"工程治理\": 141\n" +
//                "      }\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"type\": \"Feature\",\n" +
//                "      \"geometry\": {\n" +
//                "        \"type\": \"Point\",\n" +
//                "        \"coordinates\": [\n" +
//                "          119.5667332,\n" +
//                "          28.1894542\n" +
//                "        ]\n" +
//                "      },\n" +
//                "      \"properties\": {\n" +
//                "        \"市\": \"丽水市\",\n" +
//                "        \"隐患点\": 894,\n" +
//                "        \"避让搬迁\": 191,\n" +
//                "        \"工程治理\": 70\n" +
//                "      }\n" +
//                "    }\n" +
//                "  ]\n" +
//                "}";
        String initUrl="http://115.236.34.34:8668/api/v1/datasets/dizai/汇总-按市统计?year=2017&month=9";
        //String jsonMessage1=getResultStrFromAPI(initUrl);
        doReadGeojson(initUrl);


    }
    //输入url，返回解析的字符串
    public static String getResultStrFromAPI(String apiUrl){
        String resultString = "";
        String apiUrl2=chineseToUnicode(apiUrl);
        try{
//			String resultString="{" +
//					"  \"type\": \"FeatureCollection\"," +
//					"  \"features\": [" +
//					"    {" +
//					"      \"type\": \"Feature\"," +
//					"      \"geometry\": {" +
//					"        \"type\": \"Point\"," +
//					"        \"coordinates\": [" +
//					"          119.52814," +
//					"          29.879936" +
//					"        ]" +
//					"      }," +
//					"      \"properties\": {" +
//					"        \"市\": \"杭州市\"," +
//					"        \"隐患点\": 781.3," +
//					"        \"避让搬迁\": 115," +
//					"        \"工程治理\": 218" +
//					"      }" +
//					"    }," +
//					"    {" +
//					"      \"type\": \"Feature\"," +
//					"      \"geometry\": {" +
//					"        \"type\": \"Point\"," +
//					"        \"coordinates\": [" +
//					"          121.565296," +
//					"          29.658292" +
//					"        ]" +
//					"      }," +
//					"      \"properties\": {" +
//					"        \"市\": \"宁波市\"," +
//					"        \"隐患点\": 276," +
//					"        \"避让搬迁\": 43," +
//					"        \"工程治理\": 37" +
//					"      }" +
//					"    }," +
//					"    {" +
//					"      \"type\": \"Feature\"," +
//					"      \"geometry\": {" +
//					"        \"type\": \"Point\"," +
//					"        \"coordinates\": [" +
//					"          120.438594," +
//					"          27.882016" +
//					"        ]" +
//					"      }," +
//					"      \"properties\": {" +
//					"        \"市\": \"温州市\"," +
//					"        \"隐患点\": 1545," +
//					"        \"避让搬迁\": 290," +
//					"        \"工程治理\": 258" +
//					"      }" +
//					"    }," +
//					"    {" +
//					"      \"type\": \"Feature\"," +
//					"      \"geometry\": {" +
//					"        \"type\": \"Point\"," +
//					"        \"coordinates\": [" +
//					"          119.859578," +
//					"          30.780919" +
//					"        ]" +
//					"      }," +
//					"      \"properties\": {" +
//					"        \"市\": \"湖州市\"," +
//					"        \"隐患点\": 160," +
//					"        \"避让搬迁\": 45," +
//					"        \"工程治理\": 62" +
//					"      }" +
//					"    }," +
//					"    {" +
//					"      \"type\": \"Feature\"," +
//					"      \"geometry\": {" +
//					"        \"type\": \"Point\"," +
//					"        \"coordinates\": [" +
//					"          120.556913," +
//					"          29.7612356" +
//					"        ]" +
//					"      }," +
//					"      \"properties\": {" +
//					"        \"市\": \"绍兴市\"," +
//					"        \"隐患点\": 289," +
//					"        \"避让搬迁\": 63," +
//					"        \"工程治理\": 218" +
//					"      }" +
//					"    }," +
//					"    {" +
//					"      \"type\": \"Feature\"," +
//					"      \"geometry\": {" +
//					"        \"type\": \"Point\"," +
//					"        \"coordinates\": [" +
//					"          119.9997208," +
//					"          29.1041079" +
//					"        ]" +
//					"      }," +
//					"      \"properties\": {" +
//					"        \"市\": \"金华市\"," +
//					"        \"隐患点\": 643," +
//					"        \"避让搬迁\": 70," +
//					"        \"工程治理\": 97" +
//					"      }" +
//					"    }," +
//					"    {" +
//					"      \"type\": \"Feature\"," +
//					"      \"geometry\": {" +
//					"        \"type\": \"Point\"," +
//					"        \"coordinates\": [" +
//					"          118.6833012," +
//					"          28.8731117" +
//					"        ]" +
//					"      }," +
//					"      \"properties\": {" +
//					"        \"市\": \"衢州市\"," +
//					"        \"隐患点\": 616," +
//					"        \"避让搬迁\": 141," +
//					"        \"工程治理\": 102" +
//					"      }" +
//					"    }," +
//					"    {" +
//					"      \"type\": \"Feature\"," +
//					"      \"geometry\": {" +
//					"        \"type\": \"Point\"," +
//					"        \"coordinates\": [" +
//					"          122.225178," +
//					"          30.238917" +
//					"        ]" +
//					"      }," +
//					"      \"properties\": {" +
//					"        \"市\": \"舟山市\"," +
//					"        \"隐患点\": 220," +
//					"        \"避让搬迁\": 0," +
//					"        \"工程治理\": 0" +
//					"      }" +
//					"    }," +
//					"    {" +
//					"      \"type\": \"Feature\"," +
//					"      \"geometry\": {" +
//					"        \"type\": \"Point\"," +
//					"        \"coordinates\": [" +
//					"          121.1135732," +
//					"          28.683662" +
//					"        ]" +
//					"      }," +
//					"      \"properties\": {" +
//					"        \"市\": \"台州市\"," +
//					"        \"隐患点\": 330," +
//					"        \"避让搬迁\": 51," +
//					"        \"工程治理\": 141" +
//					"      }" +
//					"    }," +
//					"    {" +
//					"      \"type\": \"Feature\"," +
//					"      \"geometry\": {" +
//					"        \"type\": \"Point\"," +
//					"        \"coordinates\": [" +
//					"          119.5667332," +
//					"          28.1894542" +
//					"        ]" +
//					"      }," +
//					"      \"properties\": {" +
//					"        \"市\": \"丽水市\"," +
//					"        \"隐患点\": 894," +
//					"        \"避让搬迁\": 191," +
//					"        \"工程治理\": 70" +
//					"      }" +
//					"    }" +
//					"  ]" +
//					"}";
            String str = URLEncoder.encode("汇总-按市统计","utf-8");
            URL url = new URL(apiUrl2);
            //URL url = new URL("http://115.236.34.34:8668/api/v1/datasets/dizai/"+str+"?year=2017&month=9");
            //URL url = new URL("http://www.baidu.com");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            urlConnection.setRequestProperty("ContentType","application/json;charset=utf-8");
            // System.out.println(urlConnection.getInputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));

            String lines;
            while((lines = in.readLine()) != null)
            {
                //lines = new String(lines.getBytes());
                resultString += lines;
            }
            System.out.println(resultString);

        }catch( Exception e)
        {
            e.printStackTrace();
        }
        return resultString;

    };
    public static String chineseToUnicode(String url) {
//        String result = "";
//        for (int i = 0; i < str.length(); i++) {
//            int chr1 = (char) str.charAt(i);
//            if (chr1 >= 19968 && chr1 <= 171941) {//汉字范围 \u4e00-\u9fa5 (中文)
//                result += "\\u" + Integer.toHexString(chr1);
//            } else {
//                result += str.charAt(i);
//            }
//        }
//        return result;
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]");
        //找到中文url中的中文
        Matcher m = p.matcher(url);
        //依次递推，查找下一个单个文字，然后把他替换成utf-8
        while(m.find()){
            String group = m.group();
            try {
                url =  url.replaceFirst(group, URLEncoder.encode(group, "utf-8"));
            } catch (Exception e) {
                // do nothing
            }
        }
        return url;
    }
}
