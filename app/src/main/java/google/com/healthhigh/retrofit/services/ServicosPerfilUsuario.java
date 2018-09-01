package google.com.healthhigh.retrofit.services;

import java.util.Map;

import google.com.healthhigh.domain.Usuario;
import google.com.healthhigh.retrofit.ContainerAPI;
import google.com.healthhigh.retrofit.LoginContainer;
import google.com.healthhigh.retrofit.RespostaContainer;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ServicosPerfilUsuario {
    @GET("login")
//    Call<RespostaContainer<Usuario>> autenticarUsuario(@Query("acao") String acao, @Query("login") String method, @Query("senha") String handler);
    Call<ResponseBody> autenticarUsuario(@Query("email") String method, @Query("senha") String handler);
}
