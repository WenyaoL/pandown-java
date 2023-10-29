package com.pan.pandown.util;

import java.util.*;

public class CookieUtils {

    public static List<String> mapToCookieList(Map<String,String> map){
        ArrayList<String> arrayList = new ArrayList<>();
        map.forEach((k,v)->{
            arrayList.add(k + "=" + v);
        });
        return arrayList;
    }

    public static Map<String,String> strToCookieMap(String cookieStr){
        if (Objects.isNull(cookieStr))return null;
        String[] split = cookieStr.split(";");
        HashMap<String, String> result = new HashMap<>();
        Arrays.stream(split).forEach(str->{
            String[] keyAndValue = str.trim().split("=",2);
            if (keyAndValue.length==1){
                result.put(keyAndValue[0],null);
            } else {
                String key = keyAndValue[0];
                String value = keyAndValue[1];
                result.put(key,value);
            }

        });

        return result;
    }

}
