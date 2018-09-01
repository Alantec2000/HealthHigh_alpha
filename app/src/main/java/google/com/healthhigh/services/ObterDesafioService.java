package google.com.healthhigh.services;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.healthhigh.R;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import google.com.healthhigh.activities.MedalhasActivity;
import google.com.healthhigh.controller.AtividadeController;
import google.com.healthhigh.controller.DesafioController;
import google.com.healthhigh.controller.PremiacaoController;
import google.com.healthhigh.domain.Atividade;
import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.domain.Premiacao;
import google.com.healthhigh.retrofit.DispatcherBody;
import google.com.healthhigh.retrofit.RespostaContainer;
import google.com.healthhigh.retrofit.RetrofitConfig;
import google.com.healthhigh.retrofit.DesafioRetroContainer;
import google.com.healthhigh.utils.DataHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObterDesafioService extends IntentService {
    public static final String NOVOS_DESAFIOS = "obter_novos_desafios";

    public ObterDesafioService() {
        super("ColetarDesafiosActivity");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /*private void obterNovosDesafios() {
        DispatcherBody<Set<Long>> request = new DispatcherBody<>();
        Set<Long> desafios_obtidos = obterDesafiosConhecidos();
        request.acao = "coletarDesafiosCompletos";
        request.params.put("desafios_obtidos", desafios_obtidos);
        String json = new Gson().toJson(request);
        Log.i("json coletar desafios", json);
        Call<RespostaContainer<List<Desafio>>> call = new RetrofitConfig().getServicosMetas().obterNovasMetas(json);
        call.enqueue(new Callback<RespostaContainer<List<Desafio>>>() {
            @Override
            public void onResponse(Call<RespostaContainer<List<Desafio>>> call, Response<RespostaContainer<List<Desafio>>> response) {
                if(response.isSuccessful()){
                    persistirDesafio(response.body().getContent());
                }
            }

            @Override
            public void onFailure(Call<RespostaContainer<List<Desafio>>> call, Throwable t) {
                Log.e("Login error", t.fillInStackTrace().getMessage());
                Log.e("Login error", call.request().toString());
            }
        });
    }
*/

    private void obterNovasPremiacoes(){
        DispatcherBody<Set<Long>> request = new DispatcherBody<>();
        final PremiacaoController p_c = new PremiacaoController(getContext());
        final Map<Long,Premiacao> premiacoes_ = p_c.getPremiacoes();
        Call<ResponseBody> call = new RetrofitConfig().getServicosPremiacao().obterRawPremiacoes();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    String resposta = null;
                    JsonArray premiacoes = new JsonArray();
                    JsonObject jo = null;
                    try {
                        resposta = response.body().string();
                        jo = new JsonParser().parse(resposta).getAsJsonObject();
                        premiacoes = jo.getAsJsonArray("data");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(premiacoes.size() > 0){
                        Gson g = new Gson();
                        for (Iterator<JsonElement> it = premiacoes.iterator(); it.hasNext(); ) {
                            JsonElement j = it.next();

                            Premiacao p = g.fromJson(j, Premiacao.class);
                            if((p != null && p.getId() > 0) && !premiacoes_.containsKey(p.getId())){
                                p_c.inserirPremiacao(p);
                            }
                        }
                        getAtividades();
                    } else {
                        Log.i("Obter desafios:","Nenhum desafio encontrado!");
                    }
                } else {
                    Log.e("Erro ao baixar desafios", "Requisição deu pau!");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Login error", t.fillInStackTrace().getMessage());
                Log.e("Login error", call.request().toString());
            }
        });
    }
    private void obterNovosDesafios() {

    }

    private List<Atividade> getAtividades() {
        final List<Atividade> atividades_ = new ArrayList<>();
        final AtividadeController a_c = new AtividadeController(getContext());
        final Map<Long, Atividade> _atividades_ = a_c.getAtividades();
        DispatcherBody<Set<Long>> request = new DispatcherBody<>();
        final PremiacaoController p_c = new PremiacaoController(getContext());
        final Map<Long,Premiacao> premiacoes_ = p_c.getPremiacoes();
        Call<ResponseBody> call = new RetrofitConfig().getServicosMetas().getRawAtividade();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    String resposta = null;
                    JsonArray atividades = new JsonArray();
                    JsonObject jo = null;
                    try {
                        resposta = response.body().string();
                        jo = new JsonParser().parse(resposta).getAsJsonObject();
                        atividades = jo.getAsJsonArray("data");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(atividades.size() > 0){
                        Gson g = new Gson();
                        for (Iterator<JsonElement> it = atividades.iterator(); it.hasNext(); ) {
                            JsonElement j = it.next();
                            Atividade p = g.fromJson(j, Atividade.class);
                            if((p != null && p.getId() > 0) && (p.getId_premiacao() > 0 && premiacoes_.containsKey(p.getId_premiacao()))){
                                if(!_atividades_.containsKey(p.getId())){
                                    p.setPremiacao(premiacoes_.get(p.getId_premiacao()));
                                    atividades_.add(p);
                                }
                            }
                        }
                        DispatcherBody<Set<Long>> request = new DispatcherBody<>();
                        final DesafioController d_c = new DesafioController(getContext());
                        final Map<Long, Desafio> _desafios_ = d_c.getMapDesafios();
                        Call<ResponseBody> call_desafio = new RetrofitConfig().getServicosDesafio().getRawDesafio();
                        call_desafio.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response.isSuccessful()){
                                    String resposta = null;
                                    JsonArray desafios = new JsonArray();
                                    JsonObject jo = null;
                                    try {
                                        resposta = response.body().string();
                                        jo = new JsonParser().parse(resposta).getAsJsonObject();
                                        desafios = jo.getAsJsonArray("data");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if(desafios.size() > 0){
                                        Gson g = new Gson();
                                        List<Desafio> desafios_ = new ArrayList<>();
                                        for (Iterator<JsonElement> it = desafios.iterator(); it.hasNext(); ) {
                                            JsonElement j = it.next();

                                            Desafio p = g.fromJson(j, Desafio.class);
                                            if((p != null && p.getId() > 0) && !_desafios_.containsKey(p.getId())){
                                                if(p.getId_premiacao() > 0 && premiacoes_.containsKey(p.getId_premiacao())){
                                                    p.setPremiacao(premiacoes_.get(p.getId_premiacao()));
                                                    p.setAtividades(atividades_);
                                                    desafios_.add(p);
                                                }
                                            }
                                        }
                                        if(desafios_.size() > 0){
                                            d_c.inserirDesafios(desafios_);
                                        }
                                    } else {
                                        Log.i("Obter desafios:","Nenhum desafio encontrado!");
                                    }
                                } else {
                                    Log.e("Erro ao baixar desafios", "Requisição deu pau!");
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.e("Login error", t.fillInStackTrace().getMessage());
                                Log.e("Login error", call.request().toString());
                            }
                        });
                    } else {
                        Log.i("Obter desafios:","Nenhum desafio encontrado!");
                    }
                } else {
                    Log.e("Erro ao baixar desafios", "Requisição deu pau!");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Login error", t.fillInStackTrace().getMessage());
                Log.e("Login error", call.request().toString());
            }
        });
        return atividades_;
    }

    private void persistirDesafio(List<Desafio> desafios) {
        DesafioController d_c = new DesafioController(this);
        d_c.inserirDesafios(desafios);
    }

    private Set<Long> obterAtividadesConhecidas() {
        AtividadeController a_c = new AtividadeController(this);
        return a_c.getIdAtividades();
    }

    @SuppressLint("DefaultLocale")
    private void persistirPremiacoes(List<Premiacao> premiacoes) {
        PremiacaoController p_c = new PremiacaoController(this);
        int n_p_i = p_c.inserirListaPremiacoes(premiacoes);
        if(n_p_i > 0){
            NotificationManager n_m = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if(n_m != null){
                Intent i = new Intent(this, MedalhasActivity.class);
                i.putExtra(MedalhasActivity.SOMENTE_NOVAS, 1);
                PendingIntent i_p = PendingIntent.getActivities(this, 0, new Intent[]{i}, 0);
                NotificationCompat.Builder n_b = new NotificationCompat.Builder(this);
                n_b.setContentTitle("Conteúdo novo:");
                String aux = n_p_i > 1 ? "Novas premiações" : "Nova premiação";
                n_b.setContentText(String.format("%d %s", n_p_i, aux));
                n_b.setSmallIcon(R.mipmap.ic_launcher);
                Notification n = n_b.build();
                n.vibrate = new long[]{100,400,100};
                n_m.notify(R.layout.activity_medalhas, n);
            }
        }
    }

    public Set<Long> obterPremiacoesConhecidas(){
        PremiacaoController controller = new PremiacaoController(this);
        return controller.getIdsPremiacoes();
    }

    public Set<Long> obterDesafiosConhecidos(){
        DesafioController controller = new DesafioController(this);
        return controller.getDesafios();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public Context getContext(){
        return this;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        obterNovasPremiacoes();
    }
}
