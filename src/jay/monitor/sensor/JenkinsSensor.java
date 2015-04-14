package jay.monitor.sensor;

import java.awt.Color;
import java.util.Properties;

public class JenkinsSensor extends AbstractSensor {

  Jenkins jenkins;
  
  public JenkinsSensor(Properties props) {
    super(props);
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

}
