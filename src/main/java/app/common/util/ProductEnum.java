package app.common.util;

public enum ProductEnum {

    BARCODED_PRODUCT("BARCODED_PRODUCT"),
    NORMAL_PRODUCT("NORMAL_PRODUCT");

    private final String productType;

    ProductEnum(String productType){
        this.productType=productType;
    }

    public String getType(){
        return productType;
    }
}
