package google.com.healthhigh.retrofit.services;

import java.util.List;

import google.com.healthhigh.domain.Premiacao;
import google.com.healthhigh.retrofit.RespostaContainer;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServicoPremiacao {
    @GET("PremiacaoDispatcher.php")
    Call<RespostaContainer<List<Premiacao>>> obterNovasPremiacoes(@Query(encoded=true, value="json_request") String json);

    @GET("premiacoes")
    Call<ResponseBody> obterRawPremiacoes();
}
