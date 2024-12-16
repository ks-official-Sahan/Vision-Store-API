package dto.response;

import java.io.Serializable;
import java.util.List;

public class ResponseDTO<T> implements Serializable {

    private boolean status;
    private String message;
    private String url;
    private List<T> dataList;
    private T data;
    private int code = 200;
    private String target;
    private String error;

    public ResponseDTO() {
    }

    public ResponseDTO(boolean status, String message, String url, List<T> dataList, T data, int code) {
        this.status = status;
        this.message = message;
        this.url = url;
        this.dataList = dataList;
        this.data = data;
        this.code = code;
    }

    public ResponseDTO(boolean status, String message, String url, List<T> dataList, T data, String target, String error) {
        this.status = status;
        this.message = message;
        this.url = url;
        this.dataList = dataList;
        this.data = data;
        this.target = target;
        this.error = error;
    }

    
    
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
    
}
