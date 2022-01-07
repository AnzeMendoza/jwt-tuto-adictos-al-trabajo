package com.ssubijana.roleauthorization.domain;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class ResponseInfoJwtDTO {
    private String timestap;

    private int code;

    private String path;

    private String message;

    public ResponseInfoJwtDTO(HttpServletResponse response) {
        this.timestap = new Date().toString();
        this.message = "Se creo el jwt correctamente";
        this.code = response.getStatus();
        this.path = "localhost:8080/api/v1/auth";
    }

    public static String Create(HttpServletResponse response){
        return new ResponseInfoJwtDTO(response).toString();
    }

}
