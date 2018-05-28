package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import google.com.healthhigh.domain.Atividade;
import google.com.healthhigh.domain.ExecucaoAtividade;
import google.com.healthhigh.domain.InteracaoAtividade;
import google.com.healthhigh.domain.Publicacao;
import google.com.healthhigh.domain.SessaoAtividade;
import google.com.healthhigh.utils.DataHelper;

public class InteracaoAtividadeDAO extends DAO {
    public static final String
            TABLE_NAME = "hhwm_interacao_atividade",
            ID = "id_interacao_atividade",
            ID_ATIVIDADE = "i_id_atividade_interacao_atividade",
            ID_PUBLICACAO = "i_id_publicacao_interacao_atividade",
            DATA_INICIO = "i_data_inicio_interacao_atividade",
            DATA_CRIACAO = "i_data_criacao_interacao_atividade",
            DATA_CONCLUSAO = "i_data_conclusao_interacao_atividade";

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                ID + " INTEGER NOT NULL PRIMARY KEY, " +
                ID_ATIVIDADE + " INTEGER NOT NULL, " +
                ID_PUBLICACAO + " INTEGER NOT NULL, " +
                DATA_CRIACAO + " INTEGER NOT NULL, " +
                DATA_INICIO + " INTEGER NOT NULL, " +
                DATA_CONCLUSAO + " INTEGER NOT NULL DEFAULT 0, " +
                "UNIQUE(" + ID_PUBLICACAO + ", " + ID_ATIVIDADE + ")" +
                "FOREIGN KEY(" + ID_ATIVIDADE + ") REFERENCES " + AtividadeDAO.TABLE_NAME + "(" + AtividadeDAO.ID + ") ON DELETE CASCADE, " +
                "FOREIGN KEY(" + ID_PUBLICACAO + ") REFERENCES " + PublicacaoDAO.TABLE_NAME + "(" + PublicacaoDAO.ID + ") ON DELETE CASCADE " +
                ");";
    }

    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public InteracaoAtividadeDAO(Context context) {
        super(context);
    }



    private abstract static class InteracaoAtividadeBehavior implements Behavior {
        protected InteracaoAtividade interacao;
        protected Map<Long, InteracaoAtividade> interacoes;

        public InteracaoAtividadeBehavior() {
            interacao = null;
            interacoes = new TreeMap<>();
        }

        public InteracaoAtividade getInteracao() {
            return interacao;
        }

        public Map<Long, InteracaoAtividade> getInteracoes() {
            return interacoes;
        }
    }

    @Override
    protected void prepareContentReceiver() {
    }

    public InteracaoAtividade get(Atividade a, Publicacao p) {
        InteracaoAtividade i_a = null;
        if(a != null && p != null){
            String select = "SELECT * FROM " + TABLE_NAME + " as ia " +
                    "INNER JOIN " + AtividadeDAO.TABLE_NAME + " as a ON " +
                    "a." + AtividadeDAO.ID + " = ia." + InteracaoAtividadeDAO.ID_ATIVIDADE + " " +
                    "INNER JOIN " + PublicacaoDAO.TABLE_NAME + " as p ON " +
                    "p." + PublicacaoDAO.ID + " = ia." + ID_PUBLICACAO + " " +
                    "WHERE p." + PublicacaoDAO.ID + " = " + String.valueOf(p.getId()) + " AND " +
                    "a." + AtividadeDAO.ID + " = " + String.valueOf(a.getId()) + " LIMIT 1;";
            InteracaoAtividadeBehavior i_a_b = new InteracaoAtividadeBehavior() {
                @Override
                public void setContent(Cursor c) {
                    interacao = get(c);
                }
            };
            getSelectQueryContent(select, i_a_b);
            i_a = i_a_b.interacao;
        }
        return i_a;
    }

    // Função que atualiza as interações das atividades de acordo com a publicação passada
    public void getInteracaoAtividade(final Map<Long, Atividade> atividades, final Publicacao p) {
        if((atividades != null && atividades.size() > 0) && (p != null && p.getId() > 0)){
            String in = "(%s)";
            List<String> aux = new ArrayList<>();
            for(Atividade a : atividades.values()){
                aux.add(String.valueOf(a.getId()));
            }
            String columns[] = {
                    "p." + PublicacaoDAO.ID,
                    "a." + AtividadeDAO.ID,
                    "ia.*", "sa.*", "ea.*"
            };
            in = String.format(in, TextUtils.join(",",aux));
            String select = "SELECT DISTINCT " + createColumns(columns) + " FROM " + TABLE_NAME + " as ia " +
                    "INNER JOIN " + PublicacaoDAO.TABLE_NAME + " as p ON " +
                    "p." + PublicacaoDAO.ID + " = ia." + InteracaoAtividadeDAO.ID_PUBLICACAO + " " +
                    "INNER JOIN " + AtividadeDAO.TABLE_NAME + " as a ON " +
                    "a." + AtividadeDAO.ID + " = ia." + InteracaoAtividadeDAO.ID_ATIVIDADE + " " +
                    "LEFT JOIN " + SessaoAtividadeDAO.TABLE_NAME + " as sa ON " +
                    "sa." + SessaoAtividadeDAO.ID_INTERACAO_ATIVIDADE + " = ia." + InteracaoAtividadeDAO.ID + " " +
                    "LEFT JOIN " + ExecucaoAtividadeDAO.TABLE_NAME + " as ea ON " +
                    "ea." + ExecucaoAtividadeDAO.ID_SESSAO_ATIVIDADE + " = sa." + SessaoAtividadeDAO.ID + " " +
                    "WHERE " +
                    "a." + AtividadeDAO.ID + " IN " + in + " AND " +
                    PublicacaoDAO.ID + " = " + String.valueOf(p.getId());
            Log.v("Select com in", select);
            getSelectQueryContent(select, new InteracaoAtividadeBehavior() {
                @Override
                public void setContent(Cursor c) {
                    Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(c));
                    long a_id = getLong(c, AtividadeDAO.ID);
                    Atividade a = atividades.get(a_id);
                    if(a.getInteracao_atividade() == null){
                        InteracaoAtividade interacao = get(c);
                        a.setInteracao_atividade(interacao);
                        interacao.setPublicacao(p);
                        interacao.setAtividade(a);
                    } else {
                        InteracaoAtividade interacao = a.getInteracao_atividade();
                        if(interacao.getSessoes_atividade() == null){
                            interacao.setSessoes_atividade(new TreeMap<Long, SessaoAtividade>());
                        }
                        if(!c.isNull(c.getColumnIndex(SessaoAtividadeDAO.ID))){
                            long id_s_a = getLong(c, SessaoAtividadeDAO.ID);
                            SessaoAtividade s_a;
                            if(!interacao.getSessoes_atividade().containsKey(id_s_a)){
                                s_a = SessaoAtividadeDAO.getSessaoAtividade(c);
                                interacao.getSessoes_atividade().put(id_s_a, s_a);
                            } else {
                                s_a = interacao.getSessoes_atividade().get(id_s_a);
                                s_a.setInteracao_atividade(interacao);
                            }
                            if(s_a != null){
                                long id_e_a = getLong(c, ExecucaoAtividadeDAO.ID);
                                ExecucaoAtividade e_a;
                                if(s_a.getAtividades().containsKey(id_e_a)){
                                    e_a = s_a.getAtividades().get(id_e_a);
                                } else {
                                    e_a = ExecucaoAtividadeDAO.get(c);
                                    e_a.setSessao_atividade(s_a);
                                    s_a.getAtividades().put(id_e_a, e_a);
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    public InteracaoAtividade get(SessaoAtividade s_a){
        String select = "SELECT * FROM " + TABLE_NAME + " as ia " +
                "INNER JOIN " + SessaoAtividadeDAO.TABLE_NAME + " as sa ON " +
                "sa." + SessaoAtividadeDAO.ID_INTERACAO_ATIVIDADE + " = ia." + ID + " " +
                "WHERE sa." + SessaoAtividadeDAO.ID + " = " + String.valueOf(s_a.getId());
        InteracaoAtividadeBehavior i_a_b = new InteracaoAtividadeBehavior() {
            @Override
            public void setContent(Cursor c) {
                InteracaoAtividade i_a = get(c);
            }
        };
        return i_a_b.interacao;
    }

    public static InteracaoAtividade get(Cursor c) {
        InteracaoAtividade i_a = new InteracaoAtividade(null);
        i_a.setId(getLong(c, ID));
        i_a.setData_conclusao(getLong(c, DATA_CONCLUSAO));
        i_a.setData_inicio(getLong(c, DATA_INICIO));
        return i_a;
    }

    public boolean inserir(InteracaoAtividade i_a) throws Exception {
        ContentValues cv = getContentValues(i_a);
        long id = insert(TABLE_NAME, cv);
        if(id > 0) {
            i_a.setId(id);
        } else {
            i_a = null;
        }
        return id > 0;
    }

    public boolean atualizar(InteracaoAtividade i_a) throws Exception {
        ContentValues cv = getContentValues(i_a);
        return update(TABLE_NAME, cv, ID + " = ?", new String[] {String.valueOf(i_a.getId())});
    }

    private ContentValues getContentValues(InteracaoAtividade i_a) throws Exception {
        ContentValues cv = new ContentValues();
        if(i_a.getIdMeta() > 0){
            cv.put(ID_ATIVIDADE, i_a.getIdMeta());
        } else {
            throw new Exception("Nenhuma atividade informada para interação!");
        }
        if(i_a.getIdPublicacao() > 0){
            cv.put(ID_PUBLICACAO, i_a.getIdPublicacao());
        } else {
            throw new Exception("Nenhuma publicação informada para interação!");
        }
        cv.put(DATA_CONCLUSAO, i_a.getData_conclusao());
        cv.put(DATA_CRIACAO, i_a.getData_criacao());
        cv.put(DATA_INICIO, i_a.getData_inicio());
        return cv;
    }
}
