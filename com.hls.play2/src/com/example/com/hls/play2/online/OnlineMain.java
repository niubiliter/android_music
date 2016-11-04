package com.example.com.hls.play2.online;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.R;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class OnlineMain extends Activity {
		private ListView lv2;
		private MediaPlayer mp = new MediaPlayer();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == 1) {
					// …Ë÷√  ≈‰∆˜
					showmusic();
				}
			}
		};
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
		}
		public void showmusic() {
		}
}
