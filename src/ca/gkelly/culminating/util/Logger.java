package ca.gkelly.culminating.util;

import java.util.HashMap;

public class Logger {
	/** Info log level */
	public static final byte INFO = 0b100;
	/** Debug log level */
	public static final byte DEBUG = 0b010;
	/** Error log level */
	public static final byte ERROR = 0b001;
	/** All log level, will print if any level is enabled */
	public static final byte ALL = 0b111;

	/** Info level enable */
	public static boolean infoEnabled = true;
	/** Debug level enable */
	public static boolean debugEnabled = true;
	/** Error level enable */
	public static boolean errorEnabled = true;

	/** Width of timestamp */
	public static final int TIME_WIDTH = 9;
	/** Width of trace */
	public static final int TRACE_WIDTH = 26;

	/** Saved copy of log, to be used for file output */
	static String log = "";
	/** Epoch of initialization, default epoch */
	final static long EPOCH_INIT = System.currentTimeMillis();

	/** All additional epochs */
	private static HashMap<String, Long> epochs = new HashMap<>();

	/**
	 * Write message to log
	 * 
	 * @param type       Message type
	 * @param message    Message payload
	 * @param terminator Message terminator (newline or not)
	 * @param discreete  If true, hides timestamp, trace, etc
	 * @param epoch      String used to select timestamp epoch, <code>INIT</code>
	 *                   will use default
	 */
	public static void log(byte type, Object message, String terminator, boolean discreete, String epoch) {
		String head = "";
		// Add log level to header,
		if((type & ALL) == ALL) {
			head += "ALL  |";
		} else if(((type & ERROR) == ERROR && errorEnabled)) {
			head += "ERROR|";
		} else if(((type & DEBUG) == DEBUG && debugEnabled)) {
			head += "DEBUG|";
		} else if(((type & INFO) == INFO && infoEnabled)) {
			head += "INFO |";
		} else {
			return;
		}

		// Get time
		String time = "";
		// If initial, or timestamp DNE
		if(epoch == "INIT" || !epochs.containsKey(epoch)) {
			// Get time string
			time = "" + (System.currentTimeMillis() - EPOCH_INIT);
			// Add apropriate whitespace
			for(int i = 0;i < TIME_WIDTH - time.length();i++)
				head += " ";
		} else {
			// Get time string
			time = "" + (System.currentTimeMillis() - epochs.get(epoch));
			// Add epoch identifier
			head += epoch;
			// Add whitespace
			for(int i = 0;i < TIME_WIDTH - time.length() - epoch.length();i++)
				head += " ";
		}
		// Add time to header
		head += time + "|";

		// Get trace
		String trace = getTrace();
		// Add trace
		head += trace;
		// Add whitespace
		for(int i = 0;i < TRACE_WIDTH - trace.length();i++)
			head += " ";

		// Add head and message
		if(message instanceof String) {
			if(((String) message).contains("{")) {
				message = insertTime((String) message);
			}
		}
		String out = head + "| " + message + terminator;
		if(discreete) {
			out = message + terminator;
		}

		// Output to console
		System.out.print(out);
		// Save to log
//		log += out;
	}

	/**
	 * Write message to log
	 * 
	 * @param type       Message type
	 * @param message    Message payload
	 * @param terminator Message terminator (newline or not)
	 * @param discreete  If true, hides timestamp, trace, etc
	 */
	public static void log(byte type, Object message, String terminator, boolean discreete) {
		log(type, message, terminator, discreete, "INIT");
	}

	/**
	 * Write message to log
	 * 
	 * @param type       Message type
	 * @param message    Message payload
	 * @param terminator Message terminator (newline or not)
	 */
	public static void log(byte type, Object message, String terminator) {
		log(type, message, terminator, false);
	}

	/**
	 * Write message to log
	 * 
	 * @param type    Message type
	 * @param message Message payload
	 */
	public static void log(byte type, Object message) {
		log(type, message, "\n");
	}

	/**
	 * Write message to log<br/>
	 * Uses <code>DEBUG</code> type
	 * 
	 * @param message Message payload
	 */
	public static void log(Object message) {
		log(DEBUG, message);
	}

	/**
	 * Write message to log with specified epoch<br/>
	 * Uses <code>DEBUG</code> type
	 * 
	 * @param message Message payload
	 * @param epoch   The epoch identifier
	 */
	public static void log(Object message, String epoch) {
		log(DEBUG, message, "\n", false, epoch);
	}

	/**
	 * Get the trace for use with output<br/>
	 * <strong>Format:</strong> Class.func():line
	 * 
	 * @return The trace formatted for logging
	 */
	private static String getTrace() {
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		// Skip first element, it is unwanted
		for(int i = 1;i < trace.length;i++) {
			if(trace[i].getClassName().contains("Logger")) {
				continue;
			}
			String s = "";
			s += trace[i].getClassName().split("\\.")[trace[i].getClassName().split("\\.").length - 1] + ".";
			s += trace[i].getMethodName() + "():";
			s += trace[i].getLineNumber();
			return s;
		}
		return "Error";
	}
	
	private static String insertTime(String message) {
		String[] stamps = message.split("\\{");
		message = "";
		for(int i = 0; i < stamps.length; i++) {
			if(!stamps[i].contains("}")) {
				message+=stamps[i];
				continue;
			}
			String stamp = stamps[i].split("}")[0];
			String time = (System.currentTimeMillis()-epochs.get(stamp))+"ms";
			message+=time;
		}
		
		return message;
	}

	/** Output a blank line, when selected level is enabled */
	public static void newLine(byte type) {
		if((type & ALL) == ALL) {
		} else if(((type & ERROR) == ERROR && errorEnabled)) {
		} else if(((type & DEBUG) == DEBUG && debugEnabled)) {
		} else if(((type & INFO) == INFO && infoEnabled)) {
		} else {
			return;
		}
		System.out.println("");
	}

	/**
	 * Save a new epoch
	 * 
	 * @param id Identifier to be used with the new epoch, can not exceed 4 characters
	 */
	public static void epoch(String id) {
		if(id.length() > 4)
			id = id.substring(0, 4);
		epochs.put(id, System.currentTimeMillis());
	}
}