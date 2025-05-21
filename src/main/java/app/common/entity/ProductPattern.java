package app.common.entity;

public enum ProductPattern {

    BOX_PACK_SINGLE("BOX_PACK_SINGLE"),
    BOX_SINGLE("BOX_SINGLE"),
    SINGLE("SINGLE"),
    PACK_SINGLE("PACK_SINGLE");

    private final String key;

    ProductPattern(String key) {
        this.key = key;
    }

    public String value() {
        return key;
    }

}
