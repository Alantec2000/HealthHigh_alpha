package google.com.healthhigh.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import google.com.healthhigh.adapter.DesafioAdapter;
import google.com.healthhigh.dao.DesafioDAO;
import google.com.healthhigh.dao.MetaDAO;
import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.domain.Meta;

import com.google.healthhigh.R;

import java.util.List;
import java.util.TreeMap;

public class ListaDesafiosActivity extends AppCompatActivity {
    private DesafioDAO dDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_desafios);
        dDao = new DesafioDAO(this);


//        addDesafioTeste();
//        addMetaTeste();
        setDesafioList();
    }


    private void addDesafioTeste(){
        Desafio d = new Desafio();
        DesafioDAO dd = new DesafioDAO(this);
        for (int i = 0; i < 10; i++){
            d.setTitulo("Desafio " + i);
            d.setDescricao("Descrição do " + d.getTitulo());
            d.setTipo(1);
            d.setAceito(false);
            d.setData_criacao(System.currentTimeMillis()/1000);
            d.setData_aceito(System.currentTimeMillis()/1000);
            d.setTentativas(0);
            dd.insereDesafio(d);
        }
    }
    private void addMetaTeste(){
        Meta m = new Meta();
        MetaDAO md = new MetaDAO(this);
        for (int i = 0; i < 10; i++){
            m.setNome("Meta " + i);
            m.setDescricao("Descrição da " + m.getNome());
            m.setTipo(0);
            m.setTempo(3600000);
            m.setData(System.currentTimeMillis());
            m.setQtde(i);
            md.insereMeta(m);
        }
    }
    private void setDesafioList() {
        RecyclerView rv = (RecyclerView) findViewById(R.id.listaCompletaDesafios);
        List<Desafio> desafios = dDao.getDesafiosList(0);
        rv.setAdapter(new DesafioAdapter(desafios,this));
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setNestedScrollingEnabled(false);
    }
}
