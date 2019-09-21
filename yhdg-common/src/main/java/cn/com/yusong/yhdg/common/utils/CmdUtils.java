package cn.com.yusong.yhdg.common.utils;

import cn.com.yusong.yhdg.common.constant.Constant;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class CmdUtils {

    static Logger log = LoggerFactory.getLogger(CmdUtils.class);

	public static int syncExec(String cmd) throws InterruptedException, IOException {
		Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec( cmd );
        
        Thread thread1 = new Thread(new EmptyConsumer(process.getInputStream()));
        thread1.start();
        
        Thread thread2 = new Thread(new EmptyConsumer(process.getErrorStream()));
        thread2.start();
        
        return process.waitFor();
	}

    public static int syncExec(String cmd, OutputStream stdOut, OutputStream errOut) throws InterruptedException, IOException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec( cmd );

        Thread thread1 = new Thread(new StreamConsumer(process.getInputStream(), stdOut));
        thread1.start();

        Thread thread2 = new Thread(new StreamConsumer(process.getErrorStream(), errOut));
        thread2.start();

        return process.waitFor();
    }

    public static int syncExec(String cmd, LineListener stdLineListener, LineListener errorLineListener) throws InterruptedException, IOException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec( cmd );

        if(stdLineListener == null) {
            Thread thread1 = new Thread(new EmptyConsumer(process.getInputStream()));
            thread1.start();
        } else {
            Thread thread1 = new Thread(new LineConsumer(process.getInputStream(), stdLineListener));
            thread1.start();
        }

        if(errorLineListener == null) {
            Thread thread2 = new Thread(new EmptyConsumer(process.getErrorStream()));
            thread2.start();
        } else {
            Thread thread2 = new Thread(new LineConsumer(process.getErrorStream(), errorLineListener));
            thread2.start();
        }

        return process.waitFor();
    }
	
	public static class EmptyConsumer implements Runnable {

		private InputStream stream;
		
		public EmptyConsumer(InputStream stream) {
			super();
			this.stream = stream;
		}

		@Override
		public void run() {
			int count = 0;
			byte[] buf = new byte[1024];
			try {
				
				while( (count = stream.read(buf)) != -1 ) {
				}
			} catch (IOException e) {
			} finally {
				IOUtils.closeQuietly(stream);
			}
		}
	}

    public static class StreamConsumer implements Runnable {

        private InputStream inputStream;
        private OutputStream outputStream;

        public StreamConsumer(InputStream inputStream, OutputStream outputStream) {
            super();
            this.inputStream = inputStream;
            this.outputStream = outputStream;
        }

        @Override
        public void run() {
            int count = 0;
            byte[] buf = new byte[1024];
            try {

                while( (count = inputStream.read(buf)) != -1 ) {
                    outputStream.write(buf, 0, count);
                }
            } catch (IOException e) {
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
        }
    }

    public static class LineConsumer implements Runnable {

        private InputStream inputStream;
        private LineListener lineListener;

        public LineConsumer(InputStream inputStream, LineListener lineListener) {
            super();
            this.inputStream = inputStream;
            this.lineListener = lineListener;
        }

        @Override
        public void run() {
            String line = null;
            try {
                InputStreamReader reader = new InputStreamReader(inputStream, Constant.ENCODING_UTF_8);
                BufferedReader buf = new BufferedReader(reader);
                try {
                    while( (line = buf.readLine()) != null ) {
                        lineListener.receive(line);
                    }
                } catch (IOException e) {
                    log.error("LineConsumer.run()", e);
                } finally {
                    IOUtils.closeQuietly(inputStream);
                    IOUtils.closeQuietly(reader);
                    IOUtils.closeQuietly(buf);
                }
            } catch (Exception ex) {
                log.error("LineConsumer.run()", ex);
            }
        }
    }

    public static interface LineListener {
        public void receive(String line);
    }
}
