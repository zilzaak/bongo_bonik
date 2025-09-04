package app.common.util;

public enum CounterEnum {
    PURCHASE("PURCHASE"),
    INVOICE("INVOICE"),
    BARCODE("BARCODE"),
    PRODUCT("PRODUCT"),
    SYS_USER("SYS_USER");
    private final String counterCode;

    CounterEnum(String counterCode){
        this.counterCode=counterCode;
    }
    public String getValue(){
        return counterCode;
    }
}
