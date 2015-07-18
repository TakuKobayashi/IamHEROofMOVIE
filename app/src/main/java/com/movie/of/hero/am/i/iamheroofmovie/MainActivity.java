package com.movie.of.hero.am.i.iamheroofmovie;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import java.util.UUID;


public class MainActivity extends Activity {

    //Youtube のビデオID
    private static String videoId = "9bZkp7q19f0";
    private MqttAndroidClient mqttAndroidClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
