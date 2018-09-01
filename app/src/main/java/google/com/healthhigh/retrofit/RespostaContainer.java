package google.com.healthhigh.retrofit;

public class RespostaContainer<T> {
    private int code;
    private String message;
    private T content;

    public T getContent() {
        return content;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
