package google.com.healthhigh.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.healthhigh.R;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import google.com.healthhigh.adapter.TipoMetaAdapter;
import google.com.healthhigh.controller.DesafioController;
import google.com.healthhigh.dao.DesafioXMetaDAO;
import google.com.healthhigh.dao.MetaDAO;
import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.domain.InteracaoDesafio;
import google.com.healthhigh.domain.Meta;
import google.com.healthhigh.domain.TipoMeta;
import google.com.healthhigh.utils.DataHelper;
import google.com.healthhigh.utils.MessageDialog;

public class DetalhesDesafiosNew extends AppCompatActivity implements View.OnClickListener {
    public static String DESAFIO_ACTION = "DESAFIO_ID";
    private int status;
    private Button btn_aceitar_desafio;
    private Desafio d = null;
    private DesafioController d_c;
    private RecyclerView rv;
    private TextView
            titulo,
            descricao,
            data_criacao,
            status_desafio,
            status_publicacao,
            data_conclusao_desafio,
            data_fim_publicacao,
            data_inicio_publicacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_detalhes_desafios_new);
        d_c = new DesafioController(this);
        setElementosUI();
        setEventoBotao();
    }

    private void setElementosUI() {
        rv = (RecyclerView) findViewById(R.id.rv_lista_metas_desafio);
        titulo = (TextView) findViewById(R.id.tituloDetalhesDesafio);
        descricao = (TextView) findViewById(R.id.txt_descricao_desafio);
        data_criacao = (TextView) findViewById(R.id.dataCriacaoDetalheDesafio);
        status_desafio = (TextView) findViewById(R.id.statusDesafio);
        data_conclusao_desafio = (TextView) findViewById(R.id.txt_data_conclusao_desafio);
        status_publicacao = (TextView) findViewById(R.id.txt_status_publicacao_desafio);
        data_inicio_publicacao = (TextView) findViewById(R.id.txt_data_inicio_publicacao_desafio);
        data_fim_publicacao = (TextView) findViewById(R.id.txt_data_fim_publicacao_desafio);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaDesafio();
    }

    private void setEventoBotao() {
        btn_aceitar_desafio = (Button) findViewById(R.id.btn_iniciar_desafio);
        btn_aceitar_desafio.setOnClickListener(this);
    }

    private void associaMetasDesafios(Desafio d) {
        MetaDAO md = new MetaDAO(this);
        DesafioXMetaDAO dxm = new DesafioXMetaDAO(this);
        TreeMap<Long, Meta> metas = md.getMetas();
        if(metas != null){
            for(Long i : metas.navigableKeySet()){
                Meta m = metas.get(i);
                dxm.insereDesafioAssocMeta(d,m);
            }
        }
    }

    private void carregaDesafio() {
        Intent i = getIntent();
        long id = i.getLongExtra(DESAFIO_ACTION, 0);
        if(id > 0){
            d = new Desafio();
            d.setId(id);
            d = d_c.getDesafio(d);
            if(d != null){
                carregarInformacoesDesafio(d);
            } else {
                MessageDialog.showMessage(this,"Desafio não foi encontrado no sistema, tente novamente!", "Desafio não encontrado");
            }
        } else {
            MessageDialog.showMessage(this, "Nenhum ID de Desafio informado!", "Desafio não informado");
        }
    }

    private void carregarInformacoesDesafio(Desafio d) {
        data_criacao.setText(DataHelper.toDateString(d.getData_criacao()));
        titulo.setText(d.getTitulo());
        descricao.setText(d.getDescricao());
        setListaMetas(new ArrayList<TipoMeta>(d.getMetas_list()));
        data_conclusao_desafio.setVisibility(View.INVISIBLE);
        atualizaStatusDesafio();
    }

    private void atualizaStatusDesafio() {
        if(d.getPublicacao() != null){
            data_inicio_publicacao.setText("Início: " + DataHelper.toDateString(d.getPublicacao().getData_inicio()));
            data_fim_publicacao.setText("Fim: " + DataHelper.toDateString(d.getPublicacao().getData_fim()));
            String status = "";
            boolean is_vigente = d.getPublicacao().isVigente();
            status = is_vigente ? "Vigente" : "Encerrada";
            status_publicacao.setText(status);
            if(d.getInteracao_desafio() != null){
                InteracaoDesafio i_d = d.getInteracao_desafio();
                i_d.atualizaStatus();
                if(i_d.getStatus().equals(InteracaoDesafio.PENDENTE) ||
                   i_d.getStatus().equals(InteracaoDesafio.EM_EXECUCAO) ){
                    verificarDesafioConcluido();
                }
                switch (i_d.getStatus()) {
                    case InteracaoDesafio.PENDENTE:
                        status = "Pendente";
                        btn_aceitar_desafio.setText("Iniciar Desafio");
                        btn_aceitar_desafio.setEnabled(true);
                        break;
                    case InteracaoDesafio.EM_EXECUCAO:
                        status = "Realizando";
                        btn_aceitar_desafio.setText("Cancelar Desafio");
                        btn_aceitar_desafio.setEnabled(true);
                        break;
                    case InteracaoDesafio.CONCLUIDO:
                        status = "Concluído";
                        data_conclusao_desafio.setVisibility(View.VISIBLE);
                        data_conclusao_desafio.setText("Concluído em: " + DataHelper.toDateString(i_d.getData_conclusao()));
                        btn_aceitar_desafio.setText("Desafio Concluído");
                        btn_aceitar_desafio.setEnabled(false);
                        break;
                    case InteracaoDesafio.ENCERRADO:
                        status = "Encerrado";
                        btn_aceitar_desafio.setText("Desafio Encerrado");
                        btn_aceitar_desafio.setEnabled(false);
                        break;
                    case InteracaoDesafio.CANCELADO:
                        status = is_vigente ? "Pendente" : "Cancelado";
                        if(is_vigente){
                            btn_aceitar_desafio.setText("Iniciar Desafio");
                            btn_aceitar_desafio.setEnabled(true);
                        } else {
                            btn_aceitar_desafio.setText("Desafio Cancelado");
                            btn_aceitar_desafio.setEnabled(false);
                        }
                        break;
                }
                status_desafio.setText(status);
            }
        } else {
            d.setStatus(Desafio.NAO_PUBLICADO);
            btn_aceitar_desafio.setText("Desafio Não Publicado");
            btn_aceitar_desafio.setEnabled(false);
        }
    }

    private void verificarDesafioConcluido() {
        if(d_c.verificarDesafioConcluido(d)) {
            if(d_c.concluirDesafio(d.getInteracao_desafio())){
                MessageDialog.showMessage(this, "Desafio concluído com sucesso!", "Desafio concluído!");
            }
        }
    }
