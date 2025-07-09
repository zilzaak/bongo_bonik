package app.modules.base.moduleInfo.entity;

public enum HeirarchyType {

    MODULE("MODULE"),
    SUB_MODULE("SUB_MODULE"),
    MENU("MENU");

    private final String type;

     HeirarchyType(String type){
        this.type=type;
    }

    public String getValue(){
         return this.type;
    }


}
