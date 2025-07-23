package app.modules.sales.dto;

public enum PaymentMethod {

    CASH("CASH"),
    BANK("BANK"),
    CARD("CARD"),
    CHECK("CHECK"),
    BKASH("BKASH"),
    ROCKET("ROCKET");

    public final String payment;

    PaymentMethod(String x){
        this.payment=x;
    }
}
