package miscperipherals.util;

import java.util.LinkedList;
import java.util.List;

public class BFMachine implements Runnable {
	private String code;
	private String input;
	private List<BFEventHandler> handlers = new LinkedList<BFEventHandler>();
	public String output;
	
	private int pc = 0;
	private int head = 0;
	private int[] tape = new int[256];
	private int inputHead = -1;
	private int depth = 0;
	private int[] depthpos = new int[256];
	
	public BFMachine(String code) throws BFException {
		setCode(code);
	}
	
	public void setCode(String code) throws BFException {
		int open = 0;
		int close = 0;
		for (int i = 0; i < code.length(); i++) {
			char c = code.charAt(i);
			if (c == '[') open++;
			else if (c == ']') close++;
		}
		if (open != close) throw new BFException("parse error: unmatched brackets: " + open + " != " + close);
		
		this.code = code;
	}
	
	public void setInput(String input) {
		this.input = input;
	}
	
	public void addEventHandler(BFEventHandler handler) {
		handlers.add(handler);
	}
	
	public boolean tick() throws BFException {
		if (pc >= code.length()) {
			for (BFEventHandler handler : handlers) handler.onEnd(output);
			return false;
		}
		
		char c = code.charAt(pc);
		switch (c) {
			case '>': {
				if (++head > tape.length) head = 0;
				pc++;
				break;
			}
			case '<': {
				if (--head < 0) head = tape.length - 1;
				pc++;
				break;
			}
			case '+': {
				tape[head]++;
				pc++;
				break;
			}
			case '-': {
				tape[head]--;
				pc++;
				break;
			}
			case '.': {
				for (BFEventHandler handler : handlers) handler.onOutput(output, "" + (char)tape[head]);
				output += (char)tape[head];
				pc++;
				break;
			}
			case ',': {
				if (input == null || ++inputHead >= input.length()) {
					throw new BFException("at " + pc + ": reading beyond input size");
				}
				
				tape[head] = input.charAt(inputHead);
				pc++;
				break;
			}
			case '[': {
				depth++;
				if (depth >= depthpos.length) {
					throw new BFException("at " + pc + ": code is too deep");
				}
				depthpos[depth] = pc;
				if (tape[head] != 0) pc++;
				else {
					int j = 0;
					while (j < depth) {
						pc++;
						if (code.charAt(pc) == ']') j++;
					}
					pc++;
				}
				break;
			}
			case ']': {
				if (tape[head] != 0) pc = depthpos[depth];
				else pc++;
				depth--;
				break;
			}
			case '$': {
				for (BFEventHandler handler : handlers) handler.onInsta(tape[head]);
				pc++;
				break;
			}
			default: {
				pc++;
				break;
			}
		}
		
		return true;
	}
	
	/**
	 * Autonomous run
	 */
	@Override
	public void run() {
		try {
			while (tick()) {}
		} catch (BFException e) {
			for (BFEventHandler handler : handlers) handler.onError(e);
		}
	}
	
	public static class BFException extends IllegalStateException {
		public BFException(String s) {
			super(s);
		}
	}
	
	public static interface BFEventHandler {
		public void onError(BFException e);
		
		public void onInsta(int value);
		
		public void onOutput(String output, String add);
		
		public void onEnd(String output);
	}
	
	/** Ye olde BF machine
	 private static class BFExecutor implements Runnable {
		private final IComputerAccess computer;
		private final double id;
		private final String code;
		private final String input;
		
		public BFExecutor(IComputerAccess computer, double id, String code, String input) {
			this.computer = computer;
			this.id = id;
			this.code = code;
			this.input = input;
		}
		
		@Override
		public void run() {
			try {
				execute();
			} catch (Throwable e) {
				computer.queueEvent("bf_fail", new Object[] {id, e.getClass() == Exception.class ? e.getMessage() : e.toString()});
			}
		}
		
		private void execute() throws Exception {
			int head = 0;
			int[] tape = new int[256];
			int inputHead = -1;
			int depth = 0;
			int[] depthpos = new int[256];
			
			int open = 0;
			int close = 0;
			for (int i = 0; i < code.length(); i++) {
				char c = code.charAt(i);
				if (c == '[') open++;
				else if (c == ']') close++;
			}
			if (open != close) throw new Exception("parse error: unmatched brackets: "+open+" != "+close);
			
			String output = "";
			long start = System.currentTimeMillis();
			int i = 0;
			while (i < code.length()) {
				char c = code.charAt(i);
				switch (c) {
					case '>': {
						if (++head > tape.length) throw new Exception("at "+i+": seeking after tape length of "+tape.length);
						i++;
						break;
					}
					case '<': {
						if (--head < 0) throw new Exception("at "+i+": seeking before tape start");
						i++;
						break;
					}
					case '+': {
						tape[head]++;
						i++;
						break;
					}
					case '-': {
						tape[head]--;
						i++;
						break;
					}
					case '.': {
						output += (char)tape[head];
						i++;
						break;
					}
					case ',': {
						if (input == null || ++inputHead >= input.length()) {
							throw new Exception("at "+i+": reading beyond input size");
						}
						
						tape[head] = input.charAt(inputHead);
						i++;
						break;
					}
					case '[': {
						depth++;
						depthpos[depth] = i;
						if (tape[head] != 0) i++;
						else {
							int j = 0;
							while (j < depth) {
								i++;
								if (code.charAt(i) == ']') j++;
							}
							i++;
						}
						break;
					}
					case ']': {
						if (tape[head] != 0) i = depthpos[depth];
						else i++;
						depth--;
						break;
					}
					case '$': {
						computer.queueEvent("bf_insta", new Object[] {id, tape[head]});
						i++;
						break;
					}
					default: {
						i++;
						break;
					}
				}
				
				if (System.currentTimeMillis() - start > 10000L) throw new Exception("executing for too long");
			}
			
			computer.queueEvent("bf_done", new Object[] {id, output});
		}
	} 
	 */
}
