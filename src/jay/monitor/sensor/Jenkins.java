package jay.monitor.sensor;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Jenkins {

  
  public final URL url;


  private Jenkins(String url) {
    try {
      this.url = new URL(url+"/api/xml");
    }
    catch (MalformedURLException e) {
      throw new IllegalArgumentException(e);
    }
    update();
  }

  public void update() {
    for(String color : getColors()) {
      evaluateColor(color);
    }
  }
  
  Color[] colors = new Color[] {
      Color.green,
      Color.yellow,
      Color.red,
  };

  public boolean building = false;
  public int colorCode = -1;
  public Color color = Color.gray;
  
  // Dirty hack to support switch on string with java 6
  final int blue = 4;
  final int yellow = 6;
  final int red = 3;
  final int   blue_anime = blue+6;
  final int yellow_anime = yellow+6;
  final int    red_anime =   red+6;
  // end dirty hack
  
  private void evaluateColor(String colorString) {
          switch (colorString.length()) {
            case blue :
              colorCode = Math.max(colorCode,0);
              break;
            case yellow :
              colorCode = Math.max(colorCode,1);
              break;
            case red :
              colorCode = Math.max(colorCode,2);
              break;
            case blue_anime :
              colorCode = Math.max(colorCode,0);
              building = true;
              break;
            case yellow_anime :
              colorCode = Math.max(colorCode,1);
              building = true;
              break;
            case red_anime :
              colorCode = Math.max(colorCode,2);
              building = true;
              break;
          }
          color = colors[colorCode];
  }

  private Set<String> getColors() {
    Set<String> ret = new HashSet<String>();
    final String regEx = "<color>(\\w+)</color>";
    Pattern pattern = Pattern.compile(regEx);
    try {
      BufferedReader in = new BufferedReader(
      new InputStreamReader(url.openStream()));
      String inputLine;
      Matcher m = null;
      while ((inputLine = in.readLine()) != null)
          m = pattern.matcher(inputLine);
          while(m.find()) {
            ret.add(m.group(1));
          }
      in.close();
    } catch (Exception e){
      e.printStackTrace();
    }
    return ret;
  }

  public static Jenkins getState(String url) {
    return new Jenkins(url);
  }

}
