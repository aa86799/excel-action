package lune.readxmlUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lune.bean.JsonBean;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WriteToXml {
    private static final String STR_JSON = "{\"user_hint\":\"test\",\"password_hint\":\"ป้อนรหัสผ่าน\"}";
    private static List<JsonBean> bizDataList;
    private static List<String> bizStringList = new ArrayList<>();

    private static List<JsonBean> deliveryDataList;
    private static List<String> deliveryStringList = new ArrayList<>();

    private static List<JsonBean> uiDataList;
    private static List<String> uiStringList = new ArrayList<>();

    private static List<JsonBean> widgetDataList;
    private static List<String> widgetStringList = new ArrayList<>();

    private static List<JsonBean> cameraDataList;
    private static List<String> cameraStringList = new ArrayList<>();


    /**
     * 将json字符串转换成xml
     *
     * @param json
     *            json字符串
     * @param parentElement
     *            xml根节点
     * @throws Exception
     */
    public static Element jsonToXml(String json, Element parentElement) throws Exception {
        bizDataList = new ReadXmlBySAX().getBooks("src/res/in/bizstrings.xml");
        bizStringList.clear();
        for (int i = 0; i < bizDataList.size(); i++) {
            bizStringList.add(bizDataList.get(i).getKey());
        }

//        deliveryDataList = new ReadXmlBySAX().getBooks("src/res/in/deliverystrings.xml");
//        deliveryStringList.clear();
//        for (int i = 0; i < deliveryDataList.size(); i++) {
//            deliveryStringList.add(deliveryDataList.get(i).getKey());
//        }
//
//        uiDataList = new ReadXmlBySAX().getBooks("src/res/in/uistrings.xml");
//        uiStringList.clear();
//        for (int i = 0; i < uiDataList.size(); i++) {
//            uiStringList.add(uiDataList.get(i).getKey());
//        }
//
//        widgetDataList = new ReadXmlBySAX().getBooks("src/res/in/widgetstrings.xml");
//        widgetStringList.clear();
//        for (int i = 0; i < widgetDataList.size(); i++) {
//            widgetStringList.add(widgetDataList.get(i).getKey());
//        }
//
//        cameraDataList = new ReadXmlBySAX().getBooks("src/res/in/camera_strings.xml");
//        cameraStringList.clear();
//        for (int i = 0; i < cameraDataList.size(); i++) {
//            cameraStringList.add(cameraDataList.get(i).getKey());
//        }
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        System.out.println(jsonObject.toString());
        Element ee = toXml(jsonObject, parentElement, null);
        return ee;
    }

    /**
     * 将json字符串转换成xml
     *
     * @param jsonElement
     *            待解析json对象元素
     * @param parentElement
     *            上一层xml的dom对象
     * @param name
     *            父节点
     */
    public static Element toXml(JsonElement jsonElement, Element parentElement, String name) {
        if (jsonElement instanceof JsonArray) {
            //是json数据，需继续解析
            JsonArray sonJsonArray = (JsonArray)jsonElement;
            for (int i = 0; i < sonJsonArray.size(); i++) {
                JsonElement arrayElement = sonJsonArray.get(i);
                toXml(arrayElement, parentElement, name);
            }
        }else if (jsonElement instanceof JsonObject) {
            //说明是一个json对象字符串，需要继续解析
            JsonObject sonJsonObject = (JsonObject) jsonElement;
            Element currentElement = null;
            if (name != null) {
                currentElement = parentElement.addElement(name);
            }
            Set<Map.Entry<String, JsonElement>> set = sonJsonObject.entrySet();
            for (Map.Entry<String, JsonElement> s : set) {
                if (bizStringList.contains(s.getKey())){
                    toXml(s.getValue(), currentElement != null ? currentElement : parentElement, s.getKey());
                }

//                if (cameraStringList.contains(s.getKey())){
//                    toXml(s.getValue(), currentElement != null ? currentElement : parentElement, s.getKey());
//                }
//
//                if (deliveryStringList.contains(s.getKey())){
//                    toXml(s.getValue(), currentElement != null ? currentElement : parentElement, s.getKey());
//                }
//
//                if (uiStringList.contains(s.getKey())){
//                    toXml(s.getValue(), currentElement != null ? currentElement : parentElement, s.getKey());
//                }
//
//                if (widgetStringList.contains(s.getKey())){
//                    toXml(s.getValue(), currentElement != null ? currentElement : parentElement, s.getKey());
//                }
            }
        } else {
            //说明是一个键值对的key,可以作为节点插入了
            addAttribute(parentElement, name, jsonElement.getAsString());
        }
        return parentElement;
    }


    /**
     *
     * @param element  	父节点
     * @param name		子节点的名字
     * @param value		子节点的值
     */
    public static void addAttribute(Element element, String name, String value) {
        //增加子节点，并为子节点赋值
        Element el = element.addElement("string");
        el.addAttribute("name",name);
        el.addText(value);

    }


    public static void main(String[] args) throws Exception {
//        deliveryDataList = new ReadXmlBySAX().getBooks("src/res/in/deliverystrings.xml");
//        uiDataList = new ReadXmlBySAX().getBooks("src/res/in/uistrings.xml");
//        widgetDataList = new ReadXmlBySAX().getBooks("src/res/in/widgetstrings.xml");
//        cameraDataList = new ReadXmlBySAX().getBooks("src/res/in/camera_strings.xml");
        Document document = DocumentHelper.createDocument();
        Element root =  document.addElement("resources"); //默认根节点

        Element el = jsonToXml(STR_JSON, root);
        System.out.println(el.asXML());
        try {
            //生成xml文件
            String fileName = "strings.xml";
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("utf-8"); // 指定XML编码
            format.setExpandEmptyElements(true);//自动添加闭合标签
            document.setXMLEncoding("utf-8");
            //指定文件路径，名字，格式
            XMLWriter writer = new XMLWriter(
                    new FileOutputStream(new File("src/res/output/" + File.separator + fileName)), format);
            writer.write(document);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
