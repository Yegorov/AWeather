package com.yegorov.app;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class UserActivity extends ActionBarActivity implements ClickListener, FragmentCreateViewListener {
    public final static String TAG = "UserActivity";
    private ProfileFragment profileFragment;
    private MainActivity.WeatherFragment weatherFragment;
    private ShareFragment shareFragment;

    private User user;
    private String socialStr;
    private volatile Sociable sociable;
    private WeatherString weatherString;
    private DayString dayString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        if (savedInstanceState == null) {
            profileFragment = new ProfileFragment();
            weatherFragment = new MainActivity.WeatherFragment();
            shareFragment = new ShareFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.container1, profileFragment)
                    .add(R.id.container2, weatherFragment)
                    .add(R.id.container3, shareFragment)
                    .commit();

            getFragmentManager().executePendingTransactions();
        }

        socialStr = getIntent().getStringExtra(SocialNet.TAG);
        user = getIntent().getParcelableExtra(User.USER_TAG);
        weatherString = getIntent().getParcelableExtra(WeatherString.TAG);
        dayString = getIntent().getParcelableExtra(DayString.TAG);
        sociable = getSociable(socialStr, user);

        getAsyncUserData();



        Log.d(TAG, socialStr);
        Log.d(TAG, user.getAccessToken() + " " + user.getUserId() + " " + user.getExpiresIn());
    }

    private Sociable getSociable(String socNet, User user) {
        if(socNet.equals(SocialNet.VK)) {
            return new VKApi(user, this);
        }
        else if(socNet.equals(SocialNet.FB)) {
            return new FBApi(user, this);
        }

        return null;
    }

    private void getAsyncUserData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sociable.getUserDataInfo();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        profileFragment = (ProfileFragment)getFragmentManager().findFragmentById(R.id.container1);
                        UserInfo u = sociable.getUserInfo();
                        profileFragment.getUserName().setText(u.getName());
                        profileFragment.getProfileUrl().setText(u.getProfileUrl());
                        profileFragment.getProfileImg().setImageBitmap(u.getBitmap());
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
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

    @Override
    public void OnClick(View v) {
        sociable.postRecord(weatherString.toString());
    }

    @Override
    public void onCreateView() {
        if(weatherFragment != null && weatherFragment.getAirTemp() != null) {
            weatherFragment.getAirTemp().setText(weatherString.getAirTemp());
            weatherFragment.getBarPressure().setText(weatherString.getBarPressure());
            weatherFragment.getHumidity().setText(weatherString.getHumidity());
            weatherFragment.getWind().setText(weatherString.getWindSpeed() + " " + weatherString.getWindDirection());
            weatherFragment.getDate().setText(dayString.getDate());
            weatherFragment.getSunrise().setText(dayString.getSunRise());
            weatherFragment.getSunset().setText(dayString.getSunSet());
            weatherFragment.getMoon().setText(dayString.getPhaseMoon());
        }
    }

    public static class ProfileFragment extends Fragment {
        private TextView userName;
        private TextView profileUrl;
        private ImageView profileImg;

        public TextView getUserName() {
            return userName;
        }

        public TextView getProfileUrl() {
            return profileUrl;
        }

        public ImageView getProfileImg() {
            return profileImg;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.profile_fragment, container, false);
            userName = (TextView) rootView.findViewById(R.id.user_name);
            profileUrl = (TextView) rootView.findViewById(R.id.profile_url);
            profileImg = (ImageView) rootView.findViewById(R.id.profile_photo);

            return rootView;
        }
    }

    public static class ShareFragment extends Fragment implements View.OnClickListener {
        private ImageButton shareBtn;
        private ClickListener shareClickListener;

        public ImageButton getShareBtn() {
            return shareBtn;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

            shareClickListener = (ClickListener)activity;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.share_fragment, container, false);
            shareBtn = (ImageButton) rootView.findViewById(R.id.sharebtn);
            shareBtn.setOnClickListener(this);
            return rootView;
        }

        @Override
        public void onClick(View v) {
            shareClickListener.OnClick(v);
        }
    }
}

