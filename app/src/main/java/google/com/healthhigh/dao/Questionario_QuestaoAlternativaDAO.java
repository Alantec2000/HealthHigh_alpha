package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import google.com.healthhigh.domain.Questao;
import google.com.healthhigh.domain.Questionario;
import google.com.healthhigh.domain.Resposta;

/**
 * Created by Alan on 22/10/2017.
 */

public class Questionario_QuestaoAlternativaDAO extends DAO {
    public Questionario questionario;
    public List<Questionario> lista_questionarios;
    public List<Questao> lista_questoes;
    public Questao questao;

    public static final String TABLE_NAME = "phh_questionario_questao_alternativa";
    public static final String ID = "id_questionario_questao_alternativa";
    public static final String ID_QUESTIONARIO = "id_questionario_questionario_questao_alternativa";
    public static final String ID_QUESTAO = "id_questao_questionario_questao_alternativa";

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY NOT NULL, " +
                ID_QUESTAO + " INTEGER NOT NULL, " +
                ID_QUESTIONARIO + " INTEGER NOT NULL, " +
                "UNIQUE(" + ID_QUESTAO + "," + ID_QUESTIONARIO + ") ON CONFLICT IGNORE, " +
                "FOREIGN KEY(" + ID_QUESTAO + ") REFERENCES " + QuestaoAlternativaDAO.TABLE_NAME + "(" + QuestaoAlternativaDAO.ID + ")" +
                "FOREIGN KEY(" + ID_QUESTIONARIO + ") REFERENCES " + QuestionarioDAO.TABLE_NAME + "(" + QuestionarioDAO.ID + ")" +
                ");";
    }

    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public Questionario_QuestaoAlternativaDAO(Context context) {
        super(context);
    }

    @Override
    protected void prepareContentReceiver() {
        questionario = new Questionario();
        lista_questionarios = new ArrayList<>();
        lista_questoes = new ArrayList<>();
        questionario.setQuestoes(lista_questoes);
        questao = new Questao();
    }

    /*public Questionario getQuestionarioQuestoesRespostas(long id){
        *//* Obtém um questionário, suas questões e respostas associadas *//*
        String sql = "SELECT * FROM " + QuestionarioDAO.TABLE_NAME + " as qio " +
                " LEFT JOIN " + Questionario_QuestaoAlternativaDAO.TABLE_NAME + " as qio_qao ON qio." + QuestionarioDAO.ID + " = qio_qao." + ID_QUESTIONARIO +
                " LEFT JOIN " + QuestaoAlternativaDAO.TABLE_NAME + " qao ON qao." + QuestaoAlternativaDAO.ID + " = qio_qao." + Questionario_QuestaoAlternativaDAO.ID_QUESTAO +
                " LEFT JOIN " + RespostaDAO.TABLE_NAME + " r ON r." + RespostaDAO.ID_QUESTIONARIO + " = qao." + QuestaoAlternativaDAO.ID +
                " WHERE qio." + QuestionarioDAO.ID + " = " + Long.toString(id) + ";";

        getSelectQueryContent(sql, new Behavior(){
            @Override
            public void setContent(Cursor c) {
                Resposta r = null;
                QuestaoAlte q = null;
                if(!c.isNull(c.getColumnIndex(QuestaoAlternativaDAO.ID))){
                    q = QuestaoAlternativaDAO.getQuestao(c);

                    if(!c.isNull(c.getColumnIndex(RespostaDAO.ID))){
                        r = RespostaDAO.getResposta(c);
                        if(q != null) q.setResposta(r);
                    }
                }

                if(questionario.getId() == 0){
                    questionario = QuestionarioDAO.getQuestionario(c);
                }

                if(questionario.getId() > 0 && q != null){
                    q.setQuestionario(questionario);
                    questionario.getQuestoes().add(q);
                }
            }
        });
        return questionario;
    }
*/
    public void insertQuestaoInQuestionario(Questionario q, Questao qs){
        write_db.insert(TABLE_NAME, null, createInsert(q, qs));
    }

    public void insertQuestaoInQuestionario(Questionario questionario){
        questionario.setId(write_db.insert(QuestionarioDAO.TABLE_NAME, null, createInstertQuestionario(questionario)));
        for(Questao questao : questionario.getQuestoes()){
            questao.setId(write_db.insert(QuestaoAlternativaDAO.TABLE_NAME, null,  createInstertQuestao(questao)));
            write_db.insert(TABLE_NAME, null, createInsert(questionario, questao));
        }
    }

    private ContentValues createInsert(Questionario q, Questao qs){
        ContentValues cv = new ContentValues();
        cv.put(ID_QUESTAO, qs.getId());
        cv.put(ID_QUESTIONARIO, q.getId());
        return cv;
    }

    private ContentValues createInstertQuestao(Questao q){
        ContentValues cv = new ContentValues();
        cv.put(QuestaoAlternativaDAO.DESCRICAO, q.getDescricao());
        return cv;
    }

    private ContentValues createInstertQuestionario(Questionario q){
        ContentValues cv = new ContentValues();
        cv.put(QuestionarioDAO.TITULO, q.getTitulo());
        cv.put(QuestionarioDAO.DESCRICAO, q.getDescricao());
        return cv;
    }
}
