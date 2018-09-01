package google.com.healthhigh.tarefas_assincronas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import google.com.healthhigh.adapter.ItemGridAdapter;
import google.com.healthhigh.controller.PremiacaoController;
import google.com.healthhigh.domain.Item;
import google.com.healthhigh.domain.Premiacao;
import google.com.healthhigh.utils.ItemList;

public class CarregaListaMedalhas extends AsyncTask<Void, String, List<Premiacao>> {
    private Context c;
    private RecyclerView rv;
    private ProgressDialog p;
    public CarregaListaMedalhas(Context c, RecyclerView rv){
        this.c = c;
        this.rv = rv;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        p = new ProgressDialog(c);
        p.setMessage("Carregando Lista...");
        p.show();
    }
    @Override
    protected List<Premiacao> doInBackground(Void... params) {
        PremiacaoController p_c = new PremiacaoController(c);
        List<Premiacao> itens = p_c.getListaPremiacoes();
        return itens;
    }

    @Override
    protected void onProgressUpdate(String... msg) {
        super.onProgressUpdate(msg);
        p.setMessage(msg.toString());
    }

    @Override
    protected void onPostExecute(List<Premiacao> itens) {
        super.onPostExecute(itens);
        rv.setAdapter(new ItemGridAdapter(itens,c));
        rv.setLayoutManager(new GridLayoutManager(c, 2));
        rv.setNestedScrollingEnabled(false);
        p.dismiss();
    }
}
