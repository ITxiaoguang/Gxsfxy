package top.yokey.gxsfxy.utility;

import android.text.Editable;
import android.text.Html;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {

    //作用：判断是不是 JSON 数据
    public static boolean isJson(String string) {

        if (!isEmpty(string)) {
            if (string.contains("{") && string.contains("}")) {
                return true;
            }
        }

        return false;

    }

    //作用：判断为不为空
    public static boolean isEmpty(String string) {

        return string == null
                || string.isEmpty()
                || string.length() == 0
                || string.equals("")
                || string.equals("null");

    }

    //作用：判断是不是一个URL
    public static boolean isUrlAddress(String url) {

        return url.contains("http") || url.contains("www.") || url.contains(".com") || url.contains(".cn");

    }

    //作用：判断是不是一个邮箱地址
    public static boolean isEmailAddress(String string) {

        String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(string);
        return matcher.matches();

    }

    //作用：判断是不是一个电话号码
    public static boolean isMobileNumber(String mobile) {

        String reg = "^((13[0-9])|(15[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(mobile);
        return m.matches();

    }

    //作用：转换成适合手机阅读的HTML
    public static String encodeHtml(String string) {

        string = "<html><head><style type='text/css'>body{font-family:'Microsoft YaHei';padding:8px}p,img,table,embed,input,dl,dd,tr,td{width:100%;padding:0px;margin:0px;color:#333333;line-height:28px;}</style></head><body>" + string + "</body></html>";
        return string;

    }

    //作用：替换HTML表情
    public static String replaceFace(Editable editable) {

        return Html.toHtml(editable)
                .replace("<imgsrc", "<img src")
                .replace("<p dir=\"ltr\">", "")
                .replace("<p dir=ltr>", "")
                .replace("</p>", "")
                .replace("\n", "");

    }

    //作用：替换HTML表情
    public static String replaceKeyword(String string, String keyword) {

        if (!keyword.equals("e")) {
            return string.replace(keyword, "<font color='#115FB2'>" + keyword + "</font>");
        } else {
            return string;
        }

    }

    //作用：JSONObject 转 HashMap
    public static HashMap<String, String> jsonObjectToHashMap(String string) {

        try {
            HashMap<String, String> hashMap = new HashMap<>();
            JSONObject jsonObject = new JSONObject(string);
            Iterator iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next().toString();
                String value = jsonObject.getString(key);
                hashMap.put(key, value);
            }
            return hashMap;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    //作用：hashMap 转 JsonObject
    public static String hashMapToJsonObject(HashMap hashMap) {

        String json = "{";
        Iterator iterator = hashMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next().toString();
            String value = hashMap.get(key).toString();
            if (isEmpty(value)) {
                json = json + "\"" + key + "\":\"" + value + "\",";
            } else {
                if (value.substring(0, 1).equals("[") || value.substring(0, 1).equals("{")) {
                    json = json + "\"" + key + "\":" + value + ",";
                } else {
                    json = json + "\"" + key + "\":\"" + value + "\",";
                }
            }
        }
        json = json.substring(0, json.length() - 1);
        json += "}";
        return json;

    }

    //作用：图片内容转换
    public static Vector<String> encodeImage(String content) {
        Vector<String> vector = new Vector<>();
        if (!TextUtil.isEmpty(content)) {
            content = content.replace("[", "").replace("]", "").replace("\"", "").replace("\\", "");
            if (!content.contains(",")) {
                if (!TextUtil.isEmpty(content)) {
                    vector.add(content);
                }
            } else {
                while (content.contains(",")) {
                    String temp = content.substring(0, content.indexOf(","));
                    content = content.substring(content.indexOf(",") + 1, content.length());
                    vector.add(temp.replace(" ", ""));
                }
                if (!TextUtil.isEmpty(content)) {
                    vector.add(content.replace(" ", ""));
                }
            }
        }
        return vector;
    }

}