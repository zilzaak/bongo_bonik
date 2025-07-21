package app.accounting.accounts;

public enum AccountType {

    ASSETS("ASSETS"),              // CURRENT_ASSET(CASH,BANK ,INVENTORY,ACC_RECEIVABLE), FIXED_ASSET(FURNITURE,VEHICLE,PERSONAL_ASSET),VAT_INPUT
    LIABILITIES("LIABILITIES"),    // ACC_PAYABLE,LOANS,SALARY_PAYABLE,VAT_OUTPUT
    EQUITY("EQUITY"),              // WITHDRAWAL,OWNERS_CAPITAL,RETAINED_EARNINGS(=REVENUE-EXPENSE)
    INCOME("INCOME"),              // SALES_REVENUE,SERVICE_REVENUE ,DISCOUNT_REVENUE,DISCOUNT_RECEIVED
    EXPENSE("EXPENSE") ;           // COGS,RENT,SALARIES,OFFICE_SUPPLY,DEPRECIATION,UTILITIES,DISCOUNT_GIVEN


    private final String type;

    AccountType(String type){
        this.type=type;
    }

    public String getType(){
        return this.type;
    }

}
