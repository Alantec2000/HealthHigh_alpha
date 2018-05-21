package google.com.healthhigh.db;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import google.com.healthhigh.dao.AlternativaDAO;
import google.com.healthhigh.dao.AtividadeDAO;
import google.com.healthhigh.dao.ColaboradorDAO;
import google.com.healthhigh.dao.DesafioDAO;
import google.com.healthhigh.dao.DesafioNoticiaDAO;
import google.com.healthhigh.dao.DesafioQuestionarioDAO;
import google.com.healthhigh.dao.DesafioXMetaDAO;
import google.com.healthhigh.dao.ExecucaoAtividadeDAO;
import google.com.healthhigh.dao.InteracaoAtividadeDAO;
import google.com.healthhigh.dao.InteracaoDesafioDAO;
import google.com.healthhigh.dao.InteracaoNoticiaDAO;
import google.com.healthhigh.dao.InteracaoQuestionarioDAO;
import google.com.healthhigh.dao.ItemDAO;
import google.com.healthhigh.dao.ItemXMetaDAO;
import google.com.healthhigh.dao.MetaDAO;
import google.com.healthhigh.dao.NoticiaDAO;
import google.com.healthhigh.dao.PremiacaoDAO;
import google.com.healthhigh.dao.PublicacaoDAO;
import google.com.healthhigh.dao.QuestaoAlternativaDAO;
import google.com.healthhigh.dao.QuestaoOpinativaDAO;
import google.com.healthhigh.dao.QuestaoOptativaDAO;
import google.com.healthhigh.dao.QuestionarioDAO;
import google.com.healthhigh.dao.QuestionarioQuestaoOpinativaDAO;
import google.com.healthhigh.dao.Questionario_QuestaoAlternativaDAO;
import google.com.healthhigh.dao.Questionario_QuestaoOptativaDAO;
import google.com.healthhigh.dao.RespostaAlternativaDAO;
import google.com.healthhigh.dao.RespostaDAO;
import google.com.healthhigh.dao.RespostaOpinativaDAO;
import google.com.healthhigh.dao.RespostaOptativaDAO;
import google.com.healthhigh.domain.ExecucaoAtividade;

public class CreateDB extends SQLiteOpenHelper{
    private static String NOME_BANCO = "healthHigh";
    private static int VERSAO = 23;
    private static CreateDB db = null;

    public static CreateDB getDBInstance(Context context){
        if(db == null)
            db = new CreateDB(context);
        return db;
    }

    public CreateDB(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables(db);
        createTables(db);
    }

    private void createTables(SQLiteDatabase db) {
        db.execSQL(ColaboradorDAO.getCreateTableString());
        db.execSQL(PremiacaoDAO.getCreateTableString());
        db.execSQL(DesafioDAO.getCreateTableString());
        db.execSQL(PublicacaoDAO.getCreateTableString());

        db.execSQL(AtividadeDAO.getCreateTableString());
        db.execSQL(InteracaoAtividadeDAO.getCreateTableString());
        db.execSQL(ExecucaoAtividadeDAO.getCreateTableString());

        db.execSQL(InteracaoDesafioDAO.getCreateTableString());
        db.execSQL(DesafioXMetaDAO.getCreateTableString());
        db.execSQL(MetaDAO.getCreateTableString());
        db.execSQL(ItemDAO.getCreateTableString());
        db.execSQL(ItemXMetaDAO.getCreateTableString());
        db.execSQL(NoticiaDAO.getCreateTableString());
        db.execSQL(DesafioNoticiaDAO.getCreateTableString());
        db.execSQL(InteracaoNoticiaDAO.getCreateTableString());

        db.execSQL(QuestionarioDAO.getCreateTableString());
        db.execSQL(DesafioQuestionarioDAO.getCreateTableString());
        db.execSQL(InteracaoQuestionarioDAO.getCreateTableString());

        db.execSQL(Questionario_QuestaoAlternativaDAO.getCreateTableString());
        db.execSQL(Questionario_QuestaoOptativaDAO.getCreateTableString());
        db.execSQL(QuestionarioQuestaoOpinativaDAO.getCreateTableString());

        db.execSQL(QuestaoAlternativaDAO.getCreateTableString());
        db.execSQL(AlternativaDAO.getCreateTableString());
        db.execSQL(QuestaoOpinativaDAO.getCreateTableString());
        db.execSQL(QuestaoOptativaDAO.getCreateTableString());

        db.execSQL(RespostaDAO.getCreateTableString());
        db.execSQL(RespostaAlternativaDAO.getCreateTableString());
        db.execSQL(RespostaOptativaDAO.getCreateTableString());
        db.execSQL(RespostaOpinativaDAO.getCreateTableString());
    }

    private void dropTables(SQLiteDatabase db) {
        db.execSQL(InteracaoNoticiaDAO.getDropTableString());
        db.execSQL(DesafioNoticiaDAO.getDropTableString());
        db.execSQL(NoticiaDAO.getDropTableString());

        db.execSQL(RespostaDAO.getDropTableString());
        db.execSQL(RespostaAlternativaDAO.getDropTableString());
        db.execSQL(RespostaOptativaDAO.getDropTableString());

        db.execSQL(Questionario_QuestaoAlternativaDAO.getDropTableString());
        db.execSQL(Questionario_QuestaoOptativaDAO.getDropTableString());
        db.execSQL(QuestionarioQuestaoOpinativaDAO.getDropTableString());

        db.execSQL(AlternativaDAO.getDropTableString());
        db.execSQL(QuestaoAlternativaDAO.getDropTableString());
        db.execSQL(QuestaoOpinativaDAO.getDropTableString());
        db.execSQL(QuestaoOptativaDAO.getDropTableString());

        db.execSQL(RespostaOpinativaDAO.getDropTableString());db.execSQL(InteracaoQuestionarioDAO.getDropTableString());
        db.execSQL(DesafioQuestionarioDAO.getDropTableString());
        db.execSQL(QuestionarioDAO.getDropTableString());

        db.execSQL(PublicacaoDAO.getDropTableString());
        db.execSQL(InteracaoDesafioDAO.getDropTableString());
        db.execSQL(DesafioXMetaDAO.getDropTableString());
        db.execSQL(ItemDAO.getDropTableString());
        db.execSQL(ItemXMetaDAO.getDropTableString());
        db.execSQL(MetaDAO.getDropTableString());
        db.execSQL(DesafioDAO.getDropTableString());
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }
}
