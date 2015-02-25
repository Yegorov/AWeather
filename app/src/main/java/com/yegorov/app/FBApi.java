package com.yegorov.app;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class FBApi implements Sociable {

    private User user;
    private UserInfo userInfo;
    private Activity activity;

    public FBApi(User user, Activity activity) {
        this.user = user;
        this.userInfo = new UserInfo();
        this.activity = activity;
    }

    @Override
    public void getUserDataInfo() {
        AndroidHttpClient httpClient = AndroidHttpClient.newInstance(ProjectVars.USER_AGENT);
        HttpGet httpGet = new HttpGet(ProjectVars.FB_URL +
                ProjectVars.FB_VER + "/me/?" +
                ProjectVars.FIELDS + "id,name&" +
                ProjectVars.ACCESS_TOKEN + "=" + user.getAccessToken());
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
            userInfo.setName(jsonObject.getString("name"));
            userInfo.setProfileUrl(ProjectVars.FB + "/" + jsonObject.getString("id"));


            httpGet = new HttpGet(ProjectVars.FB_URL +
                    ProjectVars.FB_VER + "/me/" +
                    ProjectVars.PICTURE + "type=large&redirect=false&" +
                    ProjectVars.ACCESS_TOKEN + "=" + user.getAccessToken());
            Log.d("JSON", httpGet.getURI().toString());
            httpResponse = httpClient.execute(httpGet);
            rd = new BufferedReader(
                    new InputStreamReader(httpResponse.getEntity().getContent()));
            result = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            jsonObject = (JSONObject) new JSONTokener(result.toString()).nextValue();
            Log.d("JSON", jsonObject.toString(4));
            URL url = new URL(jsonObject.getJSONObject("data").getString("url"));
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                AndroidHttpClient httpClient = AndroidHttpClient.newInstance(ProjectVars.USER_AGENT);
                HttpPost httpPost = new HttpPost(ProjectVars.FB_URL +
                        ProjectVars.FB_VER + "/me/" + ProjectVars.FEED);
                HttpResponse httpResponse;
                try {
                    httpPost.setEntity(new StringEntity(ProjectVars.MESSAGE + URLEncoder.encode(record, "UTF-8")
                            + "&" + ProjectVars.ACCESS_TOKEN + "=" + user.getAccessToken()));

                    httpResponse = httpClient.execute(httpPost);
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

                    if(jsonObject.has("id"))
                        activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                Toast.makeText(activity, activity.getText(R.string.success_share), Toast.LENGTH_SHORT).show();
                            }
                        });

                } catch (Exception e) {
                    e.printStackTrace();
                }
                httpClient.close();
            }
        }).start();
    }
}
