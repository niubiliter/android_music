package com.example.com.hls.play2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore.Audio.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity implements OnClickListener {
	static List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private ListView lv;
	private Button online_btn;// 在线音乐
	private ImageButton butplay, butonplay, butdownplay;
	public static final String CTL_ACTION = "huangliusong.CTL_ACTION";
	public static final String UPDATE_ACTION = "huangliusong.UPDATE_ACTION";
	int status = 0x11;
	ActivityReceiver activityReceiver;
	Intent intentservice;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lv = (ListView) findViewById(R.id.card_listView);
		butplay = (ImageButton) findViewById(R.id.butplay);
		butdownplay = (ImageButton) findViewById(R.id.butdownplay);
		butonplay = (ImageButton) findViewById(R.id.butonplay);
		butplay.setOnClickListener(this);
		butdownplay.setOnClickListener(this);
		butonplay.setOnClickListener(this);

		online_btn = (Button) findViewById(R.id.online_btn);
		online_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, SecondMain.class);
				startActivity(intent);
			}

		});
		musicList();
		activityReceiver = new ActivityReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(UPDATE_ACTION);
		registerReceiver(activityReceiver, filter);
		intentservice = new Intent(this, MusicService.class);
		startService(intentservice);
	}

	public void musicList() {
		// 取得指定位置的文件并显示到播放列表
		String[] music = new String[] { Media._ID, Media.DISPLAY_NAME,
				Media.TITLE, Media.DURATION, Media.ARTIST, Media.DATA };
		Cursor cursor = getContentResolver().query(Media.EXTERNAL_CONTENT_URI,
				music, null, null, null);
		while (cursor.moveToNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("music_name", cursor.getString(1));
			map.put("music_url", cursor.getString(5));
			map.put("music_art", cursor.getString(4));
			list.add(map);// 添加map歌曲和歌手

		}
		// 设置适配器
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.list_item_card,
				new String[] { "music_name", "music_art", "music_url" }, new int[] {
						R.id.line1, R.id.line2, R.id.line3 });
		lv.setAdapter(adapter);
		// 为Listview项目添加监听
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CTL_ACTION);
				intent.putExtra("control", 5);
				intent.putExtra("current", position);
				intent.putExtra("who", 1);
				sendBroadcast(intent);
				
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(CTL_ACTION);
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
				butplay.setBackgroundResource(R.drawable.btn_check_to_off_mtrl_007);
				break;
			case 0x13:
				status = 0x13;
				break;

			}
		}

	}

}
