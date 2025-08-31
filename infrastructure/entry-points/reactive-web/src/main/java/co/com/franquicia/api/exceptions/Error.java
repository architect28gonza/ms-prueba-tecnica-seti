package co.com.franquicia.api.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Error<T> {
    private Header header;
    private T body;

    @Data
    @AllArgsConstructor
    public static class Header {
        private String message;
        private Integer code;
    }
}