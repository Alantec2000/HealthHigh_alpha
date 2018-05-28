package google.com.healthhigh.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.healthhigh.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import google.com.healthhigh.adapter.SpinnerOptionAdapter;
import google.com.healthhigh.controller.AtividadeController;
import google.com.healthhigh.controller.DesafioController;
import google.com.healthhigh.controller.SessaoAtividadeController;
import google.com.healthhigh.controller.InteracaoAtividadeController;
import google.com.healthhigh.domain.Atividade;
import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.domain.ExecucaoAtividade;
import google.com.healthhigh.domain.SessaoAtividade;
import google.com.healthhigh.sensors.SensorPasso;
import google.com.healthhigh.sensors.SensorUI;
import google.com.healthhigh.utils.MessageDialog;
import google.com.healthhigh.utils.Toaster;

public class RealizandoAtividadeActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener, Chronometer.OnChronometerTickListener, View.OnClickListener{
    public static String DESAFIO_ID = "desafio_id";

    private final int EM_EXECUCAO = 1, PARADO = 2, PAUSADO = 3;
    private int estado = PARADO;
    protected int check = 0;

    protected Context context;
    private SensorPasso pedometro;
    private Intent intent;
    private Desafio d = null;
    Map<Long, Atividade> atividades;
    SessaoAtividade sessao_atual;
    Atividade atividade_atual;
    ExecucaoAtividade execucao_atual;
    private int opcao_anterior;

    private long tempo_pausa = 0;

    private RecyclerView rv;
    private TextView n_passos_label;
    Spinner sp_atividades;
    Chronometer chrn;
    Button iniciar, pausar, parar;

