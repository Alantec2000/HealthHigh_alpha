package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.Map;
import java.util.TreeMap;

import google.com.healthhigh.domain.ExecucaoAtividade;
import google.com.healthhigh.domain.InteracaoAtividade;
import google.com.healthhigh.domain.SessaoAtividade;

public class ExecucaoAtividadeDAO extends DAO {
    public static final String
            TABLE_NAME = "hhwm_execucao_atividade",
            ID = "id_execucao_atividade",
            ID_SESSAO_ATIVIDADE = "i_id_sessao_atividade_execucao_atividade",
            NUMERO_PASSOS = "i_numero_passos",
            TEMPO_TOTAL = "i_tempo_total";

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                ID + " INTEGER NOT NULL PRIMARY KEY, " +
                ID_SESSAO_ATIVIDADE + " INTEGER NOT NULL, " +
                NUMERO_PASSOS + " INTEGER NOT NULL DEFAULT 0, " +
                TEMPO_TOTAL + " INTEGER NOT NULL DEFAULT 0, " +
                "FOREIGN KEY(" + ID_SESSAO_ATIVIDADE + ") REFERENCES " + SessaoAtividadeDAO.TABLE_NAME + "(" + SessaoAtividadeDAO.ID + ") ON DELETE CASCADE " +
                ");";
    }

    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public ExecucaoAtividadeDAO(Context context) {
        super(context);
    }

    public boolean insert(ExecucaoAtividade execucao) throws Exception {
        ContentValues cv = getContentValues(execucao);
        long id = insert(TABLE_NAME, cv);
        execucao.setId(id);
        return id > 0;
    }

    private ContentValues getContentValues(ExecucaoAtividade execucao) throws Exception {
        ContentValues cv = new ContentValues();
        if(execucao.getSessao_atividade() != null && execucao.getSessao_atividade().getId() > 0){
            cv.put(ID_SESSAO_ATIVIDADE, execucao.getSessao_atividade().getId());
        } else {
            throw new Exception("Nenhuma sessão informada!");
        }
        cv.put(TEMPO_TOTAL, execucao.getTempo_execucao());
        cv.put(NUMERO_PASSOS, execucao.getTotal_passos_dados());
        return cv;
    }

    public boolean update(ExecucaoAtividade execucao) {
        boolean resposta = false;
        try {
            ContentValues cv = getContentValues(execucao);
            resposta = update(TABLE_NAME, cv, ID + " = ?", new String[]{String.valueOf(execucao.getId())});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resposta;
    }

    private abstract static class ExecucaoAtividadeBehavior implements Behavior {
        protected ExecucaoAtividade atividade;
        protected Map<Long, ExecucaoAtividade> atividades;

        public ExecucaoAtividadeBehavior() {
            this.atividades = new TreeMap<>();
        }
    }

    @Override
    protected void prepareContentReceiver() {

    }

    public static ExecucaoAtividade get(Cursor c){
        ExecucaoAtividade e_a = new ExecucaoAtividade();
        e_a.setId(getLong(c, ID));
        e_a.setTotal_passos_dados(getInt(c, NUMERO_PASSOS));
        e_a.setTempo_execucao(getLong(c, TEMPO_TOTAL));
        return e_a;
    }

    public Map<Long, ExecucaoAtividade> get(final SessaoAtividade s_a) {
        Map<Long, ExecucaoAtividade> atividades = new TreeMap<>();
        if(s_a != null){
            String select = "SELECT * FROM " + TABLE_NAME + " as ea " +
                    "INNER JOIN " + SessaoAtividadeDAO.TABLE_NAME + " as sa  ON " +
                    "sa." + SessaoAtividadeDAO.ID + " = ea." + ID_SESSAO_ATIVIDADE + " " +
                    "WHERE sa." + SessaoAtividadeDAO.ID + " = " + String.valueOf(s_a.getId());
            ExecucaoAtividadeBehavior e_b = new ExecucaoAtividadeBehavior() {
                @Override
                public void setContent(Cursor c) {
                    long id = getLong(c, ID);
                    ExecucaoAtividade e_a;
                    if(atividades.containsKey(id)){
                        e_a = atividades.get(id);
                    } else {
                        e_a = get(c);
                        atividades.put(id, e_a);
                    }
                    e_a.setSessao_atividade(s_a);
                }
            };
            getSelectQueryContent(select, e_b);
            atividades = e_b.atividades;
        }
        return atividades;
    }
}
