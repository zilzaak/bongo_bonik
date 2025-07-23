package app.modules.sales.dto;

public enum SaleType {
    CASH("CASH"),
    DUE("DUE"),
    INSTALLMENT("INSTALLMENT");
    public final String payment;
    SaleType(String x){
        this.payment=x;
    }
}
