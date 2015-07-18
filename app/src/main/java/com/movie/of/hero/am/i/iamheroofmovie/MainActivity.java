package com.movie.of.hero.am.i.iamheroofmovie;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

import java.util.UUID;


public class MainActivity extends YouTubeBaseActivity {

    private MqttAndroidClient mqttAndroidClient;

    //Youtube のビデオID
    private static String videoId = "9bZkp7q19f0";
    private YouTubePlayerView youTubeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        //Youtubeビューの初期化
        youTubeView.initialize(Config.getDeveloperKey(), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {
                    player.loadVideo(videoId);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
                String errorMessage = String.format("ERR", errorReason.toString());
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        String clientId = UUID.randomUUID().toString();
        mqttAndroidClient = new MqttAndroidClient(this, "tcp://100.123.45.194:61623", "webconsole-88028"); // (1)
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("admin");
        options.setPassword("password".toCharArray());
        //connecしてpublisする処理だが,connecができていないのでコメントアウト
        /*
        try {
            mqttAndroidClient.connect(options, null, new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken mqttToken) {
                    Log.d("TakuTaku", "SUCCESS:" + mqttToken.getMessageId());
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
        Log.d("TakuTaku", "client:" + mqttAndroidClient.isConnected());
        try {
            mqttAndroidClient.publish("3dbcs.biz/UTokyo/iREF/6F/Hilobby/Light/LED/LoE/L42/brightness/W", "value=0&datetime=2015-07-18T17:14:00+09:00".getBytes(), 0, false); // (3)
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        */

        /*
        mAudioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC, SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_DEFAULT,
                bufSize, AudioTrack.MODE_STREAM,
                420);//セッションID
        mAudioTrack.play();


        mVisualizer = new Visualizer(420);

// 1024
        int captureSize = Visualizer.getCaptureSizeRange()[1];
        mVisualizer.setCaptureSize(captureSize);


        mVisualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    // Waveデータ
                    public void onWaveFormDataCapture(
                            Visualizer visualizer,
                            byte[] bytes, int samplingRate) {
                    }

                    // フーリエ変換
                    public void onFftDataCapture(
                            Visualizer visualizer,
                            byte[] bytes, int samplingRate) {

                        //このbytesがFFT後のデータ
                        //何度も呼ばれるのでこのデータを分析する

                    }, Visualizer.getMaxCaptureRate(),
                            false,//trueならonWaveFormDataCapture()
                            true);//trueならonFftDataCapture()

                    mVisualizer.setEnabled(true);

                    byte[] audioData = new byte[bufSize];

                    // A単音
                    double freqA = 440;
                    double t = 0.0;
                    double dt = 1.0 / SAMPLE_RATE;
                    for (int i = 0; i < audioData.length; i++, t += dt) {
                        audioData[i] = (byte) (Byte.MAX_VALUE * (Math.sin(2.0 *
                                Math.PI * t * freqA)));
                    }
                    mAudioTrack.write(audioData, 0, audioData.length);
                    */
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
    protected void onPause() {
        super.onPause();
    }
}
