package com.movie.of.hero.am.i.iamheroofmovie;

import android.util.Log;

import java.net.URISyntaxException;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.cli.Listener;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

public class Subscriber {

	String host = "100.123.45.194";
	int port = 61613;
	private CallbackConnection connection;

	public Subscriber() throws URISyntaxException {
		init();
	}

	private void init() throws URISyntaxException {
		MQTT mqtt = new MQTT();
		mqtt.setHost(host, 61613);
		mqtt.setCleanSession(true);
		mqtt.setClientId("ElasticSearch");
		mqtt.setUserName("admin");
		mqtt.setPassword("password");

		connection = mqtt.callbackConnection();
	}

	public void connect() {

		connection.connect(new Callback<Void>() {

			@Override
			public void onFailure(Throwable arg0) {
				System.out.println("hoge");

			}

			@Override
			public void onSuccess(Void arg0) {
				Log.d("TakuTaku", "SUCCESS:" + host + ":" + port);
				connection.publish("3dbcs.biz/UTokyo/iREF/6F/Hilobby/Light/LED/LoE/L50/brightness/W", "value=10".getBytes(), QoS.AT_LEAST_ONCE, false, new Callback<Void>() {
					@Override
					public void onSuccess(Void aVoid) {
						Log.d("TakuTaku", "SUCCESS:" + host + ":" + port);
					}

					@Override
					public void onFailure(Throwable throwable) {

					}
				});
			}
		});
	}

	public void disconnect() {
		connection.disconnect(null);
	}

}