    private DesafioController d_c;
    private AtividadeController a_c;
    private SessaoAtividadeController s_a_c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realizando_desafio_new);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        context = this;
        intent = getIntent();

        setElementosUI();
        setControllers();
        setDesafio();
        setBotoes();
        setPedometro();

        // Na primeira execução o usuário não terá setado uma atividade ainda,
        // então o sistema irá carregar os dados da última sessão
        carregarSessao();
        alterarEstadoBotoes();
    }

    private void alterarEstadoBotoes() {
        if(estado == PARADO){
            pararPressionado();
            if(sessao_atual != null){
                if(sessao_atual.getData_fim() == 0 && (execucao_atual != null || sessao_atual.getAtividades().size() > 0)){
                    pausar.setEnabled(false);
                    parar.setEnabled(true);
                }
            }
        }
    }

    private void setElementosUI() {
        n_passos_label = (TextView) findViewById(R.id.n_passos_label);
        sp_atividades = (Spinner) findViewById(R.id.sp_lista_atividades);
        chrn =  (Chronometer) findViewById(R.id.chrn_execucao_atividade);
    }

    private void setControllers() {
        d_c = new DesafioController(this);
        a_c = new AtividadeController(this);
        s_a_c = new SessaoAtividadeController(this);
    }

    private void carregarSessao() {
        try {
            sessao_atual = s_a_c.getSessaoAberta(atividade_atual, true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(sessao_atual != null){
                n_passos_label.setText("Passos: " + String.valueOf(sessao_atual.getTotalPassosDados()));
                chrn.setBase(SystemClock.elapsedRealtime() - sessao_atual.getTempoExecucaoTotal());
                if(sessao_atual.getData_fim() == 0) {
                    parar.setEnabled(true);
                }
            }
        }
    }

    private void setBotoes() {
        iniciar = (Button)findViewById(R.id.btn_iniciar_atividade);
        iniciar.setOnClickListener(this);

        pausar = (Button)findViewById(R.id.btn_pausar_atividade);
        pausar.setOnClickListener(this);

        parar = (Button)findViewById(R.id.btn_parar_atividade);
        parar.setOnClickListener(this);
    }

    private void setDesafio() {
        d = d_c.getDesafioAtual();
        if(d != null) {
            obterAtividades();
        }
        carregarAtividades(atividades);
    }

    private void obterAtividades() {
        atividades = a_c.getAtividades(d);
    }

    private void carregarAtividades(Map<Long, Atividade> atividades) {
        List<SpinnerOptionAdapter> options = new ArrayList<>();
        options.add(new SpinnerOptionAdapter("Nenhuma", null));
        opcao_anterior = 0;
        if(atividades != null && atividades.size() > 0){
            for(Atividade a : atividades.values()){
                options.add(new SpinnerOptionAdapter(a.getNome(), a));
            }
            sp_atividades.setEnabled(true);
        } else {
            sp_atividades.setEnabled(false);
        }
        ArrayAdapter<SpinnerOptionAdapter> adapter =
                new ArrayAdapter<SpinnerOptionAdapter> (
                        this,
                        android.R.layout.simple_spinner_item,
                        options
                );
        sp_atividades.setAdapter(adapter);
        check = 0;
        sp_atividades.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parant, View v, final int pos, long id) {
        if(++check > 1){
            SpinnerOptionAdapter s = (SpinnerOptionAdapter) parant.getItemAtPosition(pos);
            final Atividade atividade = (Atividade) s.tag;
            AlertDialog a_d = new AlertDialog.Builder(this).create();
            if(estado == EM_EXECUCAO || estado == PAUSADO){
                if(estado == EM_EXECUCAO){
                    pausar();
                }
                a_d.setTitle("Atividade em andamento:");
                a_d.setMessage("Deseja realmente mudar de atividade?");
                a_d.setButton(DialogInterface.BUTTON_NEGATIVE, "Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sp_atividades.post(new Runnable() {
                            @Override
                            public void run() {
                                sp_atividades.setSelection(opcao_anterior);
                            }
                        });
                        iniciar();
                        check = 0;
                        Log.v("Atividade", "Não Alterar");
                    }
                });
                a_d.setButton(DialogInterface.BUTTON_POSITIVE, "Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        estado = PARADO;
                        atividade_atual = atividade;
                        opcao_anterior = pos;
                        carregarSessao();
                        Log.v("Atividade", "Alterando");
                    }
                });
                a_d.show();
            } else if(estado == PARADO){
                atividade_atual = atividade;
                carregarSessao();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void setPedometro() {
        pedometro = new SensorPasso(this);
        if(pedometro.getStepSensor() != null) {
            SensorUI hud = new SensorUI();
            hud.setnPassos(n_passos_label);
            hud.setTempo(chrn);
        } else {
            Toaster.toastLongMessage(this, "Celular não possui sensor compatível para execução das atividades");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void pararPedometro(){
        if(pedometro != null)
            pedometro.unsetEventListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pararPedometro();
    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {

    }

    private void pausarCronometro() {
        chrn.stop();
        atualizarTempoAtividade();
    }

    private void atualizarTempoAtividade(){
        tempo_pausa = chrn.getBase() - SystemClock.elapsedRealtime();
    }

    @SuppressLint("HandlerLeak")
    private Handler handle_numero_passos = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int numero_passos = pedometro.getPassos();
            n_passos_label.setText("Passos: " + String.valueOf(numero_passos + sessao_atual.getTotalPassosDados()));
            if(atividade_atual != null && (atividade_atual.getTotal_passos_dados() <= (sessao_atual.getTotalPassosDados() + numero_passos))){
                pausar();
                InteracaoAtividadeController i_a_c = new InteracaoAtividadeController(context);
                try {
                    i_a_c.finalizarInteracaoAtividade(atividade_atual.getInteracao_atividade());
                    MessageDialog.showMessage(context, "Atividade concluída!", "Parabéns!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };


    public void iniciar(){
        if(pedometro != null){
            if(sessao_atual != null){
                if(execucao_atual == null) {
                    execucao_atual = new ExecucaoAtividade();
                    execucao_atual.setSessao_atividade(sessao_atual);
                }
                pedometro.iniciarPedometro();
                pedometro.setHandler(handle_numero_passos);
                chrn.setBase((SystemClock.elapsedRealtime() + tempo_pausa) - sessao_atual.getTempoExecucaoTotal());
                chrn.setOnChronometerTickListener(this);
                chrn.start();
                estado = EM_EXECUCAO;
                iniciarPressionado();
            } else {
                carregarSessao();
                if(sessao_atual == null){
                    estado = PARADO;
                    MessageDialog.showMessage(this, "Erro ao iniciar atividade", "Erro");
                } else {
                    iniciar();
                }
            }
        }
    }

    public void pausar(){
        if(sessao_atual != null){
            pausarPressionado();
            pararPedometro();
            pausarCronometro();
            atualizarExecucao();
            s_a_c.adicionarExecucao(execucao_atual);
            if(execucao_atual.getId() != 0){
//                sessao_atual.getAtividades().put(execucao_atual.getId(), execucao_atual);
//                execucao_atual = new ExecucaoAtividade();
                estado = PAUSADO;
                Log.i("Atividade pausada", "Sucesso");
            } else {
                Log.i("Atividade pausada", "erro");
            }
        }
    }

    private void atualizarExecucao() {
        execucao_atual.setTotal_passos_dados(pedometro.getPassos());
        execucao_atual.setTempo_execucao((tempo_pausa * (-1)) - sessao_atual.getTempoExecucaoTotal());
    }

    public void parar(){
        if(sessao_atual != null){
            pausarCronometro();
            pararPedometro();
            boolean inseriu_ou_atualizou_execucao = true;
            if(execucao_atual != null){
                atualizarTempoAtividade();
                atualizarExecucao();
                s_a_c.adicionarExecucao(execucao_atual);
                inseriu_ou_atualizou_execucao = execucao_atual.getId() != 0;
            }
            s_a_c.encerrarSessao(sessao_atual);
            if(inseriu_ou_atualizou_execucao && sessao_atual.getData_fim() > 0){
                Log.i("Atividade parada", "Sucesso");
                sessao_atual = null;
                execucao_atual = null;
                chrn.setBase(SystemClock.elapsedRealtime());
                n_passos_label.setText("Passos: 0");
                tempo_pausa = 0;
                estado = PARADO;
                Toaster.toastLongMessage(this, "Sessão de atividade encerrada!");
                pararPressionado();
            } else {
                Toaster.toastLongMessage(this, "Erro ao tentar encerrar a sessão, tente novamente ou cancele a sessão atual!");
                Log.i("Atividade pausada", "erro");
            }
        }
    }

    public void travaTudo(){
        iniciar.setEnabled(false);
        pausar.setEnabled(true);
        parar.setEnabled(true);
    }
    public void iniciarPressionado(){
        iniciar.setEnabled(false);
        pausar.setEnabled(true);
        parar.setEnabled(true);
    }
    public void pausarPressionado(){
        iniciar.setEnabled(true);
        pausar.setEnabled(false);
        parar.setEnabled(true);
    }
    public void pararPressionado(){
        iniciar.setEnabled(true);
        pausar.setEnabled(false);
        parar.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        view.setEnabled(false);
        switch (view.getId()) {
            case R.id.btn_iniciar_atividade:
                iniciar();
            break;
            case R.id.btn_pausar_atividade:
                pausar();
            break;
            case R.id.btn_parar_atividade:
                parar();
            break;
        }
    }
}
