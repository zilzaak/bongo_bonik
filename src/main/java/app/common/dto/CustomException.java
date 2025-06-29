package app.common.dto;

public class CustomException extends Exception{
   private  String message;

   public CustomException(String message){
       this.message=message;
   }

}
