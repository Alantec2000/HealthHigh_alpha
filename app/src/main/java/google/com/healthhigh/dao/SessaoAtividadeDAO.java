package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import google.com.healthhigh.domain.ExecucaoAtividade;
import google.com.healthhigh.domain.InteracaoAtividade;
import google.com.healthhigh.domain.SessaoAtividade;

public class SessaoAtividadeDAO extends DAO {
    public static final String
            TABLE_NAME = "hhwm_sessao_atividade",
            ID = "id_sessao_atividade",
            ID_INTERACAO_ATIVIDADE = "i_id_interacao_atividade_sessao_atividade",
            DATA_INICIO = "i_data_inicio_sessao_atividade",
            DATA_FIM = "i_data_fim_sessao_atividade";

    public SessaoAtividadeDAO(Context context) {
        super(context);
    }

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                ID + " INTEGER NOT NULL PRIMARY KEY, " +
                ID_INTERACAO_ATIVIDADE + " INTEGER DEFAULT NULL, " +
                DATA_INICIO + " INTEGER NOT NULL, " +
                DATA_FIM + " INTEGER DEFAULT NULL, " +
                "FOREIGN KEY(" + ID_INTERACAO_ATIVIDADE + ") REFERENCES " + InteracaoAtividadeDAO.TABLE_NAME + "(" + InteracaoAtividadeDAO.ID + ")" +
                ");";
    }

    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public boolean put(SessaoAtividade s_a) {
        ContentValues cv = get(s_a);
        long id = insert(TABLE_NAME, cv);
        s_a.setId(id);
        return id > 0;
    }

    private ContentValues get(SessaoAtividade s_a){
        ContentValues cv = new ContentValues();
        cv.put(DATA_INICIO, s_a.getData_inicio());
        cv.put(DATA_FIM, s_a.getData_fim());
        if(s_a.getInteracao_atividade() != null){
            cv.put(ID_INTERACAO_ATIVIDADE, s_a.getInteracao_atividade().getId());
        } else {
            cv.putNull(ID_INTERACAO_ATIVIDADE);
        }

        return cv;
    }

    public boolean update(SessaoAtividade sessao) {
        ContentValues cv = get(sessao);
        return update(TABLE_NAME, cv, ID + " = ?", new String[]{String.valueOf(sessao.getId())});
    }

    private abstract class SessaoAtividadeBehavior implements Behavior {
        protected Map<Long, SessaoAtividade> sessoes;
        protected SessaoAtividade sessao;

        public SessaoAtividadeBehavior() {
            this.sessoes = new TreeMap<>();
            this.sessao = null;
        }

        public Map<Long, SessaoAtividade> getSessoes() {
            return sessoes;
        }

        public SessaoAtividade getSessao() {
            return sessao;
        }
    }

    @Override
    protected void prepareContentReceiver() {

    }

    public static SessaoAtividade getSessaoAtividade(Cursor c){
        SessaoAtividade s_a = new SessaoAtividade();
        s_a.setId(getLong(c, ID));
        /*if(!c.isNull(c.getColumnIndex(InteracaoAtividadeDAO.ID))){
            i_a = InteracaoAtividadeDAO.get(c);
        }*/
        s_a.setData_inicio(getLong(c, DATA_INICIO));
        s_a.setData_fim(getLong(c, DATA_FIM));
        return s_a;
    }

    public SessaoAtividade getSessaoAberta(InteracaoAtividade i_a) {
        String select = "SELECT * FROM " + TABLE_NAME + " as sa " +
                "LEFT JOIN " + InteracaoAtividadeDAO.TABLE_NAME + " as ia ON " +
                "ia." + InteracaoAtividadeDAO.ID + " = sa." + ID_INTERACAO_ATIVIDADE + " " +
                "WHERE sa." + DATA_FIM + " = 0 ";
        if(i_a != null && i_a.getId() > 0){
            select += " AND ia." + InteracaoAtividadeDAO.ID + " = " + String.valueOf(i_a.getId());
        } else {
            select += " AND ia." + InteracaoAtividadeDAO.ID + " IS NULL ";
        }
        select += " ORDER BY " + ID + " DESC LIMIT 1";
        SessaoAtividadeBehavior s_b = new SessaoAtividadeBehavior() {
            @Override
            public void setContent(Cursor c) {
                Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(c));
                SessaoAtividade s_a = getSessaoAtividade(c);
                sessao = s_a;
            }
        };
        getSelectQueryContent(select, s_b);
        return s_b.sessao;
    }

    public List<SessaoAtividade> getSessoes(InteracaoAtividade i_a) {
        List<SessaoAtividade> sessoes = new ArrayList<>();
        if(i_a != null && i_a.getId() > 0){
            String select = "SELECT * FROM " + TABLE_NAME + " as sa " +
                    "INNER JOIN " + ExecucaoAtividadeDAO.TABLE_NAME + " as ea ON " +
                    "ea." + ExecucaoAtividadeDAO.ID_SESSAO_ATIVIDADE + " = sa." + ID + " " +
                    "INNER JOIN " + InteracaoAtividadeDAO.TABLE_NAME + " as ia ON " +
                "ia." + InteracaoAtividadeDAO.ID + " = sa." + ID_INTERACAO_ATIVIDADE + " " +
                "WHERE sa." + DATA_FIM + " <> 0 ";
            select += " AND ia." + InteracaoAtividadeDAO.ID + " = " + String.valueOf(i_a.getId());
            select += " ORDER BY " + ID + " DESC LIMIT 1";
            SessaoAtividadeBehavior s_b = new SessaoAtividadeBehavior() {
                @Override
                public void setContent(Cursor c) {
                    Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(c));
                    long id = getLong(c, ID);
                    SessaoAtividade sa;
                    if(!sessoes.containsKey(id)){
                        sa = getSessaoAtividade(c);
                        sessoes.put(id, sa);
                    } else {
                        sa = sessoes.get(id);
                    }
                    ExecucaoAtividade ea;
                    long id_ea = getLong(c, ExecucaoAtividadeDAO.ID);
                    if(sa.getAtividades().containsKey(id_ea)){
                        ea = sa.getAtividades().get(id_ea);
                    } else {
                        ea = ExecucaoAtividadeDAO.get(c);
                        sa.getAtividades().put(id_ea, ea);
                    }
                }
            };
            getSelectQueryContent(select, s_b);
            sessoes = new ArrayList<>(s_b.sessoes.values());
        }
        return sessoes;
    }
}
