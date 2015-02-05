package com.yegorov.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.yegorov.parse.ForecastProvider;
import com.yegorov.parse.ParseFactory;
import com.yegorov.parse.WeatherParsable;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class Weather extends Activity {

    private LinearLayout mainLayout;
    private Button vkShareBtn;
    private ImageView userImage;
    private TextView userNameTextView;
    private TextView vkdomainTextView;
    private TextView univTextView;
    private TextView weatherTextView;
    private ProgressDialog progressDialog;

    private String access_token;
    private String user_id;

    private com.yegorov.modelweather.Weather weather = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);

        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        vkShareBtn = (Button) findViewById(R.id.shareButton);
        userImage = (ImageView) findViewById(R.id.userImage);
        userNameTextView = (TextView) findViewById(R.id.userNameTextView);
        vkdomainTextView = (TextView) findViewById(R.id.domainTextView);
        univTextView = (TextView) findViewById(R.id.universityTextView);
        weatherTextView = (TextView) findViewById(R.id.weatherTextView);

        Intent intent = getIntent();
        access_token = intent.getStringExtra(ConstKey.ACCESS_TOKEN);
        user_id = intent.getStringExtra(ConstKey.USER_ID);

        onLoad();
    }

    // OnClick buttons listener
    public void onClick(View v) {
        if(v.getId() == vkShareBtn.getId()) {
            shareWeather();
        }
    }

    private void shareWeather() {
        if(weather == null)
            return;
        vkShareBtn.setVisibility(View.INVISIBLE);
        // In new thread post message (about weather) in wall VK.
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    AndroidHttpClient httpClient = AndroidHttpClient.newInstance(ConstKey.USER_AGENT);
                    HttpGet httpGet = new HttpGet(ConstKey.VK_URL +
                        ConstKey.WALL_POST +
                        ConstKey.OWNER_ID + ConstKey.VK_WALL_OWNER_ID + "&" +
                        ConstKey.MESSAGE + URLEncoder.encode(getText(R.string.current_weather) + "\n" + weather.toString(), "UTF-8") + "&" +
                        ConstKey.ACCESS_TOKEN + "=" + access_token + "&" +
                        ConstKey.VK_VERSION);

                    HttpResponse httpResponse = httpClient.execute(httpGet);

                    BufferedReader rd = new BufferedReader(
                            new InputStreamReader(httpResponse.getEntity().getContent()));

                    StringBuffer result = new StringBuffer();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        result.append(line);
                    }
                    rd.close();

                    JSONObject jsonObject = (JSONObject) new JSONTokener(result.toString()).nextValue();

                    Log.d("JSON", jsonObject.toString(4));

                    httpClient.close();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), getText(R.string.success_share), Toast.LENGTH_SHORT).show();
                            //vkShareBtn.setVisibility(View.VISIBLE);
                        }
                    });
                }catch (Exception e) {
                    // TODO
                }
            }
        }).start();
    }

    private void onLoad() {
        // Hide user's elements and show progress dialog
        mainLayout.setVisibility(View.INVISIBLE);
        progressDialog = ProgressDialog.show(this, getText(R.string.please_wait),
                                                   getText(R.string.getting_weather_info),
                                                   true, false);
        // In new thread, downloading information about user in VK and weather forecast in Inmart provider
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Create http client and build GET request
                AndroidHttpClient httpClient = AndroidHttpClient.newInstance(ConstKey.USER_AGENT);

                HttpGet httpGet = new HttpGet(ConstKey.VK_URL +
                                            ConstKey.USER_GET +
                                            ConstKey.FIELDS + "photo_200,domain,universities&" +
                                            ConstKey.ACCESS_TOKEN + "=" + access_token + "&" +
                                            ConstKey.VK_VERSION);
                HttpResponse httpResponse;

                try {
                    // Get info about user
                    httpResponse = httpClient.execute(httpGet);

                    BufferedReader rd = new BufferedReader(
                            new InputStreamReader(httpResponse.getEntity().getContent()));

                    StringBuffer result = new StringBuffer();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        result.append(line);
                    }

                    rd.close();

                    JSONObject jsonObject = (JSONObject) new JSONTokener(result.toString()).nextValue();

                    Log.d("JSON", jsonObject.toString(4));

                    jsonObject = ((JSONObject)jsonObject.getJSONArray("response").get(0));
                    final String userName = jsonObject.getString("first_name") + " " + jsonObject.getString("last_name");
                    final String domain = getText(R.string.vk_domain) + jsonObject.getString("domain");

                    String univ1 = "";
                    if(jsonObject.getJSONArray("universities").length() != 0)
                        univ1 = ((JSONObject)jsonObject.getJSONArray("universities").get(0)).getString("name");
                    final String univ = univ1;

                    URL url = new URL(jsonObject.getString("photo_200"));
                    final Bitmap userImageBmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());


                    // Get weather forecast
                    httpGet = new HttpGet(ConstKey.INMART_URL);

                    httpResponse = httpClient.execute(httpGet);

                    rd = new BufferedReader(
                            new InputStreamReader(httpResponse.getEntity().getContent()));

                    result = new StringBuffer();

                    while ((line = rd.readLine()) != null) {
                        result.append(line);
                    }
                    rd.close();

                    WeatherParsable weatherParsable = ParseFactory.getParseFactory()
                                                                  .getParserInstance(ForecastProvider.InmartWeather);
                    weather = weatherParsable.stringParse(result.toString());

                    // Update UI from another thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            userNameTextView.setText(userName);
                            vkdomainTextView.setText(domain);
                            univTextView.setText(univ);
                            userImage.setImageBitmap(userImageBmp);
                            weatherTextView.setText(weather.toString());
                            mainLayout.setVisibility(View.VISIBLE);
                            progressDialog.dismiss();
                        }
                    });

                    httpClient.close();
                } catch (Exception e) {
                    // TODO
                }
            }
        }).start();
    }
}
