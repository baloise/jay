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
    System.out.println("Update jekins");
    jenkins.update();
    return Color.green.equals(jenkins.color) ? 1 : 0;
  }

}
