package google.com.healthhigh.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import google.com.healthhigh.retrofit.services.ServicoMetas;
import google.com.healthhigh.retrofit.services.ServicoPremiacao;
import google.com.healthhigh.retrofit.services.ServicosDesafio;
import google.com.healthhigh.retrofit.services.ServicosPerfilUsuario;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {
    private Retrofit retro;
    public static final String URL = "http://boyware.com.br/api/";
    public RetrofitConfig() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retro = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
    }

    public ServicosPerfilUsuario getServicoPerfilUsuario() {
        return this.retro.create(ServicosPerfilUsuario.class);
    }

    public ServicosDesafio getServicosDesafio() {
        return this.retro.create(ServicosDesafio.class);
    }

    public ServicoPremiacao getServicosPremiacao() {
        return this.retro.create(ServicoPremiacao.class);
    }

    public ServicoMetas getServicosMetas() {
        return this.retro.create(ServicoMetas.class);
    }
}