package google.com.healthhigh.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.healthhigh.R;

import java.util.List;

import google.com.healthhigh.adapter.DesafioAdapter;
import google.com.healthhigh.controller.DesafioController;
import google.com.healthhigh.dao.DesafioDAO;
import google.com.healthhigh.dao.MetaDAO;
import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.domain.Meta;
import google.com.healthhigh.utils.DataHelper;

public class ListaDesafiosActivity extends AppCompatActivity {
    private DesafioController d_c;
    Intent intent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_desafios);
        d_c = new DesafioController(this);
//        addDesafioTeste();
//        addMetaTeste();
        setDesafioList();
    }

    private void addDesafioTeste(){
        Desafio d = new Desafio();
        int[] quantidades = {10, 50, 100};
        DesafioDAO dd = new DesafioDAO(this);
        for (int i = 1; i <= 3; i++){
            d.setTitulo("Desafio " + i);
            d.setDescricao("Dê " + quantidades[i-1] + " passos para ficar mais saudável!");
            d.setTipo(1);
            d.setQuantidade(quantidades[i-1]);
            d.setAceito(false);
            d.setData_criacao(DataHelper.now());
            d.setData_aceito(DataHelper.now());
            d.setTentativas(0);
            dd.insereDesafio(d);
        }
    }
    private void addMetaTeste(){
        Meta m = new Meta();
        MetaDAO md = new MetaDAO(this);
        for (int i = 1; i < 10; i++){
            m.setNome("Meta " + i);
            m.setDescricao("Descrição da " + m.getNome());
            m.setTipo(0);
            m.setData(DataHelper.now());
            m.setQtde(i);
            md.insereMeta(m);
        }
    }
    private void setDesafioList() {
        RecyclerView rv = (RecyclerView) findViewById(R.id.listaCompletaDesafios);
        List<Desafio> desafios = d_c.getDesafios(0l);
        rv.setAdapter(new DesafioAdapter(desafios, this));
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setNestedScrollingEnabled(false);
    }
}
