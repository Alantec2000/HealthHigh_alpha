package google.com.healthhigh.activities;

import android.content.Intent;
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

import google.com.healthhigh.adapter.MetaListAdapter;
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
    private Button btnInitDesafio;
    private Desafio d = null;
    private DesafioController d_c;
    private RecyclerView rv;
    private TextView titulo, descricao, data_criacao, statusDesafio, dataConclusao_label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_desafios_new);
        d_c = new DesafioController(this);
        setUIElements();
        setEventBotoes();
    }

    private void setUIElements() {
        rv = (RecyclerView) findViewById(R.id.rv_lista_metas_desafio);
        titulo = (TextView) findViewById(R.id.tituloDetalhesDesafio);
        descricao = (TextView) findViewById(R.id.txt_descricao_desafio);
        data_criacao = (TextView) findViewById(R.id.dataCriacaoDetalheDesafio);
        statusDesafio = (TextView) findViewById(R.id.statusDesafio);
        dataConclusao_label = (TextView) findViewById(R.id.txt_data_status_desafio);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaDesafio();
    }

    private void setEventBotoes() {
        btnInitDesafio = (Button) findViewById(R.id.botaoIniciar);
        btnInitDesafio.setOnClickListener(this);
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
        btnInitDesafio.setEnabled(d != null && (d.getStatus() != Desafio.CONCLUIDO && d.getStatus() != Desafio.ENCERRADO));
    }

    private void carregarInformacoesDesafio(Desafio d) {
        data_criacao.setText(DataHelper.toDateString(d.getData_criacao()));
        titulo.setText(d.getTitulo());
        descricao.setText(d.getDescricao());
        setListaMetas(new ArrayList<TipoMeta>(d.getMetas_list().values()));
        if(d.getPublicacao() == null){
            statusDesafio.setText("Desafio não publicado");
            dataConclusao_label.setVisibility(View.INVISIBLE);
        } else {
            dataConclusao_label.setVisibility(View.VISIBLE);
            if(d.getInteracao_desafio() != null){
                InteracaoDesafio i_d = d.getInteracao_desafio();
                if(i_d.getData_conclusao() > 0) {
                    dataConclusao_label.setText("Desafio concluído em: " + DataHelper.toDateString(i_d.getData_conclusao()));
                    statusDesafio.setText("Status: Concluído");
                    status = Desafio.CONCLUIDO;
                } else if(i_d.getData_aceito() > 0 && i_d.estaRealizando()){
                    dataConclusao_label.setText("Desafio iniciado em: " + DataHelper.toDateString(i_d.getData_aceito()));
                    statusDesafio.setText("Status: Realizando");
                    btnInitDesafio.setText("Cancelar desafio");
                    status = Desafio.EM_EXECUCAO;
                } else if(i_d.getData_cancelamento() > 0){
                    dataConclusao_label.setText("Desafio cancelado em: " + DataHelper.toDateString(i_d.getData_conclusao()));
                    if(i_d.getPublicacao().isVigente()){
                        statusDesafio.setText("Status: Pendente");
                        status = Desafio.PENDENTE;
                    } else{
                        statusDesafio.setText("Status: Cancelado");
                        status = Desafio.ENCERRADO;
                    }
                } else if(!d.getPublicacao().isVigente()){
                    status = Desafio.ENCERRADO;
                    dataConclusao_label.setText("Encerrado");
                    statusDesafio.setText("Publicação encerrada em: " + DataHelper.toDateString(d.getPublicacao().getData_fim()));
                }
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
                statusDesafio.setText(Desafio.getStatusText(d.getStatus()));
                titulo.setText(d.getTitulo());
                descricao.setText(d.getDescricao());
                d.setMetas_list(d_c.getMetasDesafio(d));
//                String conclusao = "Indefinida";
                *//* Reconstruir depois para um método da classe Desafio
                if(d.getStatus() == Desafio.CONCLUIDO){
                    Button botaoIniciar = (Button) findViewById(R.id.botaoIniciar);
                    dataConclusao.setVisibility(View.VISIBLE);
                    dataConclusao_label.setVisibility(View.VISIBLE);
                    conclusao = d.getData_conclusao() == 0 ? "Indefinida" : DataHelper.parseUT(d.getData_conclusao(), "dd/MM/yy");
                } else if(d.getStatus() == Desafio.ENCERRADO){
                    dataConclusao.setVisibility(View.VISIBLE);
                    dataConclusao_label.setVisibility(View.VISIBLE);
                    conclusao = d.getData_conclusao() == 0 ? "Desafio não foi concluído!" : DataHelper.parseUT(d.getData_conclusao(), "dd/MM/yy");
                } else {
                    dataConclusao.setVisibility(View.INVISIBLE);
                    dataConclusao_label.setVisibility(View.INVISIBLE);
                }
                dataConclusao.setText(conclusao);*//*
//                setListaMetas(d.getMetas());
            } else {
                MessageDialog.showMessage(this,"Desafio não foi encontrado no sistema, tente novamente!", "Desafio não encontrado");
            }
        } else {
            MessageDialog.showMessage(this, "Nenhum ID de Desafio informado!", "Desafio não informado");
        }
        btnInitDesafio.setEnabled(d != null && (d.getStatus() != Desafio.CONCLUIDO && d.getStatus() != Desafio.ENCERRADO));
    }*/

    private void setListaMetas(List<TipoMeta> metas){
        rv.setAdapter(new TipoMetaAdapter(this,metas));
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.botaoIniciar:
                iniciarDesafio();
                break;
        }
    }

    private void iniciarDesafio() {
        Intent initDesafio = new Intent(this, RealizandoDesafioActivity.class);
        initDesafio.putExtra(RealizandoDesafioActivity.DESAFIO_ID, d.getId());
        startActivity(initDesafio);
    }
}
