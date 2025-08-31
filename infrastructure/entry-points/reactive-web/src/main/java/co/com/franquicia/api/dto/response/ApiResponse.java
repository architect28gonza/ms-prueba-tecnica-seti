package co.com.franquicia.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private Header header;
    private T body;

    @Data
    @AllArgsConstructor
    public static class Header {
        private String message;
        private Integer code;
    }
} 