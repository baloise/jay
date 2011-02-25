package jay.monitor.sensor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HTTPSensor extends AbstractSensor {

  private URL url;

  public HTTPSensor(String name, String url) throws MalformedURLException {
    super(name);
    this.url = new URL(url);
	}

  public HTTPSensor(String aName, String url, long aDelay) throws MalformedURLException {
    super(aName, aDelay);
    this.url = new URL(url);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected long sens() {
    try {
      HttpURLConnection con = (HttpURLConnection)url.openConnection();
      int responseCode = con.getResponseCode();
      if (200 > responseCode || responseCode >= 300) {
        return 0;
      }
    }
    catch (IOException e) {
      return 0;
    }
    return 1;
  }

  public String readUrl() throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
    String inputLine;
    StringBuffer ret = new StringBuffer();
    while ((inputLine = in.readLine()) != null) {
      ret.append(inputLine);
      ret.append("\n");
    }
    in.close();
    return ret.toString();
  }

}
