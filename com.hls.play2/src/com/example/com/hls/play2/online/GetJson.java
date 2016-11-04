package com.example.com.hls.play2.online;

import java.util.ArrayList;

public class GetJson {
	public RESULT result;

	public class RESULT {
		
	public ArrayList<TRACKS> tracks;
		
	}
	public class TRACKS {
		public String mp3Url;
		public String name;
		public ArrayList<ARTISTS> artists;
	}
	public class ARTISTS{
		public String name;
	}
}
