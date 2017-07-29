package google.com.healthhigh.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.Iterator;
import java.util.TreeMap;

import google.com.healthhigh.adapter.MetaListAdapter;
import google.com.healthhigh.dao.DesafioXMetaDAO;
import google.com.healthhigh.dao.MetaDAO;
import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.domain.Meta;
import google.com.healthhigh.utils.DataHelper;

import com.google.healthhigh.R;

public class DetalhesDesafios extends AppCompatActivity implements View.OnClickListener {
    public static String DESAFIO_ACTION = "DESAFIO_ID";
    private DesafioXMetaDAO dao;
    private RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_desafios);
        dao = new DesafioXMetaDAO(this);
        rv = (RecyclerView) findViewById(R.id.listaMetasDesafio);
        setEventBotoes();

        carregaDesafio();
    }

    private void setEventBotoes() {
    }

    private void associaMetasDesafios(Desafio d) {
        MetaDAO md = new MetaDAO(this);
        DesafioXMetaDAO dxm = new DesafioXMetaDAO(this);
        TreeMap<Integer, Meta> metas = md.getMetas();
        if(metas != null){
            Iterator i = metas.navigableKeySet().iterator();
            while(i.hasNext()){
                Meta m = metas.get(i.next());
                dxm.insereDesafioAssocMeta(d,m);
            }
        }
    }

    private void carregaDesafio() {
        Intent i = getIntent();
        long id = i.getLongExtra(DESAFIO_ACTION, 0);
        if(id > 0){
            Desafio d = dao.getDesafioMetas(id);
//            dao.getDesafiosXMetas();
            if(d != null){
//                associaMetasDesafios(d);
                TextView titulo = (TextView) findViewById(R.id.tituloDetalhesDesafio);
                TextView descricao = (TextView) findViewById(R.id.descricaoDetalheDesafio);
                TextView data_criacao = (TextView) findViewById(R.id.dataCriacaoDetalheDesafio);
                data_criacao.setText(DataHelper.parseUT(d.getData_criacao(), "dd/MM/yy"));
                titulo.setText(d.getTitulo());
                descricao.setText(d.getDescricao());
                setListaMetas(d.getMetas());
            } else {
                showWarning("Desafio não foi encontrado no sistema, tente novamente!", "Desafio não encontrado");
            }
        } else {
            showWarning("Nenhum ID de Desafio informado!", "Desafio não informado");
        }
    }

    private void setListaMetas(TreeMap<Integer, Meta> metas){
        rv.setAdapter(new MetaListAdapter(this,metas));
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void showWarning(String msg, String titulo){
        final AlertDialog ad=new AlertDialog.Builder(this).create();
        ad.setTitle(titulo);
        ad.setMessage(msg);
        ad.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case 0:
//                finish();
                break;
        }
    }
}
