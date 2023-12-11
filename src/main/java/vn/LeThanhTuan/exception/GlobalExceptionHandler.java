package vn.LeThanhTuan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import vn.LeThanhTuan.response.ResponseObject;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseObject> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        String message = ex.getMessage();
        ResponseObject apiResponse = ResponseObject.builder()
                                    .code(HttpStatus.NOT_FOUND.value())
                                    .message(message)
                                    .data(null)
                                    .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseObject> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        ResponseObject apiResponse = ResponseObject.builder()
                                    .code(HttpStatus.EXPECTATION_FAILED.value())
                                    .message("The file upload is too large!")
                                    .data(null)
                                    .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ResponseObject> handleBaseException(BaseException e) {
        ResponseObject apiResponse = ResponseObject.builder()
                .code(e.getCode())
                .message(e.getMessage())
                .data(null)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}
