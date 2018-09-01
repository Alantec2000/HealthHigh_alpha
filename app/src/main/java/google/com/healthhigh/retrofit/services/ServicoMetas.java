package google.com.healthhigh.retrofit.services;

import java.util.List;

import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.retrofit.RespostaContainer;
import google.com.healthhigh.retrofit.DesafioRetroContainer;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServicoMetas {
    @GET("ColetaDispatcher.php")
    Call<RespostaContainer<List<Desafio>>> obterNovasMetas(@Query("json_request") String json);
    @GET("atividades")
    Call<ResponseBody> getRawAtividade();
}