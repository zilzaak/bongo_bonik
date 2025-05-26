package app.common.dto;


import lombok.Data;

@Data
public class CommonDTO {

    private Long id;
    private String name;
    private Long orgId;
    private String orgName;
    private String entity;

}
