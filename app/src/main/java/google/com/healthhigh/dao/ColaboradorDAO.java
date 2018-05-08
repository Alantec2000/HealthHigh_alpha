package google.com.healthhigh.dao;

import android.content.Context;
import android.database.Cursor;

import google.com.healthhigh.domain.Colaborador;

public class ColaboradorDAO extends DAO {
    private Colaborador cb;

    public static final String TABLE_NAME = "hh_colaborador";
    public static final String ID = "id_colaborador";
    public static final String NOME = "s_nome_colaborador";
    public static final String SENHA = "s_senha_colaborador";
    public static final String CPF = "s_cpf_colaborador";
    public static final String EMAIL = "s_email_colaborador";
    public static final String FACEBOOK = "s_facebook_acc_colaborador";
    public static final String TWITTER = "s_twitter_acc_colaborador";
    public static final String ATIVO = "ti_ativo_colaborador";
    public static final String XP = "i_xp_colaborador";
    public static final String SEXO = "s_sexo_colaborador";
    public static final String ALTURA = "i_altura_colaborador";
    public static final String TOTAL_CORRIDO = "i_total_corrido_colaborador";
    public static final String TOTAL_CAMINHADO = "i_total_caminhado_colaborador";
    public static final String TOTAL_PULADO = "i_total_pulado_colaborador";

    public ColaboradorDAO(Context context) {
        super(context);
    }

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
               ID + " INTEGER NOT NULL PRIMARY KEY, " +
               NOME + " TEXT NOT NULL, " +
               SENHA + " TEXT NOT NULL," +
               CPF + " TEXT NOT NULL," +
               EMAIL + " TEXT NOT NULL, " +
               FACEBOOK + " TEXT NOT NULL, " +
               TWITTER + " TEXT NOT NULL, " +
               SEXO + " INTEGER NOT NULL, " +
               ALTURA + " TEXT NOT NULL, " +
               ATIVO + " INTEGER NOT NULL DEFAULT 1, " +
               XP + " NUMERIC NOT NULL DEFAULT 0, " +
               TOTAL_CORRIDO + " NUMERIC NOT NULL DEFAULT 0, " +
               TOTAL_CAMINHADO + " NUMERIC NOT NULL DEFAULT 0, " +
               TOTAL_PULADO + " NUMERIC NOT NULL DEFAULT 0);";
    }

    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    @Override
    protected void setContent(Cursor c) {
        cb = getColaborador(c);
    }

    @Override
    protected void prepareContentReceiver() {
        cb = new Colaborador();
    }

    private Colaborador getColaboradorData(String select){
        getSelectQueryContent(select);
        return cb;
    }

    private Colaborador getColaborador(Cursor c){
        Colaborador cb = new Colaborador();
        cb.setNome(c.getString(c.getColumnIndex(NOME)));
        cb.setAltura(c.getString(c.getColumnIndex(ALTURA)));
        cb.setCpf(c.getString(c.getColumnIndex(CPF)));
        cb.setEmail(c.getString(c.getColumnIndex(EMAIL)));
        cb.setFacebook(c.getString(c.getColumnIndex(FACEBOOK)));
        cb.setTwitter(c.getString(c.getColumnIndex(TWITTER)));
        cb.setSexo(c.getInt(c.getColumnIndex(SEXO)));
        cb.setAtivo(c.getShort(c.getColumnIndex(ATIVO)));
        cb.setXP(c.getLong(c.getColumnIndex(XP)));
        cb.setTotal_corrido(c.getLong(c.getColumnIndex(TOTAL_CORRIDO)));
        cb.setTotal_caminhado(c.getLong(c.getColumnIndex(TOTAL_CAMINHADO)));
        cb.setTotal_pulado(c.getLong(c.getColumnIndex(TOTAL_PULADO)));
        return new Colaborador();
    }
}
