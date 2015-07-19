package com.movie.of.hero.am.i.iamheroofmovie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


public class MainActivity extends YouTubeBaseActivity implements SensorEventListener{

    private MqttAndroidClient mqttAndroidClient;

    //Youtube のビデオID
    private static String videoId = "BBRqGcDEjiM";
    private YouTubePlayer youTubePlayer;
    private SensorManager mSensorManager;
    private Timer mTimer;

    private RequestQueue mQueue;

    AudioTrack mAudioTrack;
    Visualizer mVisualizer;

    //サンプルレート
    static int SAMPLE_RATE = 44100;
    int bufSize = SAMPLE_RATE * 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mTimer = new Timer();
        mQueue = Volley.newRequestQueue(this);
        HashMap<String, String> params = new HashMap<String, String>();
        httpRequest(Request.Method.GET, "http://100.123.45.187:31413/jsonp/v1/devices/37?procedure=set&params=%7B\"propertyName\"%3A\"GautomaticBathWaterHeatingHModeSetting\"%2C\"propertyValue\"%3A%5B0x41%5D%7D", params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TakuTaku", response);
            }
        });

        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        //youTubeView.setVisibility(View.INVISIBLE);
        //Youtubeビューの初期化
        youTubeView.initialize(Config.getDeveloperKey(), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {
                    youTubePlayer = player;
                    youTubePlayer.setFullscreen(true);
                    youTubePlayer.loadVideo(videoId);
                    youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                        @Override
                        public void onLoading() {

                        }

                        @Override
                        public void onLoaded(String s) {
                            Log.d("TakuTaku", "start:" + System.currentTimeMillis());
                            TimerTask task = new TimerTask() {
                                @Override
                                public void run() {
                                    Log.d("TakuTaku", "timer:" + System.currentTimeMillis());
                                    HashMap<String, String> params = new HashMap<String, String>();
                                    httpRequest(Request.Method.GET, "http://100.123.45.187:31413/jsonp/v1/devices/37?procedure=set&params=%7B%22propertyName%22%3A%22GautomaticBathWaterHeatingHModeSetting%22%2C%22propertyValue%22%3A%5B0x42%5D%7D", params, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.d("TakuTaku", response);
                                        }
                                    });
                                }
                            };
                            mTimer.schedule(task, youTubePlayer.getDurationMillis() - youTubePlayer.getCurrentTimeMillis());
                        }

                        @Override
                        public void onAdStarted() {

                        }

                        @Override
                        public void onVideoStarted() {

                        }

                        @Override
                        public void onVideoEnded() {

                        }

                        @Override
                        public void onError(YouTubePlayer.ErrorReason errorReason) {

                        }
                    });
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
                String errorMessage = String.format("ERR", errorReason.toString());
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        String clientId = UUID.randomUUID().toString();
        mqttAndroidClient = new MqttAndroidClient(this, "tcp://100.123.45.194:61613", "webconsole-88028"); // (1)
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("admin");
        options.setPassword("password".toCharArray());
        options.setCleanSession(false);

        //connecしてpublisする処理だが,connecができていないのでコメントアウト
/*
        try {
            mqttAndroidClient.connect(options, this, new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken mqttToken) {
                    Log.d("TakuTaku", "SUCCESS:" + mqttToken.getMessageId());
                    try {
                        mqttAndroidClient.publish("3dbcs.biz/UTokyo/iREF/6F/Hilobby/Light/LED/LoE/L50/brightness/W", "value=0".getBytes(), 0, false); // (3)
                    } catch (MqttPersistenceException e) {
                        e.printStackTrace();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken arg0, Throwable arg1) {
                    Log.d("TakuTaku", "FAIL:" + arg1.getMessage());
                }
            });
        } catch (MqttException e) {
            Log.d("TakuTaku", "error:" + e.getMessage());
            e.printStackTrace();
        }
        //
        */
        try {
            Subscriber subscriber = new Subscriber();
            subscriber.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        //startActivity(new Intent(this, AudioTestActivity.class));
    }

    private void httpRequest(int method, String url , final Map<String, String> params, Response.Listener response){
        StringRequest request = new StringRequest(method ,url, response, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("TakuTaku", "error:" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                return params;
            }
        };
        mQueue.add(request);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(youTubePlayer != null && !youTubePlayer.isPlaying()){
            youTubePlayer.play();
        }
        //照度センサー
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(youTubePlayer != null) {
            youTubePlayer.pause();
        }
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        youTubePlayer.release();
        if(mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_LIGHT){
            float lux = event.values[0];
            TextView t = (TextView) findViewById(R.id.debugText);
            t.setText("lux:" + lux);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
