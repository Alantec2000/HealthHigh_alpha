package google.com.healthhigh.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.healthhigh.R;

import google.com.healthhigh.domain.TipoMeta;

public class TipoMetaViewHolder extends RecyclerView.ViewHolder {
    private TipoMeta meta;
    private TextView txt_status_meta, txt_nome_meta, txt_tipo_meta;
    public TipoMetaViewHolder(View itemView) {
        super(itemView);
        txt_tipo_meta = (TextView) itemView.findViewById(R.id.txt_tipo_meta_desafio);
        txt_nome_meta = (TextView) itemView.findViewById(R.id.txt_nome_meta_desafio);
        txt_status_meta = (TextView) itemView.findViewById(R.id.txt_status_meta_desafio);
    }

    public TipoMeta getMeta() {
        return meta;
    }

    public void setMeta(TipoMeta meta) {
        this.meta = meta;
    }

    public TextView getTxt_status_meta() {
        return txt_status_meta;
    }

    public TextView getTxt_nome_meta() {
        return txt_nome_meta;
    }

    public TextView getTxt_tipo_meta() {
        return txt_tipo_meta;
    }


}