/*
    private void carregaDesafio() {
        Intent i = getIntent();
        long id = i.getLongExtra(DESAFIO_ACTION, 0);
        if(id > 0){
            d = d_c.getDesafio(id);
            if(d != null){
                data_criacao.setText(DataHelper.toDateString(d.getData_criacao()));
                status_desafio.setText(Desafio.getStatusText(d.getStatus()));
                titulo.setText(d.getTitulo());
                descricao.setText(d.getDescricao());
                d.setMetas_list(d_c.getMetasDesafio(d));
//                String conclusao = "Indefinida";
                *//* Reconstruir depois para um método da classe Desafio
                if(d.getStatus() == Desafio.CONCLUIDO){
                    Button botaoIniciar = (Button) findViewById(R.id.botaoIniciar);
                    dataConclusao.setVisibility(View.VISIBLE);
                    data_conclusao_desafio.setVisibility(View.VISIBLE);
                    conclusao = d.getData_conclusao() == 0 ? "Indefinida" : DataHelper.parseUT(d.getData_conclusao(), "dd/MM/yy");
                } else if(d.getStatus() == Desafio.ENCERRADO){
                    dataConclusao.setVisibility(View.VISIBLE);
                    data_conclusao_desafio.setVisibility(View.VISIBLE);
                    conclusao = d.getData_conclusao() == 0 ? "Desafio não foi concluído!" : DataHelper.parseUT(d.getData_conclusao(), "dd/MM/yy");
                } else {
                    dataConclusao.setVisibility(View.INVISIBLE);
                    data_conclusao_desafio.setVisibility(View.INVISIBLE);
                }
                dataConclusao.setText(conclusao);*//*
//                setListaMetas(d.getMetas());
            } else {
                MessageDialog.showMessage(this,"Desafio não foi encontrado no sistema, tente novamente!", "Desafio não encontrado");
            }
        } else {
            MessageDialog.showMessage(this, "Nenhum ID de Desafio informado!", "Desafio não informado");
        }
        btn_aceitar_desafio.setEnabled(d != null && (d.getStatus() != Desafio.CONCLUIDO && d.getStatus() != Desafio.ENCERRADO));
    }*/

    private void setListaMetas(List<TipoMeta> metas){
        rv.setAdapter(new TipoMetaAdapter(this,metas));
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_iniciar_desafio:
                if(d.getStatus() == Desafio.PENDENTE){
                    iniciarDesafio();
                    atualizaStatusDesafio();
                } else if (d.getStatus() == Desafio.EM_EXECUCAO){
                    cancelarExecucaoDesafio();
                    atualizaStatusDesafio();
                }
                break;
        }
    }

    private void iniciarDesafio(){
        d_c.iniciarDesafio(d);
    }

    private void cancelarExecucaoDesafio() {
        d_c.cancelarDesafio(d);
    }

    /*private void iniciarDesafio() {
        Intent initDesafio = new Intent(this, RealizandoDesafioActivity.class);
        initDesafio.putExtra(RealizandoDesafioActivity.DESAFIO_ID, d.getId());
        startActivity(initDesafio);
    }*/
}
