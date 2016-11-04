package com.example.com.hls.play2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.com.hls.play2.online.Json_data;

public class SecondMain extends Activity implements OnClickListener {
	private ListView lv2;
	static int status = 0x11;
	private ImageButton butplay, butonplay, butdownplay;
	private MediaPlayer mp = new MediaPlayer();
	static List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				// 设置适配器
				showmusic();
			}
		}
	};

	class geturl extends Thread {
		@Override
		public void run() {
			super.run();
			Message msg = new Message();
			msg.what = 1;
			getjson();
			handler.sendMessage(msg);
		}
	}

	public String getjson() {
		try {
			URL url = new URL(
					"http://music.163.com/api/playlist/detail?id=29818120&updateTime=-1");
			// 打开URL
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection.setRequestProperty("contentType", "UTF-8");
			// 得到输入流，即获得了网页的内容
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream(), "UTF-8"));
			StringBuffer sb = new StringBuffer();
			// 读取输入流的数据，并显示
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			line = sb.toString();
			// Log.e("json", line);
			Json_data j = new Json_data();
			list = j.gs(line);
			int i = list.size();
			Log.e("i", i + "");
			return line;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.online);
		butplay = (ImageButton) findViewById(R.id.butplay);
		butdownplay = (ImageButton) findViewById(R.id.butdownplay);
		butonplay = (ImageButton) findViewById(R.id.butonplay);
		butplay.setOnClickListener(this);
		butdownplay.setOnClickListener(this);
		butonplay.setOnClickListener(this);
		lv2 = (ListView) findViewById(R.id.card_listView1);
		lv2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MainActivity.CTL_ACTION);
				intent.putExtra("control", 5);
				intent.putExtra("current", position);
				intent.putExtra("who", 2);
				sendBroadcast(intent);
			}
		});
		new geturl().start();
	}

	public void showmusic() {
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.onlinelist,
				new String[] { "music_name", "music_art" }, new int[] {
						R.id.line1, R.id.line2 });
		lv2.setAdapter(adapter);
	}

	public class ActivityReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 获取Intent中的update消息，update代表播放状态
			int update = intent.getIntExtra("update", -1);
			switch (update) {
			case 0x11:
				status = 0x11;
				break;
			case 0x12:
				status = 0x12;
				break;
			case 0x13:
				status = 0x13;
				break;

			}
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(MainActivity.CTL_ACTION);
		switch (v.getId()) {
		case R.id.butplay:
			intent.putExtra("control", 1);
			butplay.setImageResource(R.drawable.desk_play_prs);
			if (status == 18) {
				butplay.setImageResource(R.drawable.desk_pause);
			}
			break;
		case R.id.butonplay:
			intent.putExtra("control", 3);
			break;
		case R.id.butdownplay:
			intent.putExtra("control", 4);
			break;
		}
		sendBroadcast(intent);

	}
}
