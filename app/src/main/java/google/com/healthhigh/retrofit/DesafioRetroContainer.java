package google.com.healthhigh.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

import google.com.healthhigh.domain.Atividade;
import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.domain.Noticia;
import google.com.healthhigh.domain.Premiacao;
import google.com.healthhigh.domain.Questionario;

public class DesafioRetroContainer {
    public Map<Long, Desafio> desafios;
}
