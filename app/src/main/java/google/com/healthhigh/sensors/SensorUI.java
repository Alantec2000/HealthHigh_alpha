package google.com.healthhigh.sensors;

import android.widget.TextView;


/**
 * Created by Alan on 30/07/2017.
 */

public class SensorUI {
    private TextView tempo;
    private TextView nPassos;
    private TextView media;
    public SensorUI(){}

    public SensorUI(TextView tempo, TextView nPassos, TextView media) {
        this.tempo = tempo;
        this.nPassos = nPassos;
        this.media = media;
    }

    public void setTempo(TextView tempo) {
        this.tempo = tempo;
    }

    public void setnPassos(TextView nPassos) {
        this.nPassos = nPassos;
    }

    public void setMedia(TextView media) {
        this.media = media;
    }

    public TextView getTempo() {
        return tempo;
    }

    public TextView getnPassos() {
        return nPassos;
    }

    public TextView getMedia() {
        return media;
    }
}
