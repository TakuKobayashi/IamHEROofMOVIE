package com.movie.of.hero.am.i.iamheroofmovie;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import java.net.URISyntaxException;
import java.util.UUID;


public class AudioTestActivity extends Activity {

    AudioTrack mAudioTrack;
    Visualizer mVisualizer;

    //サンプルレート
    static int SAMPLE_RATE = 44100;
    int bufSize = SAMPLE_RATE * 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audiotest);

        mAudioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC, SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_DEFAULT,
                bufSize, AudioTrack.MODE_STREAM,
                420);//セッションID
        mAudioTrack.play();


        mVisualizer = new Visualizer(mAudioTrack.getAudioSessionId());

// 1024
        int captureSize = Visualizer.getCaptureSizeRange()[1];
        mVisualizer.setCaptureSize(captureSize);


        mVisualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    // Waveデータ
                    public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {

                    }

                    // フーリエ変換
                    public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {

                        //このbytesがFFT後のデータ
                        //何度も呼ばれるのでこのデータを分析する

                    }
                },
                Visualizer.getMaxCaptureRate(),
                false,//trueならonWaveFormDataCapture()
                true);//trueならonFftDataCapture()

        mVisualizer.setEnabled(true);

        byte[] audioData = new byte[bufSize];

        // A単音
        double freqA = 440;
        double t = 0.0;
        double dt = 1.0 / SAMPLE_RATE;
        for (int i = 0; i < audioData.length; i++, t += dt) {
           audioData[i] = (byte) (Byte.MAX_VALUE * (Math.sin(2.0 * Math.PI * t * freqA)));
        }
        mAudioTrack.write(audioData, 0, audioData.length);
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
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
