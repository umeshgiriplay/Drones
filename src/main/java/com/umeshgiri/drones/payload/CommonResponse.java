package com.umeshgiri.drones.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse {
    private CommonResponseStatus status;
    private String message;
    private Object object;
    private List objects;
    private List errors;

    public static CommonResponse success(Object object) {
        return CommonResponse.builder().status(CommonResponseStatus.SUCCESS).message("Success").object(object).build();
    }

    public static CommonResponse success() {
        return CommonResponse.builder().status(CommonResponseStatus.SUCCESS).message("Success").build();
    }
}
