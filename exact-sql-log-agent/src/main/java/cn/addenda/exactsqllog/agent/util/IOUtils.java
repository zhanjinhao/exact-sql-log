package cn.addenda.exactsqllog.agent.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class IOUtils {

  public static byte[] toByteArray(URL url) throws IOException {
    URLConnection conn = url.openConnection();
    try {
      return IOUtils.toByteArray(conn);
    } finally {
      close(conn);
    }
  }


  public static byte[] toByteArray(URLConnection urlConn) throws IOException {
    InputStream inputStream = urlConn.getInputStream();
    try {
      return IOUtils.toByteArray(inputStream);
    } finally {
      inputStream.close();
    }
  }


  public static byte[] toByteArray(InputStream input) throws IOException {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    copy(input, output);
    return output.toByteArray();
  }

  public static int copy(InputStream input, OutputStream output) throws IOException {
    long count = copyLarge(input, output);
    if (count > Integer.MAX_VALUE) {
      return -1;
    }
    return (int) count;
  }

  private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

  public static long copyLarge(InputStream input, OutputStream output)
          throws IOException {
    return copyLarge(input, output, new byte[DEFAULT_BUFFER_SIZE]);
  }

  private static final int EOF = -1;

  public static long copyLarge(InputStream input, OutputStream output, byte[] buffer)
          throws IOException {
    long count = 0;
    int n = 0;
    while (EOF != (n = input.read(buffer))) {
      output.write(buffer, 0, n);
      count += n;
    }
    return count;
  }

  /**
   * Closes a URLConnection.
   *
   * @param conn the connection to close.
   * @since 2.4
   */
  public static void close(URLConnection conn) {
    if (conn instanceof HttpURLConnection) {
      ((HttpURLConnection) conn).disconnect();
    }
  }

}
