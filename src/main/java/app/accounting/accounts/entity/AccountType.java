package app.accounting.accounts.entity;

public enum AccountType {
    ASSETS("ASSETS"),
    //CURRENT_ASSET(CASH,BANK,INVENTORY,ACC_RECEIVABLE,VAT_INPUT,BANK_INTEREST),
    //FIXED_ASSET(FURNITURE,OTHERS)
    LIABILITIES("LIABILITIES"),
    //ACC_PAYABLE,LOAN_PAYABLE,VAT_OUTPUT
    WITHDRAWAL("WITHDRAWAL"),
    INVEST("INVEST"),
    INCOME("INCOME"),
    //SALES_REVENUE,SALES_RETURN,DISCOUNT_RECEIVED,BANK_INTEREST
    EXPENSE("EXPENSE") ;
    //COGS,RENT,DISCOUNT_GIVEN,LOAN_INTEREST_EXPENSE,SALARIES,DEPRECIATION,UTILITIES

    private final String type;

    AccountType(String type){
        this.type=type;
    }
    public String getType(){
        return this.type;
    }

}
