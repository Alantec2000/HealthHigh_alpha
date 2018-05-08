package google.com.healthhigh.Jobs;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.os.Build;

import java.util.List;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class VerificarRealizacaoDesafioJob extends JobScheduler {
    @Override
    public int schedule(JobInfo job) {
        return JobScheduler.RESULT_SUCCESS;
    }

    @Override
    public void cancel(int jobId) {

    }

    @Override
    public void cancelAll() {

    }

    @Override
    public List<JobInfo> getAllPendingJobs() {
        return null;
    }

    @Override
    public JobInfo getPendingJob(int jobId) {
        return null;
    }
}
