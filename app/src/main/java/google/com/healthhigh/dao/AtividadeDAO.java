package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;

import java.util.Map;
import java.util.TreeMap;

import google.com.healthhigh.domain.Atividade;
import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.utils.DataHelper;

public class AtividadeDAO extends DAO {
    public static final String
            TABLE_NAME = "hhwm_atividade",
            ID = "id_atividade",
            ID_PREMIACAO= "i_id_premiacao_atividade",
            DATA_CRIACAO = "i_data_criacao_atividade",
            NOME = "s_nome_atividade",
            DESCRICAO = "s_descricao_atividade",
            DATA_VISUALIZACAO = "i_data_visualizacao_atividade",
            TEMPO_TOTAL = "i_tempo_total_atividade",
            TOTAL_PASSOS = "i_total_passos_atividade";

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                ID + " INTEGER NOT NULL PRIMARY KEY, " +
                ID_PREMIACAO + " INTEGER NULL, " +
                DATA_CRIACAO + " INTEGER DEFAULT 0, " +
                NOME + " TEXT NOT NULL, " +
                DESCRICAO + " TEXT NOT NULL, " +
                DATA_VISUALIZACAO + " INTEGER DEFAULT 0, " +
                TEMPO_TOTAL + " INTEGER NOT NULL, " +
                TOTAL_PASSOS + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + ID_PREMIACAO + ") REFERENCES " + PremiacaoDAO.TABLE_NAME + "(" + PremiacaoDAO.ID + ")" +
                ");";
    }

    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public Map<Long, Atividade> getAtividadesDesafioAtual(Desafio d) {
        Map<Long, Atividade> atividades = new TreeMap<>();
        if(d != null && d.getId() > 0) {
            String select = "SELECT * FROM " + TABLE_NAME + " as a " +

                    "INNER JOIN " + DesafioAtividadeDAO.TABLE_NAME + " as da ON " +
                        "da." + DesafioAtividadeDAO.ID_ATIVIDADE + " = a." + AtividadeDAO.ID + " " +

                    "INNER JOIN " + DesafioDAO.TABLE_NAME + " as d " +
                        "d." + DesafioDAO.ID + " = da." + DesafioAtividadeDAO.ID_DESAFIO + " " +

                    "INNER JOIN " + PublicacaoDAO.TABLE_NAME + " as p ON " +
                        "p." + PublicacaoDAO.ID_DESAFIO + " = d." + DesafioDAO.ID + " " +

                    "INNER JOIN " + InteracaoDesafioDAO.TABLE_NAME + " as i_d ON " +
                        "i_d." + InteracaoDesafioDAO.ID_DESAFIO + " = d." + DesafioDAO.ID + " AND " +
                        "i_d." + InteracaoDesafioDAO.ID_PUBLICACAO + " = p." + PublicacaoDAO.ID + " " +

                    "INNER JOIN " + InteracaoAtividadeDAO.TABLE_NAME + " as ia ON " +
                    "ia." + InteracaoAtividadeDAO.ID_PUBLICACAO + " = p." + PublicacaoDAO.ID + " AND " +
                    "ia." + InteracaoAtividadeDAO.ID_ATIVIDADE + " = a." + AtividadeDAO.ID + " " +

                    "WHERE d." + DesafioDAO.ID + " = " + String.valueOf(d.getId()) +
                    "ia." + InteracaoDesafioDAO.REALIZANDO + " AND " +
                    DataHelper.now() + " BETWEEN p." + PublicacaoDAO.DATA_INICIO + " AND " + PublicacaoDAO.DATA_FIM;
            AtividadeBehavior a_b = new AtividadeBehavior() {
                @Override
                public void setContent(Cursor c) {
                    Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(c));
                }
            };
            getSelectQueryContent(select, a_b);
        }
        return atividades;
    }

    public abstract class AtividadeBehavior implements Behavior {
        protected final Map<Long, Atividade> atividades;
        protected final Atividade atividade;
        public AtividadeBehavior() {
            atividades = new TreeMap<>();
            atividade = new Atividade();
        }
    }

    public AtividadeDAO(Context context) {
        super(context);
    }

    @Override
    protected void prepareContentReceiver() {

    }

    // Função: Obter uma lista de atividades a partir do desafio que está sendo realizado no momento
    public Map<Long, Atividade> getAtividades(Desafio d) {
        String select =
                "SELECT * FROM " + TABLE_NAME + " as a " +
                "INNER JOIN " + DesafioAtividadeDAO.TABLE_NAME + " as da ON " +
                    "da." + DesafioAtividadeDAO.ID_ATIVIDADE + " = a." + AtividadeDAO.ID + " " +
                "INNER JOIN " + DesafioDAO.TABLE_NAME + " as d ON " +
                    "d." + DesafioDAO.ID + " = da." + DesafioAtividadeDAO.ID_DESAFIO;
        if(d != null){
            select += " WHERE d." + DesafioDAO.ID + " = " + String.valueOf(d.getId());
        }

        AtividadeBehavior a_b = new AtividadeBehavior() {
            @Override
            public void setContent(Cursor c) {
                long id = getLong(c, ID);
                Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(c));
                Atividade a = null;
                if(atividades.containsKey(id)){
                    a = atividades.get(id);
                    Log.i("Atividade desafio", "Atividade: " + a.getNome());
                } else {
                    a = getAtividade(c);
                    atividades.put(id, a);
                    Log.i("Atividade desafio", "Atividade: " + a.getNome());
                }
            }
        };
        getSelectQueryContent(select, a_b);
        return a_b.atividades;
    }

    private void inserirAtividade(Atividade a){
        ContentValues cv = getContentValues(a);
    }

    private ContentValues getContentValues(Atividade a) {
        ContentValues cv = new ContentValues();
        if(a.getIdPremiacao() > 0) {
            cv.put(ID_PREMIACAO, a.getIdPremiacao());
        } else {
            cv.putNull(ID_PREMIACAO);
        }
        cv.put(DATA_CRIACAO, a.getData_criacao());
        cv.put(DATA_VISUALIZACAO, a.getData_visualizacao());
        cv.put(TEMPO_TOTAL, a.getTotal_tempo_execucao());
        cv.put(TOTAL_PASSOS, a.getTotal_passos_dados());
        return cv;
    }

    private Atividade getAtividade(Cursor c) {
        Atividade a = new Atividade();
        a.setId(getLong(c, ID));
        a.setNome(getString(c, DESCRICAO));
        a.setDescricao(getString(c, DESCRICAO));
        a.setTotal_passos_dados(getInt(c, TOTAL_PASSOS));
        a.setTotal_tempo_execucao(getLong(c, TEMPO_TOTAL));
        a.setData_criacao(getLong(c, DATA_CRIACAO));
        a.setData_visualizacao(getLong(c, DATA_VISUALIZACAO));
        return a;
    }
}
