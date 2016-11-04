package com.example.com.hls.play2.online;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.example.com.hls.play2.online.GetJson.TRACKS;
import com.google.gson.Gson;


/**
 * Created by Administrator on 2016/2/5.
 */
public class Json_data {
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	public List<Map<String, Object>> gs(String ss)
	{
		String json=ss;
		Gson gson = new Gson();
		GetJson databean = gson.fromJson(json, GetJson.class);
		ArrayList<TRACKS> t = databean.result.tracks;
		for (GetJson.TRACKS s : t) {
		String _url= s.mp3Url;
		String _name=s.name;
		String _art=s.artists.get(0).name;
		Map<String, Object> map = new HashMap<String, Object>();
		Log.e("连接地址：", _url);
	    Log.e("歌名", _name);
	    Log.e("歌手", _art);
	    map.put("music_url", _url);
		map.put("music_name", _name);
		map.put("music_art", _art);
		list.add(map);
	}
		return list;
	}
	
}