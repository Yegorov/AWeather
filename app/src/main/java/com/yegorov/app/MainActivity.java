package com.yegorov.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yegorov.modelweather.Moon;
import com.yegorov.modelweather.Sun;
import com.yegorov.modelweather.Weather;
import com.yegorov.parse.ForecastProvider;
import com.yegorov.parse.ParseFactory;
import com.yegorov.parse.WeatherParsable;
import com.yegorov.util.NoValidDateException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends ActionBarActivity implements AuthListener, PageListener, FragmentCreateViewListener {

    private WeatherFragment weatherFragment;
    private AuthFragment authFragment;
    private WebViewFragment webViewFragment;

    private String socialStr;
    private boolean isFirst = true;

    private volatile WeatherString weatherStr;
    private volatile DayString dayString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getFragmentManager();



        if (savedInstanceState == null) {
            Log.d("MY", "savedInstanceState NULL");
            if(weatherFragment == null) {
                weatherFragment = new WeatherFragment();
                fm.beginTransaction()
                  .add(R.id.container, weatherFragment)
                  .commit();
                Log.d("MY", "f commit");
            }

            if(authFragment == null) {
                authFragment = new AuthFragment();
                fm.beginTransaction()
                  .add(R.id.container2, authFragment)
                  .commit();
                Log.d("MY", "f2 commit");
            }

            weatherStr = new WeatherString();
            dayString = new DayString();

            getAsyncWeather(new CallbackTask() {
                private ProgressDialog dialog;

                @Override
                public void onPreExecute() {
                    dialog = ProgressDialog.show(MainActivity.this, getString(R.string.please_wait), getString(R.string.update_weather), true, false);
                }

                @Override
                public void onPostExecute() {
                    onCreateView();
                    dialog.dismiss();
                }
            });
        }



    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        getFragmentManager().popBackStack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {

            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(getString(R.string.about));
            alertDialog.setIcon(R.drawable.ic_launcher);
            alertDialog.setView(View.inflate(this, R.layout.about_dialog, null));
            alertDialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void eventHandler(View v, Fragment fragment) {

    }

    @Override
    public void signIn(String socialNet) {
        if (webViewFragment == null) {
            webViewFragment = new WebViewFragment();
        }

        if (socialNet == SocialNet.VK) {
            socialStr = SocialNet.VK;
            Log.d("SIGN", "is Vk");
        } else if (socialNet == SocialNet.FB) {
            socialStr = SocialNet.FB;
            Log.d("SIGN", "is Fb");
        }

        if(isFirst) {
            getFragmentManager().beginTransaction()
                    //.remove(weatherFragment)
                    //.add(R.id.container, webViewFragment)
                    .replace(R.id.container, webViewFragment)
                    .commit();
            isFirst = false;
        }
        else {
            getFragmentManager().beginTransaction()
                    //.remove(weatherFragment)
                    //.add(R.id.container, webViewFragment)
                    .replace(R.id.container, webViewFragment)
                    .commit();
            pageStart();
        }

    }

    @Override
    public void pageFinished(String url) {

        User user = null;
        if(url.contains(ProjectVars.ACCESS_TOKEN) && url.contains(ProjectVars.VK_BLANK_URL)) {

            Uri uri = Uri.parse(url);

            String access = url.split("access_token=")[1];
            uri = Uri.parse("?" + uri.getFragment());
            user = new User(uri.getQueryParameter(ProjectVars.USER_ID),
                            uri.getQueryParameter(ProjectVars.ACCESS_TOKEN),
                            uri.getQueryParameter(ProjectVars.EXPIRES_IN));
            //Toast.makeText(this, "auth VK", Toast.LENGTH_SHORT).show();
        }
        else if(url.contains(ProjectVars.ACCESS_TOKEN) && url.contains(ProjectVars.FB_BLANK_URL)) {

            Log.d("MainActivity", url);
            Uri uri = Uri.parse(url);

            String access = url.split("access_token=")[1];
            uri = Uri.parse("?" + uri.getFragment());
            user = new User("me",
                    uri.getQueryParameter(ProjectVars.ACCESS_TOKEN),
                    uri.getQueryParameter(ProjectVars.EXPIRES_IN));
            //Toast.makeText(this, "auth FB", Toast.LENGTH_SHORT).show();
        }

        if(user == null)
            return;

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, weatherFragment)
                .commit();


        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra(User.USER_TAG, user);
        intent.putExtra(SocialNet.TAG, socialStr);
        intent.putExtra(WeatherString.TAG, weatherStr);
        intent.putExtra(DayString.TAG, dayString);
        startActivity(intent);
    }

    @Override
    public void pageStart() {
        if(socialStr == SocialNet.VK) {
            webViewFragment.getWebView().loadUrl("https://oauth.vk.com/authorize?" +
                    "client_id=" + ProjectVars.VK_APP_ID +
                    "&scope=wall" +
                    "&redirect_uri=" + ProjectVars.HTTPS + ProjectVars.VK_BLANK_URL +
                    "&display=mobile" +
                    "&v=5.28" +
                    "&response_type=token" +
                    "&revoke=0");
        }
        else if(socialStr == SocialNet.FB) {
            webViewFragment.getWebView().loadUrl("https://www.facebook.com/dialog/oauth?" +
                    "client_id=" + ProjectVars.FB_APP_ID +
                    "&display=popup" +
                    "&scope=" + "user_about_me,publish_actions" +
                    "&response_type=token" +
                    "&redirect_uri=" + ProjectVars.HTTPS + ProjectVars.FB_BLANK_URL);
        }
    }

    public void getAsyncWeather(final CallbackTask task) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        task.onPreExecute();
                    }
                });
                AndroidHttpClient httpClient = AndroidHttpClient.newInstance(ProjectVars.USER_AGENT);
                HttpGet httpGet;
                HttpResponse httpResponse;
                Weather weather;
                StringBuffer result;
                String line;
                try {
                    // Get weather forecast
                    httpGet = new HttpGet(ProjectVars.INMART_URL);
                    httpResponse = httpClient.execute(httpGet);
                    BufferedReader rd = new BufferedReader(
                            new InputStreamReader(httpResponse.getEntity().getContent()));
                    result = new StringBuffer();
                    while ((line = rd.readLine()) != null) {
                        result.append(line);
                    }
                    rd.close();
                    WeatherParsable weatherParsable = ParseFactory.getParseFactory()
                            .getParserInstance(ForecastProvider.InmartWeather);
                    weather = weatherParsable.stringParse(result.toString());


                    weatherStr.setAirTemp(weather.airTempToStr());
                    weatherStr.setBarPressure(weather.barPressToStr());
                    weatherStr.setHumidity(weather.humidityToStr());
                    weatherStr.setWindDirection(weather.windDirectionToStr());
                    weatherStr.setWindSpeed(weather.windSpeedToStr());

                    // Update UI from another thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            task.onPostExecute();
                        }
                    });
                    httpClient.close();

                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    dayString.setDate("" + day + "/" + month + "/" + year);

                    Sun s = new Sun();
                    Moon m = new Moon();
                    try {
                        // day, month, year, latitude, longitude, local time zone
                        s.init(day, month, year, 48.333611, 38.0925, +2);
                        m.init(day, month, year);
                        dayString.setSunRise(s.toStrSunRiseHHMM());
                        dayString.setSunSet(s.toStrSunSetHHMM());
                        dayString.setPhaseMoon(m.getMoonPhase().toStr());
                    } catch (NoValidDateException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    // TODO
                }
            }
        }).start();
    }

    @Override
    public void onCreateView() {
        if(weatherFragment != null && weatherFragment.getAirTemp() != null) {
            weatherFragment.getAirTemp().setText(weatherStr.getAirTemp());
            weatherFragment.getBarPressure().setText(weatherStr.getBarPressure());
            weatherFragment.getHumidity().setText(weatherStr.getHumidity());
            weatherFragment.getWind().setText(weatherStr.getWindSpeed() + " " + weatherStr.getWindDirection());
            weatherFragment.getDate().setText(dayString.getDate());
            weatherFragment.getSunrise().setText(dayString.getSunRise());
            weatherFragment.getSunset().setText(dayString.getSunSet());
            weatherFragment.getMoon().setText(dayString.getPhaseMoon());
        }
    }


    public static class WeatherFragment extends Fragment {

        private TextView airTemp;
        private TextView barPressure;
        private TextView humidity;
        private TextView wind;
        private TextView date;
        private TextView sunrise;
        private TextView sunset;
        private TextView moon;

        private FragmentCreateViewListener fragmentCreateViewListener;

        public TextView getAirTemp() {
            return airTemp;
        }

        public TextView getBarPressure() {
            return barPressure;
        }

        public TextView getHumidity() {
            return humidity;
        }

        public TextView getWind() {
            return wind;
        }

        public TextView getDate() {
            return date;
        }

        public TextView getSunrise() {
            return sunrise;
        }

        public TextView getSunset() {
            return sunset;
        }

        public TextView getMoon() {
            return moon;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            fragmentCreateViewListener = (FragmentCreateViewListener) activity;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.weather_fragment, container, false);

            airTemp  = (TextView)rootView.findViewById(R.id.air_temp);
            barPressure = (TextView)rootView.findViewById(R.id.bar_pressure);
            humidity = (TextView)rootView.findViewById(R.id.humidity);
            wind     = (TextView)rootView.findViewById(R.id.wind);
            date     = (TextView)rootView.findViewById(R.id.date);
            sunrise  = (TextView)rootView.findViewById(R.id.sunrise);
            sunset   = (TextView)rootView.findViewById(R.id.sunset);
            moon     = (TextView)rootView.findViewById(R.id.moon);
            fragmentCreateViewListener.onCreateView();
            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }
    }

    public static class AuthFragment extends Fragment implements View.OnClickListener{

        private ImageButton vkBtn;
        private ImageButton fbBtn;

        private AuthListener authListener;


        public ImageButton getVkBtn() {
            return vkBtn;
        }

        public ImageButton getFbBtn() {
            return fbBtn;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

            authListener = (AuthListener) activity;
            if(authListener == null)
                throw new RuntimeException("Activity must implement AuthListener interface");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.authorization_fragment, container, false);

            vkBtn = (ImageButton) rootView.findViewById(R.id.vkbtn);
            fbBtn = (ImageButton) rootView.findViewById(R.id.fbbtn);
            vkBtn.setOnClickListener(this);
            fbBtn.setOnClickListener(this);

            return rootView;
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.vkbtn) {
                authListener.signIn(SocialNet.VK);
            }
            else if(v.getId() == R.id.fbbtn) {
                authListener.signIn(SocialNet.FB);
            }
        }
    }

    public static class WebViewFragment extends Fragment {

        private WebView webView;

        private PageListener pageListener;

        public WebView getWebView() {
            return webView;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.webview_fragment, container, false);
            webView = (WebView) rootView.findViewById(R.id.webView);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                        pageListener.pageFinished(url);
                }
            });
            pageListener.pageStart();
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            pageListener = (PageListener)activity;
            if(pageListener == null) {
                throw new RuntimeException("Activity must implement PageListener interface");
            }
        }
    }
}

