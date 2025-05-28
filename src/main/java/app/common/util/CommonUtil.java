package app.common.util;

import app.common.entity.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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


    public static String removeAllBlankSpace(String input){
         input = input.replaceAll("\\s+", "");  //this also remove head and tails space from string
        return input;
    }

    //cat,brand,model,
    // madeWith,size,color,dto.getQtyPerUnit(),dto.getQtyUnit(),uom)

    public static String replaceRepeatedChar(String input, char ch) {
        String regex = Pattern.quote(Character.toString(ch)) + "+";
        return input.replaceAll(regex, Character.toString(ch));
    }

    public static String getProductFullname(String rootName , ProductCat cat, Brand brand, ProductModel model,
                                            MadeWith madeWith,ProductSize size,ProductColor color,
                                            Integer qtyPerUnit,String qtyUnit,UnitOfMeasure uom){
        String fullName=rootName+">"+cat.getName()+">"+brand.getName()+">"+
                Optional.ofNullable(model).map(ProductModel::getName).orElse("")+">"+
                Optional.ofNullable(madeWith).map(MadeWith::getName).orElse("")+">"+
                Optional.ofNullable(size).map(ProductSize::getName).orElse("")+">"+
                Optional.ofNullable(color).map(ProductColor::getName).orElse("")+">";

                 if(qtyPerUnit!=null){
                     fullName=fullName+qtyPerUnit+">";
                 }
                 if(qtyUnit!=null){
                     fullName=fullName+qtyUnit+">";
                 }
                 if(uom!=null){
                     fullName=fullName+uom.getName().toLowerCase();
                 }

            fullName = replaceRepeatedChar(fullName,'>');
            fullName = replaceRepeatedChar(fullName,' ');  //replace double white space or blank space
        return fullName;
    }


    public static List<Long> strListToLong(List<String> list){
        List<Long> longLst = new ArrayList<>();
        for(String str : list){
            longLst.add(Long.parseLong(str));
        }
        return longLst;
    }

}
