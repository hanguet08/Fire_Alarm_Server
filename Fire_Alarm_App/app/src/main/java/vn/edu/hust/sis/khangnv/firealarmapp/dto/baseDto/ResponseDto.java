package vn.edu.hust.sis.khangnv.firealarmapp.dto.baseDto;

// response from server
public class ResponseDto<T> {
    private String message;
    private String status;
    private T data;

    public ResponseDto(String message, String status, T data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }
}
