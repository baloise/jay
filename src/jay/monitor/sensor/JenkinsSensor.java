package jay.monitor.sensor;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class JenkinsSensor extends AbstractSensor implements ActionListener {

  Jenkins jenkins;
  private URI uri;
  
  public JenkinsSensor(Properties props) {
    super(props);
    try {
      uri = new URI(props.getProperty("url"));
    }
    catch (URISyntaxException e) {
      e.printStackTrace();
    }
    jenkins = Jenkins.getState(props.getProperty("url"));
  }

  @Override
  protected double sens() {
    jenkins.update();
    if(Color.green.equals(jenkins.color)) {
      return 0;
    } else  if(Color.yellow.equals(jenkins.color)) {
      return 0.5;
    } else  {
      return 1;
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
        try {
            desktop.browse(uri);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
  }

}
