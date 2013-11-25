package riveu.notifications.android;

import android.app.Application;

/**
 * Created by Michael Dlesk on 11/24/13.
 */
public class RiveuApplication extends Application
{
    private Thread.UncaughtExceptionHandler androidDefaultUEH;

    private Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler()
    {
        public  void uncaughtException(Thread thread, Throwable ex)
        {
            MainActivity.MessageBox("Error", "An error occured. Please verify the internet connection", getApplicationContext());

        }
    };

    @Override
    public void onCreate()
    {
        super.onCreate();
        androidDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(handler);
    }
}

