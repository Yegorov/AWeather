package com.yegorov.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class VKApi implements Sociable {
    private User user;
    private UserInfo userInfo;
    private Activity activity;

    public VKApi(User user, Activity activity) {
        this.user = user;
        this.userInfo = new UserInfo();
        this.activity = activity;
    }

    @Override
    public void getUserDataInfo() {
        // Create http client and build GET request
        AndroidHttpClient httpClient = AndroidHttpClient.newInstance(ProjectVars.USER_AGENT);
        HttpGet httpGet = new HttpGet(ProjectVars.VK_URL +
                ProjectVars.USER_GET +
                ProjectVars.FIELDS + "photo_200,domain&" +
                ProjectVars.ACCESS_TOKEN + "=" + user.getAccessToken() + "&" +
                ProjectVars.VK_VERSION);
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
            jsonObject = ((JSONObject) jsonObject.getJSONArray("response").get(0));
            userInfo.setName(jsonObject.getString("first_name") + " " + jsonObject.getString("last_name"));
            userInfo.setProfileUrl(ProjectVars.VK + "/" + jsonObject.getString("domain"));

            URL url = new URL(jsonObject.getString("photo_200"));
            userInfo.setPhotoUrl(url.toString());
            userInfo.setBitmap(BitmapFactory.decodeStream(url.openConnection().getInputStream()));
        }catch (Exception e) {

        }
        httpClient.close();

    }

    @Override
    public UserInfo getUserInfo() {
        return userInfo;
    }

    @Override
    public void postRecord(final String record) {
        // In new thread post message (about weather) in wall VK.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AndroidHttpClient httpClient = AndroidHttpClient.newInstance(ProjectVars.USER_AGENT);
                    HttpGet httpGet = new HttpGet(ProjectVars.VK_URL +
                            ProjectVars.WALL_POST +
                            ProjectVars.OWNER_ID + ProjectVars.VK_WALL_OWNER_ID + "&" +
                            ProjectVars.MESSAGE + URLEncoder.encode(record, "UTF-8") + "&" +
                            ProjectVars.ACCESS_TOKEN + "=" + user.getAccessToken() + "&" +
                            ProjectVars.VK_VERSION);
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

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, activity.getText(R.string.success_share), Toast.LENGTH_SHORT).show();
                        }
                    });
                }catch (Exception e) {
                    // TODO
                }
            }
        }).start();
    }

}
