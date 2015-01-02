package miscperipherals.speech;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * http://stackoverflow.com/questions/14165517/processbuilder-capturing-stdout-and-stderr-of-started-processes-to-another-stre
 */
public class StreamGobbler extends Thread {
	private final InputStream is;
	private final OutputStream os;

	public StreamGobbler(InputStream is, OutputStream os) {
		this.is = is;
		this.os = os;
		start();
	}

	@Override
	public void run() {
		try {
			byte[] buf = new byte[8192];
			int read;
			while ((read = is.read(buf)) != -1) {
				os.write(buf, 0, read);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
