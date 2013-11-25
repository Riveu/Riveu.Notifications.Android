package riveu.notifications.android;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.internal.fa;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    private  static final int settings_result = 1;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    public static void MessageBox(String title, String message, Context activity)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setMessage(message).setTitle(title);
        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogPopup, int id) {
                dialogPopup.dismiss();
            }
        });
        AlertDialog alert = dialog.create();
        alert.show();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void ReloadMainActivity(Activity activity)
    {
        activity.recreate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent();
                settingsIntent.setClass(MainActivity.this, SetPreferenceActivity.class);
                startActivityForResult(settingsIntent, 1);
                return true;
            case R.id.action_refresh:
                //getActionBar().setSelectedNavigationItem(0);
                //Intent intent = new Intent(this, MainActivity.class);
                //startActivity(intent);
                recreate();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case settings_result:
                SaveUserSettings();
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void SaveUserSettings()
    {

        recreate();
        //try to authenticate user
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.

        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position)
            {
                case 0:
                    return ViewNotificationFragment.newInstance(1);
                case 1:
                    return SendNotificationFragment.newInstance((1));
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }

    public static class SendNotificationFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static SendNotificationFragment newInstance(int sectionNumber) {
            SendNotificationFragment fragment = new SendNotificationFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public SendNotificationFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_send_notification, container, false);
            Button sendBtn = (Button)rootView.findViewById(R.id.send_button);
            final EditText textbox = (EditText)rootView.findViewById(R.id.message);
            sendBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    SendButtonClick(textbox.getText().toString());
                }
            });
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText("Test");
            return rootView;
        }

        public void SendButtonClick(String message) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String settingsUsername = sharedPreferences.getString("usernamePreference", "");
            String settingsPassword = sharedPreferences.getString("passwordPreference", "");
            if (AppStatus.getInstance(getActivity()).isOnline(getActivity()))
            {
            if (message.trim().length() > 0)
            {
                if (settingsUsername.trim().length() > 0 && settingsPassword.trim().length() > 0)
                {
                    ProgressDialog progress = new ProgressDialog(this.getActivity());
                    progress.setTitle("Loading");
                    progress.setMessage("Please wait while loading...");
                    progress.show();
                    message = Uri.encode(message);
                    Object[] params = new Object[4];
                    params[0] = "http://riveu.com/api.aspx?CMD=SEND_NOTIFICATION&Username=" + settingsUsername + "&Password=" + settingsPassword + "&Message=" + message;
                    params[1] = progress;
                    new SendNotificationTask(getView(),getActivity()).execute(params);
                }
                else
                {
                    MainActivity.MessageBox("Missing Credentials", "Please configure username and password in settings.", getActivity());
                }
            }
            else
            {
                MainActivity.MessageBox("Error", "Message cannot be blank", getActivity());
            }
            }
            else
            {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this.getActivity());
                dialog.setMessage("The Internet is required for this application. Please verify connection and reload the application").setTitle("Internet Required");
                dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogPopup, int id) {
                        dialogPopup.dismiss();
                        getActivity().moveTaskToBack(true);
                        getActivity().finish();
                    }
                });
                AlertDialog alert = dialog.create();
                alert.show();
            }
        }
    }

    public static class ViewNotificationFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ViewNotificationFragment newInstance(int sectionNumber) {
            ViewNotificationFragment fragment = new ViewNotificationFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public ViewNotificationFragment() {
        }

        private boolean CheckPlayServices()
        {
            try
            {
                int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
                if (resultCode != ConnectionResult.SUCCESS)
                {
                    if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
                    {
                        GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), 9000).show();
                    }
                    return false;
                }
            }
            catch (Exception ex)
            {
                return false;
            }
            return true;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            if (AppStatus.getInstance(getActivity()).isOnline(getActivity()))
            {
            ProgressDialog progress = new ProgressDialog(this.getActivity());
            progress.setTitle("Loading");
            progress.setMessage("Please wait while loading...");
            progress.show();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String settingsUsername = sharedPreferences.getString("usernamePreference", "");
            String settingsPassword = sharedPreferences.getString("passwordPreference", "");
            if (sharedPreferences.contains("pushNotificationPreference") == false)
            {
                MainActivity.MessageBox("Push Notifications", "This application supports push notifications. Please go to settings to enable it and configure your credentials.", getActivity());
            }
            else
            {
                boolean enablePush = sharedPreferences.getBoolean("pushNotificationPreference", false);
                String android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                if (enablePush)
                {
                   if (CheckPlayServices())
                   {
                       Object[] params = new Object[4];
                       params[0] = android_id;
                       params[1] = true;
                       params[2] = settingsUsername;
                       params[3] = settingsPassword;
                       new RegisterPushChannel(getActivity()).execute(params);
                   }
                }
                else
                {
                    Object[] params = new Object[4];
                    params[0] = android_id;
                    params[1] = false;
                    params[2] = settingsUsername;
                    params[3] = settingsPassword;
                    new RegisterPushChannel(getActivity()).execute(params);
                }
            }
            Object[] params = new Object[2];
            params[0] = "http://riveu.com/api.aspx?CMD=GET_NOTIFICATIONS_ANDROID&Username=" + settingsUsername + "&Password=" + settingsPassword;
            params[1] = progress;
            new GetDataTask(rootView, this.getActivity()).execute(params);
            }
            else
            {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this.getActivity());
                dialog.setMessage("The Internet is required for this application. Please verify connection and reload the application").setTitle("Internet Required");
                dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogPopup, int id) {
                        dialogPopup.dismiss();
                        getActivity().moveTaskToBack(true);
                        getActivity().finish();
                    }
                });
                AlertDialog alert = dialog.create();
                alert.show();
            }
            return rootView;
        }
    }

    static class GetDataTask extends AsyncTask<Object, ProgressDialog, String> {
        private  Exception exception;
        private  View view;
        private Context activity;
        private ProgressDialog progressDialog;
        public  GetDataTask(View rootView, Activity rootActivity)
        {
            view = rootView;
            activity = rootActivity;
        }

        @Override
        protected String doInBackground(Object... params)
        {
            try
            {
                String url = (String)params[0];
                progressDialog = (ProgressDialog)params[1];
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity entity = httpResponse.getEntity();
                InputStream inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line);
                }
                inputStream.close();
                String notificationData = sb.toString();
                return notificationData;
            }
            catch (Exception ex)
            {
                MainActivity.MessageBox("Error", "Unable to retrieve messages from the Riveu server. Please verify Internet connection and try again.", activity);
                return null;
            }
        }

        protected  void onPostExecute(String data)
        {
            try
            {
                String[] dataToBind = data.split("\\:\\:\\|\\|\\:\\:");
                ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(dataToBind));
                arrayList.remove(arrayList.size()-1);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, android.R.id.text1, arrayList);
                ListView listView = (ListView) view.findViewById(R.id.notifications_list_view);
                listView.setAdapter(adapter);
            }
            catch (Exception ex)
            {
                MainActivity.MessageBox("Error", "Unable to load notification data.", activity);
            }
            progressDialog.dismiss();
        }
    }

    static class SendNotificationTask extends AsyncTask<Object, ProgressDialog, String> {
        private  Exception exception;
        private  View view;
        private Activity activity;
        private ProgressDialog progressDialog;
        public  SendNotificationTask(View rootView, Activity rootActivity)
        {
            view = rootView;
            activity = rootActivity;
        }

        @Override
        protected String doInBackground(Object... params)
        {
            try
            {
                String url = (String)params[0];
                progressDialog = (ProgressDialog)params[1];
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity entity = httpResponse.getEntity();
                InputStream inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line);
                }
                inputStream.close();
                String notificationData = sb.toString();
                return notificationData;
            }
            catch (Exception ex)
            {
                MainActivity.MessageBox("Error", "Unable to retrieve messages from the Riveu server. Please verify Internet connection and try again.", activity);
                return "";
            }
        }

        protected  void onPostExecute(String data)
        {
            try
            {
                if (data.contains("Authentication Failure::||::"))
                {
                    MainActivity.MessageBox("Error", "Authentication Failure. Please verify credentials under settings", activity);
                }
                else
                {
                    //Send Message
                    progressDialog.dismiss();
                    MainActivity.MessageBox("Sent", "Message Sent", activity);
                    EditText editText = (EditText)view.findViewById(R.id.message);
                    editText.setText("");
                    MainActivity.ReloadMainActivity(activity);
                }
                //String[] dataToBind = data.split("\\:\\:\\|\\|\\:\\:");
                //ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(dataToBind));
                //arrayList.remove(arrayList.size()-1);
                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, android.R.id.text1, arrayList);
                //ListView listView = (ListView) view.findViewById(R.id.notifications_list_view);
                //listView.setAdapter(adapter);
            }
            catch (Exception ex)
            {
                MainActivity.MessageBox("Error", "Unable to send message.", activity);
            }
            progressDialog.dismiss();
        }
    }

    static class SubscriberTask extends AsyncTask<Object, ProgressDialog, String> {

        @Override
        protected String doInBackground(Object... params)
        {
            try
            {
                String url = (String)params[0];
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity entity = httpResponse.getEntity();
                InputStream inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line);
                }
                inputStream.close();
                return sb.toString();
            }
            catch (Exception ex)
            {
                return "";
            }
        }

        protected  void onPostExecute(String data)
        {

        }
    }

    static class RegisterPushChannel extends AsyncTask<Object, ProgressDialog, String> {

        private Activity activity;
        public  RegisterPushChannel(Activity rootActivity)
        {
            activity = rootActivity;
        }

        @Override
        protected String doInBackground(Object... params)
        {
            try
            {
                boolean register = (Boolean)params[1];
                GoogleCloudMessaging googleCloudMessaging = GoogleCloudMessaging.getInstance(activity);
                if (register)
                {
                    String uniqueId = params[0].toString();
                    String registeredId = googleCloudMessaging.register("233811183446");
                    Object[] param = new Object[1];
                    String settingsUsername = (String)params[2];
                    String settingsPassword = (String)params[3];
                    param[0] = "http://riveu.com/api.aspx?CMD=ADD_SUBSCRIBER&DEVICE_ID=" + uniqueId + "&DEVICE_TYPE=Android&URI=" + registeredId + "&Username=" + settingsUsername + "&Password=" + settingsPassword;
                    new SubscriberTask().execute(param);
                    return uniqueId;
                }
                else
                {
                    googleCloudMessaging.unregister();
                    String uniqueId = params[0].toString();
                    Object[] param = new Object[1];
                    String settingsUsername = (String)params[2];
                    String settingsPassword = (String)params[3];
                    param[0] = "http://riveu.com/api.aspx?CMD=REMOVE_SUBSCRIBER&DEVICE_ID=" + uniqueId + "&DEVICE_TYPE=Android&Username=" + settingsUsername + "&Password=" + settingsPassword;
                    new SubscriberTask().execute(param);
                    return uniqueId;
                }
            }
            catch (Exception ex)
            {
                Log.e("Error",ex.getMessage());
                return null;
            }
        }

        protected  void onPostExecute(String data)
        {

        }
    }
}

