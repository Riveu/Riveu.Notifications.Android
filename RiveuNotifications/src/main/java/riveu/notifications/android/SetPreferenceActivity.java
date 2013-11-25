package riveu.notifications.android;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Michael Dlesk on 11/24/13.
 */
public class SetPreferenceActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragment()).commit();
    }
}
