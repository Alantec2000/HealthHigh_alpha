package google.com.healthhigh.retrofit.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServicosDesafio {
    @GET("ColetaDispatcher.php")
    public Call<ResponseBody> getDesafio(@Query("json_request") String json);

    @GET("desafios")
    public Call<ResponseBody> getRawDesafio();
}
