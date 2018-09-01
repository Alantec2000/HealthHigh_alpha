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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

public class DetalhesDesafiosNew extends AppCompatActivity {
    public static String DESAFIO_ACTION = "DESAFIO_ID";
    private int status;
    private Desafio d = null;
    private DesafioController d_c;

    @BindView(R.id.btn_iniciar_desafio) Button btn_aceitar_desafio;
    @BindView(R.id.rv_lista_metas_desafio) RecyclerView  rv;
    @BindView(R.id.tituloDetalhesDesafio) TextView  titulo;
    @BindView(R.id.txt_descricao_desafio) TextView descricao;
    @BindView(R.id.dataCriacaoDetalheDesafio) TextView data_criacao;
    @BindView(R.id.statusDesafio) TextView status_desafio;
    @BindView(R.id.txt_status_publicacao_desafio) TextView status_publicacao;
    @BindView(R.id.txt_data_conclusao_desafio) TextView data_conclusao_desafio;
    @BindView(R.id.txt_data_inicio_publicacao_desafio) TextView data_fim_publicacao;
    @BindView(R.id.txt_data_fim_publicacao_desafio) TextView data_inicio_publicacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_detalhes_desafios_new);
        ButterKnife.bind(this);
        d_c = new DesafioController(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaDesafio();
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
        setListaMetas(new ArrayList<TipoMeta>(d.getMetas()));
        data_conclusao_desafio.setVisibility(View.INVISIBLE);
        status_publicacao.setVisibility(View.INVISIBLE);
        data_inicio_publicacao.setVisibility(View.INVISIBLE);
        data_fim_publicacao.setVisibility(View.INVISIBLE);
        atualizaStatusDesafio();
        atualizaDataVisualizacaoDesafio();
    }

    private void atualizaDataVisualizacaoDesafio() {
        if(d != null && d.getData_visualizacao() == 0){
            d.setData_visualizacao(DataHelper.now());
            d_c.atualizar(d);
        }
    }

    private void atualizaStatusDesafio() {
        if(d.getPublicacao() != null){
            status_publicacao.setVisibility(View.VISIBLE);
            data_inicio_publicacao.setVisibility(View.VISIBLE);
            data_fim_publicacao.setVisibility(View.VISIBLE);
            data_inicio_publicacao.setText("Início: " + DataHelper.toDateString(d.getPublicacao().getData_inicio()));
            data_fim_publicacao.setText("Fim: " + DataHelper.toDateString(d.getPublicacao().getData_fim()));
            String status = "";
            boolean is_vigente = d.getPublicacao().isVigente();
            status = is_vigente ? "Vigente" : "Encerrada";
            status_publicacao.setText(status);
            if(d.getInteracao_desafio() != null){
                InteracaoDesafio i_d = d.getInteracao_desafio();
                i_d.atualizaStatus();
                if((i_d.getStatus().equals(InteracaoDesafio.PENDENTE) || i_d.getStatus().equals(InteracaoDesafio.EM_EXECUCAO)) && d.getMetas().size() > 0){
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
            status_desafio.setText("Não Publicado");
            btn_aceitar_desafio.setText("Desafio Não Publicado");
            btn_aceitar_desafio.setEnabled(false);
        }
    }

    private void verificarDesafioConcluido() {
        if(d_c.verificarDesafioConcluido(d)) {
            if(d_c.concluirDesafio(d.getInteracao_desafio())){
                MessageDialog.showMessage(this, "Desafio concluído com sucesso!", "Desafio concluído!");
                Intent i = new Intent("google.com.healthhigh.EnviarDesafioConcluido");
                sendBroadcast(i);
            }
        }
    }

    private void setListaMetas(List<TipoMeta> metas){
        rv.setAdapter(new TipoMetaAdapter(this,metas));
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    @OnClick(R.id.btn_iniciar_desafio)
    public void iniciarDesafioPress(){
        if(d.getStatus() == Desafio.PENDENTE){
            iniciarDesafio();
            atualizaStatusDesafio();
        } else if (d.getStatus() == Desafio.EM_EXECUCAO){
            cancelarExecucaoDesafio();
            atualizaStatusDesafio();
        }
    }

    private void iniciarDesafio(){
        d_c.iniciarDesafio(d);
    }

    private void cancelarExecucaoDesafio() {
        d_c.cancelarDesafio(d);
    }
}
