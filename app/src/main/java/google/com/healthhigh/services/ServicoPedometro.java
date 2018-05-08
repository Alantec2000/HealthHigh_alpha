package google.com.healthhigh.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by Alan on 17/07/2017.
 */

public class ServicoPedometro extends Service {
    private final IBinder mBinder = new StepBinder();

    public class StepBinder extends Binder {
        ServicoPedometro getService() {
            return ServicoPedometro.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
