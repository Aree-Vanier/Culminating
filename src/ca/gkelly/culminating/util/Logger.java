package ca.gkelly.culminating.util;


public class Logger {
	public static final byte INFO = 0b100;
	public static final byte DEBUG = 0b010;
	public static final byte ERROR = 0b001;
	public static final byte ALL = 0b111;

	public static boolean infoEnabled = true;
	public static boolean debugEnabled = true;
	public static boolean errorEnabled = true;
	
	public static final int TIME_WIDTH = 7;

	static String log = "";
	static long epoch = System.currentTimeMillis();

	public static void log(byte type, Object message, Object sender, String terminator, boolean discreete) {
		String s = "";
		if((type & ALL) == ALL) {
			s+="ALL  |";
		} else if(((type & ERROR) == ERROR && errorEnabled)) {
			s+="ERROR|";
		} else if(((type & DEBUG) == DEBUG && debugEnabled)) {
			s+="DEBUG|";
		} else if(((type & INFO) == INFO && infoEnabled)) {
			s+="INFO |";
		} else {
			return;
		}
		String time = ""+(System.currentTimeMillis()-epoch);
		for(int i = 0; i<TIME_WIDTH-time.length(); i++)
			s+=" ";
		s+=time+"|";
		String sendID = sender.getClass().getSimpleName();
		s+=sendID;
		for(int i = 0; i<10-sendID.length(); i++)
			s+=" ";
		s+="| "+message+terminator;
		if(discreete) {
			s=message+terminator;
		}
		System.out.print(s);
//		log+=s+message;
	}
	
	public static void log(byte type, Object message, Object sender, String terminator) {
		log(type, message, sender, terminator, false);
	}
	
	public static void log(byte type, Object message, Object sender) {
		log(type, message, sender, "\n");
	}
	
	
	public static void newLine() {
		System.out.println("");
	}
}
