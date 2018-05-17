package google.com.healthhigh.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.healthhigh.R;

import java.util.Arrays;
import java.util.List;

import google.com.healthhigh.domain.InteracaoNoticia;
import google.com.healthhigh.domain.InteracaoQuestionario;
import google.com.healthhigh.domain.Noticia;
import google.com.healthhigh.domain.Questionario;
import google.com.healthhigh.domain.TipoMeta;
import google.com.healthhigh.viewholders.MetaViewHolder;
import google.com.healthhigh.viewholders.TipoMetaViewHolder;

/**
 * Created by Alan on 26/07/2017.
 */

public class TipoMetaAdapter extends RecyclerView.Adapter {
    private String E_TAG = "MetaListAdapter";
    private List<TipoMeta> metas;
    private Context c;
    public TipoMetaAdapter(Context c, List<TipoMeta> m){
        this.c = c;
        this.metas = m;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.layout_linha_tipo_meta, parent, false);
        return new TipoMetaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TipoMeta tm = metas.get(position);
        TipoMetaViewHolder tm_vh = (TipoMetaViewHolder) holder;
        tm_vh.setMeta(tm);
        String status = "Status: Indefinido";
        switch (tm.getTipo()){
            case TipoMeta.QUESTIONARIO:
                Questionario q = (Questionario) tm;
                tm_vh.getTxt_tipo_meta().setText("Questionário");
                tm_vh.getTxt_nome_meta().setText(q.getTitulo());
                if(q.getInteracao_questionario() != null){
                    status = q.getInteracao_questionario().statusQuestionario();
                }
            break;
            case TipoMeta.NOTICIA:
                Noticia n = (Noticia) tm;
                tm_vh.getTxt_tipo_meta().setText("Notícia");
                tm_vh.getTxt_nome_meta().setText(n.getTitulo());
                if(n.getInteracao_noticia() != null){
                    status = n.getInteracao_noticia().statusNoticia();
                }
            break;
            case TipoMeta.ATIVIDADE:
                tm_vh.getTxt_nome_meta().setText("Atividade");
            break;
            case TipoMeta.EVENTO:
                tm_vh.getTxt_nome_meta().setText("Evento");
            break;
        }
        if(Arrays.asList(InteracaoNoticia.LIDA, InteracaoQuestionario.FINALIZADO).contains(status)){
            tm_vh.itemView.setBackgroundColor(Color.parseColor("#EEFFEE"));
        }
        tm_vh.getTxt_status_meta().setText("Status: "+status);
    }

    @Override
    public int getItemCount() {
        return this.metas.size();
    }
}
