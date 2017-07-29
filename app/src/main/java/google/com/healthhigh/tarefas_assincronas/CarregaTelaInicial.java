package google.com.healthhigh.tarefas_assincronas;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import java.util.List;

import google.com.healthhigh.adapter.DesafioAdapter;
import google.com.healthhigh.dao.DesafioDAO;
import google.com.healthhigh.domain.Desafio;

public class CarregaTelaInicial extends AsyncTask<Void, Void, List<Desafio>> {
    private Context c;
    private RecyclerView rv;

    public CarregaTelaInicial(Context c, RecyclerView rv){
        this.c = c;
        this.rv = rv;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Desafio> doInBackground(Void... params) {
        DesafioDAO dao = new DesafioDAO(c);
        List<Desafio> desafios = dao.getDesafiosList(3);
        return desafios;
    }

    @Override
    protected void onPostExecute(List<Desafio> desafios) {
        super.onPostExecute(desafios);
        rv.setAdapter(new DesafioAdapter(desafios,c));
        rv.setLayoutManager(new LinearLayoutManager(c));
        rv.setNestedScrollingEnabled(false);
    }
}
