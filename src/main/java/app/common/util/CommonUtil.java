package app.common.util;

import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.common.entity.*;
import app.modules.base.org.entity.Organization;
import app.modules.base.user.repo.UserOrgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Security;
import java.util.*;
import java.util.regex.Pattern;

@Component
public class CommonUtil {

    public static UserOrgRepository userOrgRepository;

    public CommonUtil(UserOrgRepository userOrgRepository) {
        CommonUtil.userOrgRepository = userOrgRepository;
    }

    public static boolean validUserOrg(Long orgId){
        String username=  SecurityContextHolder.getContext().getAuthentication().getName();
        if(username==null){
            return false;
        }
        int x =  userOrgRepository.countByOrgIdAndUserUsername(orgId,username);
        return x>0;
    }


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
        if(input==null || input.isBlank()){
            return null;
        }
        input = input.trim();
        return input;
    }

    public static String removeAllSpace(String input){
        if(input==null || input.isBlank()){
            return null;
        }
         input = input.replaceAll("\\s+", "");  //this also remove head and tails space from string
        return input;
    }

    public static boolean isLastChar(String input,char ch){
        if(input==null || input.isBlank()){
            return false;
        }
        char lastCharacter = input.charAt(input.length()-1);
        if(lastCharacter==ch){
            return true;
        }
        return false;
    }

    //cat,brand,model,
    // madeWith,size,color,dto.getQtyPerUnit(),dto.getQtyUnit(),uom)

    public static String replaceRepeatedChar(String input, char ch) {
        if(input==null || input.isBlank()){
            return null;
        }
        String regex = Pattern.quote(Character.toString(ch)) + "+";
        return input.replaceAll(regex, Character.toString(ch));
    }

    //full name = name->Cat->brand->model->madeWith->size->color->amount per unit->measure by
    public static Map<String,Object> getProductFullname(String rootName , ProductCat cat, Brand brand, ProductModel model,
                                            MadeWith madeWith,ProductSize size,ProductColor color,
                                            Integer qtyPerUnit,String qtyUnit){
        Map<String,Object> mp=new HashMap<>();
        String fullName=rootName+">"+cat.getName()+">"+Optional.ofNullable(brand).map(Brand::getName).orElse("")+">"+
                Optional.ofNullable(model).map(ProductModel::getName).orElse("")+">"+
                Optional.ofNullable(madeWith).map(MadeWith::getName).orElse("")+">"+
                Optional.ofNullable(size).map(ProductSize::getName).orElse("")+">"+
                Optional.ofNullable(color).map(ProductColor::getName).orElse("")+">";

                 if(qtyPerUnit!=null){
                     fullName=fullName+qtyPerUnit+">";
                 }
                 if(qtyUnit!=null){
                     fullName=fullName+qtyUnit;
                 }

            fullName = replaceRepeatedChar(fullName,'>');
            fullName = replaceRepeatedChar(fullName,' ');  //replace double white space or blank space

        mp.put("fullName",fullName);

        String criteriaIds=cat.getId()+","+Optional.ofNullable(brand).map(Brand::getId).map(Object::toString).orElse("")+","+
                Optional.ofNullable(model).map(ProductModel::getId).map(Object::toString).orElse("")+","+
                Optional.ofNullable(madeWith).map(MadeWith::getId).map(Object::toString).orElse("")+","+
                Optional.ofNullable(size).map(ProductSize::getId).map(Object::toString).orElse("")+","+
                Optional.ofNullable(color).map(ProductColor::getId).map(Object::toString).orElse("")+",";

        if(qtyPerUnit!=null){
            criteriaIds=criteriaIds+qtyPerUnit+",";
        }
        if(qtyUnit!=null){
            criteriaIds=criteriaIds+qtyUnit+",";
        }

        criteriaIds = replaceRepeatedChar(criteriaIds,',');
        criteriaIds = replaceRepeatedChar(criteriaIds,' ');  //replace double white space or blank space
        mp.put("criteriaIds",criteriaIds);
        return mp;
    }


    public static List<Long> strListToLong(List<String> list){
        List<Long> longLst = new ArrayList<>();
        for(String str : list){
            longLst.add(Long.parseLong(str));
        }
        return longLst;
    }

    public static List<Long> reverseOrderList(List<Long> list){
        List<Long> result = new ArrayList<>();
        for(int i=list.size()-1 ; i>=0 ; i--){
            result.add(list.get(i));
        }
        return result;
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
        else if(counter.equals(CounterEnum.BARCODE.name())){
            attr.put("name","BARCODE_COUNTER");
            attr.put("prefix","sl-");
        }
        else if(counter.equals(CounterEnum.SYS_USER.name())){
            attr.put("name",CounterEnum.SYS_USER.name());
            attr.put("prefix","");
        }

        return attr;
    }

    public static Pageable getPageable(SearchParamDTO dto){
        Sort sort = dto.sortDir.equals("asc")?Sort.by(dto.sortField).ascending():Sort.by(dto.sortField).descending();
        Pageable pageable = PageRequest.of(dto.pageNum-1,dto.pageSize,sort);
        return pageable;

    }

    public static String replaceWord(String str, String repWord,String byWord){
        String finalStr = str.replace(repWord,byWord);
        return finalStr;
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
        if(str==null){
            return str;
        }
        String result = str.substring(0, str.length() - 1);
       return  result;
    }

    public static String removeFirstChar(String str) {

        String result =  (str == null || str.length() < 2) ? str : str.substring(1);
        return  result;
    }

    public static String currentUser(){
        return  SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public static List<String> permitAllList=Arrays.asList("/auth/getToken");


}
