package riveu.notifications.android;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
@Override
public void onReceive(Context context, Intent intent) {
    // Explicitly specify that GcmIntentService will handle the intent.
    ComponentName comp = new ComponentName(context.getPackageName(),
            GcmIntentService.class.getName());
    // Start the service, keeping the device awake while it is launching.
    startWakefulService(context, (intent.setComponent(comp)));
    setResultCode(Activity.RESULT_OK);
}
}

