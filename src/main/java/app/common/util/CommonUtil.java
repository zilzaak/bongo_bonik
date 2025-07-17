package app.common.util;

import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.common.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.regex.Pattern;


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

    public static List<String> prdctTypes = Arrays.asList("BARCODED_PRODUCT","NORMAL_PRODUCT");

    public static String removeHeadTailSpace(String input){
        if(input==null){
            return null;
        }
        input = input.trim();
        return input;
    }

    public static String removeAllSpace(String input){
        if(input==null){
            return null;
        }
         input = input.replaceAll("\\s+", "");  //this also remove head and tails space from string
        return input;
    }

    //cat,brand,model,
    // madeWith,size,color,dto.getQtyPerUnit(),dto.getQtyUnit(),uom)

    public static String replaceRepeatedChar(String input, char ch) {
        if(input==null){
            return null;
        }
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


    public static Map<String,Object> counterAttribute(String counter){
        Map<String,Object> attr = new HashMap<>();
        if(counter==null || counter.isBlank()){
            return attr;
        }

        if(counter.equals(CounterEnum.INVOICE.name())){
            attr.put("name","INVOICE_COUNTER");
            attr.put("prefix","INV-");
        }
        else if(counter.equals(CounterEnum.PURCHASE.name())){
            attr.put("name","PURCHASE_COUNTER");
            attr.put("prefix","PRCHS-");
        }

        else if(counter.equals(CounterEnum.BARCODE.name())){
            attr.put("name","BARCODE_COUNTER");
            attr.put("prefix","sl-");
        }

        return attr;
    }

    public static Pageable getPageable(SearchParamDTO dto){
        Sort sort = dto.sortDir.equals("asc")?Sort.by(dto.sortField).ascending():Sort.by(dto.sortField).descending();
        Pageable pageable = PageRequest.of(dto.pageNum-1,dto.pageSize,sort);
        return pageable;

    }



    public static MsgResponse responseFromPage(Page<Map<String,Object>> page){
        MsgResponse response = new MsgResponse();
        response.setMessage("data retrived");
        response.setSuccess(true);
        Map<String,Object> mp = new HashMap<>();
        mp.put("listData",page.getContent());
        mp.put("totalPages",page.getTotalPages());
        mp.put("totalItems",page.getTotalElements());
        mp.put("pageNum",page.getNumber());
        mp.put("pageSize",page.getSize());
        response.setData(mp);
        return response;
    }


    public static MsgResponse responseFromObjectPage(Page<Object> page){
        MsgResponse response = new MsgResponse();
        response.setMessage("data retrived");
        response.setSuccess(true);
        Map<String,Object> mp = new HashMap<>();
        mp.put("listData",page.getContent());
        mp.put("totalPages",page.getTotalPages());
        mp.put("totalItems",page.getTotalElements());
        mp.put("pageNum",page.getNumber());
        mp.put("pageSize",page.getSize());
        response.setData(mp);
        return response;
    }

    public static String removeCharFromString(String input , char ch){
        String result = input.chars()
                .filter(c -> c != ch)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
              return result;
    }

    public static String removeWordFromString(String stringSeq , String wordToRemove){
        if(wordToRemove==null){
            return stringSeq;
        }
         String updatedString = stringSeq.replace(wordToRemove, "").trim();
         return updatedString;
    }

    public static String removeLastCharacter(String str){
        String result = str.substring(0, str.length() - 1);
       return  result;
    }

    public static String removeFirstChar(String str) {
        String result =  (str == null || str.length() < 2) ? str : str.substring(1);
        return  result;
    }

    public static List<String> permitAllList=Arrays.asList("/auth/getToken");

}
