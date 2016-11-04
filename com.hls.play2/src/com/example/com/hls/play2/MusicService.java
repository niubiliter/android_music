package com.example.com.hls.play2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service {
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	MyReceiver serviceReceiver;
	MediaPlayer mPlayer;
	int status = 0x11;
	// �����±�
	int current = 0;
	int count = 0;
	int flog = 0;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		flog = 1;
		musicList(1);
		count = list.size();
		Log.e("service", "onStart");
		serviceReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MainActivity.CTL_ACTION);
		registerReceiver(serviceReceiver, filter);
		mPlayer = new MediaPlayer();
		mPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				current++;
				if (current > count - 1) {
					count = 0;
				}
				playMusic(list.get(current).get("music_url").toString());
			}
		});
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		if (flog == 2) {
			Intent sendIntent = new Intent(MainActivity.UPDATE_ACTION);
			sendIntent.putExtra("update", status);
			// sendIntent.putExtra("current", current);
			sendBroadcast(sendIntent);
		}
		flog = 2;
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		flog = 0;
		mPlayer.stop();
		mPlayer.release();
		super.onDestroy();
	}

	private void playMusic(String path) {
		try {
			/* ����MediaPlayer */
			mPlayer.reset();
			/* ����Ҫ���ŵ��ļ���·�� */
			mPlayer.setDataSource(path);
			/* ׼������ */
			mPlayer.prepare();
			/* ��ʼ���� */
			mPlayer.start();
			mPlayer.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer arg0) {
					// �������һ��֮�������һ��
					current--;
					if (current < 0) {
						current = count - 1;
					}
					playMusic(list.get(current).get("music_url").toString());
					status = 0x12;
				}
			});
		} catch (IOException e) {
		}
	}

	/* �����б� */
	/*
	 * public void musicList() { // ȡ��ָ��λ�õ��ļ�������ʾ�������б� String[] music = new
	 * String[] { Media._ID, Media.DISPLAY_NAME, Media.TITLE, Media.DURATION,
	 * Media.ARTIST, Media.DATA }; Cursor cursor =
	 * getContentResolver().query(Media.EXTERNAL_CONTENT_URI, music, null, null,
	 * null); while (cursor.moveToNext()) { Music temp = new Music();
	 * temp.setFilename(cursor.getString(1));
	 * temp.setTitle(cursor.getString(2)); temp.setDuration(cursor.getInt(3));
	 * temp.setArtist(cursor.getString(4)); temp.setData(cursor.getString(5));
	 * musiclists.add(temp);
	 * 
	 * Map<String, Object> map = new HashMap<String, Object>();
	 * map.put("music_name", cursor.getString(1)); map.put("music_art",
	 * cursor.getString(4)); list.add(map); } }
	 */
	public void musicList(int who) {

		if (who == 1) {
			list = MainActivity.list;
			count = list.size();
		} else {
			list = SecondMain.list;
			count = list.size();
		
		}

	}

	public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int control = intent.getIntExtra("control", -1);
			int who=intent.getIntExtra("who", -1);
			musicList(who);
			switch (control) {
			case 1: {
				// ���ԭ������ֹͣ��״̬
				if (status == 0x11) {
					playMusic(list.get(current).get("music_url").toString());
					status = 0x12;
				}
				// ԭ�����ڲ���״̬
				else if (status == 0x12) {
					mPlayer.pause();
					Log.e("pause", "zanting");
					status = 0x13;
				}
				// ԭ��������ͣ״̬
				else if (status == 0x13) {
					mPlayer.start();
					status = 0x12;
				}
				break;
			}
			case 2: {
				// ���ԭ�����ڲ��Ż���ͣ
				if (status == 0x12 || status == 0x13) {
					mPlayer.stop();
					status = 0x11;
				}
				break;
			}
			case 3: {
				current--;
				if (current < 0) {
					current = count - 1;
				}
				playMusic(list.get(current).get("music_url").toString());
				status = 0x12;
				break;
			}
			case 4: {
				current++;
				if (current > count - 1) {
					current = 0;
				}
				playMusic(list.get(current).get("music_url").toString());
				status = 0x12;
				break;
			}
			case 5: {
				current = intent.getIntExtra("current", -1);
				;
				playMusic(list.get(current).get("music_url").toString());
				status = 0x12;
				break;
			}
			}
			Intent sendIntent = new Intent(MainActivity.UPDATE_ACTION);
			sendIntent.putExtra("update", status);
			// sendIntent.putExtra("current", current);
			sendBroadcast(sendIntent);
		}

	}
}
