package vn.LeThanhTuan.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseObject {
    private int code;
    private String message;
    private Object data;
    private int totalPages;
    private int currentPage;
    private int pageSize;
    private String keyword;
}
