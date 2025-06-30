package app.accounting.accounts;

public enum AccountType {

    ASSETS("ASSETS"),              // CURRENT_ASSET(CASH,BANK ,INVENTORY,ACC_RECIEVABLE ),FIXED_ASSET(FURNITURE,VEHICLE,PERSONAL_ASSET)
    CONTRA_ASSETS("CONTRA_ASSETS"),
    LIABILITIES("LIABILITIES"),    // ACC_PAYABLE , LOANS
    EQUITY("EQUITY"),              // WITHDRAWAL , CAPITAL,  OWNERS_CAPITAL
    REVENUE("REVENUE"),            // SALES , OTHER_INCOME
    EXPENSE("EXPENSE"),            // RENT , SALARIES , OFFICE_SUPPLY , DEPRECIATION
    COGS("COGS");                  // COST OF PRODUCT

    private final String type;

    AccountType(String type){
        this.type=type;
    }

    public String getType(){
        return this.type;
    }

}
