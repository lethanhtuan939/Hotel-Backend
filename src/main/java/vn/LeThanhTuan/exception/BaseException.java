package vn.LeThanhTuan.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseException extends RuntimeException{

    private int code;

    private String message;
}
