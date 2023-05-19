package Game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImageComponent extends JPanel{
    private BufferedImage image;
    
    public ImageComponent(BufferedImage image){
           this.image = image;
        
    }
    
    //Paints the background for the warrior and weapon selection menus
    public void paintComponent(Graphics g) {

          Graphics2D g2d = (Graphics2D)g;
          int x = 0; 
          int y = 0;
          g2d.drawImage(image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH), x, y, this);
         
    }
    
    //Generates bufferedImage from String path
    public void setImage(String urlFoto) {
        try {
            this.image = ImageIO.read(new File(urlFoto));
        } catch (IOException e) {
            e.printStackTrace();
        }
        repaint();
    }
}
