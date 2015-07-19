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

	public Subscriber(){
		try {
			init();
		}catch (URISyntaxException e){
			e.printStackTrace();
		}
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
				resetAll();
			}
		});
	}

	public void resetAll(){
		for(int i = 42;i <= 59;++i){
			publish(i, 0);
		}
	}

	public void publish(int num, int value){
		connection.publish("3dbcs.biz/UTokyo/iREF/6F/Hilobby/Light/LED/LoE/L" + num + "/brightness/W", ("value=" + value).getBytes(), QoS.AT_LEAST_ONCE, false, new Callback<Void>() {
			@Override
			public void onSuccess(Void aVoid) {
				Log.d("TakuTaku", "PUBLISH:" + host + ":" + port);
			}

			@Override
			public void onFailure(Throwable throwable) {

			}
		});
	}

	public void publishHue(final int num, int value){
		/*
		connection.publish("3dbcs.biz/UTokyo / iREF/6F/Hilobby/Light/LED/hue/"+ i +"/status/W", ("value=" + 0).getBytes(), QoS.AT_LEAST_ONCE, false, new Callback<Void>() {
			@Override
			public void onSuccess(Void aVoid) {
				Log.d("TakuTaku", "PUBLISH:" + host + ":" + port);
				connection.publish("3dbcs.biz/UTokyo/iREF/6F/Hilobby/Light/LED/hue/light" + num + "/hue/W", ("value=" + value).getBytes(), QoS.AT_LEAST_ONCE, false, new Callback<Void>() {
					@Override
					public void onSuccess(Void aVoid) {
						Log.d("TakuTaku", "PUBLISH:" + host + ":" + port);
					}

					@Override
					public void onFailure(Throwable throwable) {

					}
				});
			}

			@Override
			public void onFailure(Throwable throwable) {

			}
		});
		*/
	}

	public void resetHueAll(){
		for(int i = 3;i <= 4;++i){
			connection.publish("3dbcs.biz/UTokyo / iREF/6F/Hilobby/Light/LED/hue/" + i + "/status/W", ("value=" + 0).getBytes(), QoS.AT_LEAST_ONCE, false, new Callback<Void>() {
				@Override
				public void onSuccess(Void aVoid) {
					Log.d("TakuTaku", "PUBLISH:" + host + ":" + port);
				}

				@Override
				public void onFailure(Throwable throwable) {

				}
			});
		}
	}

	public void disconnect() {
		connection.disconnect(null);
	}

}
