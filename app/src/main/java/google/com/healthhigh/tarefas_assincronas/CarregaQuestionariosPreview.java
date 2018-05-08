package google.com.healthhigh.tarefas_assincronas;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import google.com.healthhigh.dao.QuestionarioDAO;
import google.com.healthhigh.domain.Questionario;
import google.com.healthhigh.utils.Toaster;

/**
 * Created by Alan on 22/10/2017.
 */

public class CarregaQuestionariosPreview extends AsyncTask<Void, Void, List<Questionario>> {
    private Context c;
    private RecyclerView rv;

    public CarregaQuestionariosPreview(Context c, RecyclerView rv){
        this.c = c;
        this.rv = rv;
    }

    @Override
    protected List<Questionario> doInBackground(Void... params) {
        QuestionarioDAO dao_questionario = new QuestionarioDAO(c);
        List<Questionario> questionarios = dao_questionario.getQuestionarios(3);
        return questionarios;
    }

    @Override
    protected void onPostExecute(List<Questionario> questionarios) {
        super.onPostExecute(questionarios);
        Toaster.toastShortMessage(c, questionarios.size());
//        rv.setAdapter(new DesafioAdapter(desafios,c));
//        rv.setLayoutManager(new LinearLayoutManager(c));
//        rv.setNestedScrollingEnabled(false);
//        Toaster.toastShortMessage(c, rv.getChildCount());
    }
}
