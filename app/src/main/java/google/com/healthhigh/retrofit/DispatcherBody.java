package google.com.healthhigh.retrofit;

import java.util.Map;
import java.util.TreeMap;

public class DispatcherBody<R> {
    public String acao;
    public Map<String, R> params;

    public DispatcherBody() {
        params = new TreeMap<>();
    }
}
