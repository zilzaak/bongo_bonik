package app.common.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MsgResponse {

   private String   message;
    private Object data;
    private boolean success;

    public MsgResponse(String message  , Object data , boolean success ){
        this.message = message;
        this.data = data;
        this.success = success;
    }

    public MsgResponse(Object data){
        this.data = data;
    }
    public MsgResponse(Object data, boolean success ){
        this.data = data;
        this.success = success;
    }
    public MsgResponse(String message, boolean success){
        this.message = message;
        this.success = success;
    }
    public MsgResponse(){

    }

}
