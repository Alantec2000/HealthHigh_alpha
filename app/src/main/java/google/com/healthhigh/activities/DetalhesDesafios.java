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

import java.util.List;
import java.util.TreeMap;

import google.com.healthhigh.adapter.MetaListAdapter;
import google.com.healthhigh.dao.DesafioXMetaDAO;
import google.com.healthhigh.dao.MetaDAO;
import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.domain.Meta;
import google.com.healthhigh.utils.DataHelper;
import google.com.healthhigh.utils.MessageDialog;

public class DetalhesDesafios extends AppCompatActivity implements View.OnClickListener {
    public static String DESAFIO_ACTION = "DESAFIO_ID";
    private Button btnInitDesafio;
    private Desafio d = null;
    private DesafioXMetaDAO dao;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_desafios);
        dao = new DesafioXMetaDAO(this);
        rv = (RecyclerView) findViewById(R.id.listaMetasDesafio);
        setEventBotoes();
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
            d = dao.getDesafioMetas(id);
            if(d != null){
//                associaMetasDesafios(d);
                TextView titulo = (TextView) findViewById(R.id.tituloDetalhesDesafio);
                TextView descricao = (TextView) findViewById(R.id.descricaoDetalheDesafio);
                TextView data_criacao = (TextView) findViewById(R.id.dataCriacaoDetalheDesafio);
                TextView statusDesafio = (TextView) findViewById(R.id.statusDesafio);
                TextView dataConclusao = (TextView) findViewById(R.id.data_conclusao_desafio);
                TextView dataConclusao_label = (TextView) findViewById(R.id.data_conclusao_desafio_label);
                data_criacao.setText(DataHelper.parseUT(d.getData_criacao(), "dd/MM/yy"));
                statusDesafio.setText(Desafio.getStatusText(d.getStatus()));
                titulo.setText(d.getTitulo());
                descricao.setText(d.getDescricao());
                String conclusao = "Indefinida";
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
                dataConclusao.setText(conclusao);
//                setListaMetas(d.getMetas());
            } else {
                MessageDialog.showMessage(this,"Desafio não foi encontrado no sistema, tente novamente!", "Desafio não encontrado");
            }
        } else {
            MessageDialog.showMessage(this, "Nenhum ID de Desafio informado!", "Desafio não informado");
        }
        btnInitDesafio.setEnabled(d != null && (d.getStatus() != Desafio.CONCLUIDO && d.getStatus() != Desafio.ENCERRADO));
    }

    private void setListaMetas(List<Meta> metas){
        rv.setAdapter(new MetaListAdapter(this,metas));
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
