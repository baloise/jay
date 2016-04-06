package jay.monitor.sensor;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class URLSupport implements Configurable, ActionListener{
	private URL url;

	@Override
	public void configure(Properties props) {
		try {
			url = new URL(props.getProperty("url"));
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}

	}
	
	public URL getUrl() {
		return url;
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
	
	  @Override
	  public void actionPerformed(ActionEvent e) {
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {
	            desktop.browse(url.toURI());
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
	  }

}
