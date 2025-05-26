package app.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommonUtil {


    public static List<String> bulkStrToList(String bulkStr){
        List<String> list = new ArrayList<>();
        if(bulkStr==null || bulkStr.trim().isEmpty()){
            return list;
        }
        String [] arr = bulkStr.split(",");
        list = Arrays.asList(arr);
        return list;
    }


    public static List<Long> strListToLong(List<String> list){
        List<Long> longLst = new ArrayList<>();
        for(String str : list){
            longLst.add(Long.parseLong(str));
        }
        return longLst;
    }

}
