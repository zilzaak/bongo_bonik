package app.common.entity;

public enum ProductPattern {
    BOX_PACK_SINGLE("BOX>PACKET>SINGLE_ITEM"),
    BOX_SINGLE("BOX>SINGLE_ITEM"),
    SINGLE("SINGLE_ITEM"),
    PACK_SINGLE("PACKET>SINGLE_ITEM");
    private final String key;
    ProductPattern(String key) {
        this.key = key;
    }
    public String value() {
        return key;
    }
}
