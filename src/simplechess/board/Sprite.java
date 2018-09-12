package simplechess.board;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Alf-Andre Walla, 2012
 * 
 * Sprite class
 * Manages sprites used on the chessboard
 * 
 * Contains simple mask function to hide a specific color
 *  
**/

public final class Sprite {
    
    BufferedImage sprite;
    int width, height;
    
    // MASK_COLOR is the current filtered out color on a sprite
    private final int[] MASK_COLOR = {255, 255, 255};
    
    public Sprite() {}
    
    public Sprite(BufferedImage bi, int x, int y, int w, int h) {
        
        sprite = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        
        blit(bi, sprite, x, y, w, h);
        mask(MASK_COLOR);
        
        width = w; height = h;
    }
    
    public Sprite(char c, int w, int h) {
        
        sprite = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        
        /*
         * Chessboard characters
            &#9812; = ♔
            &#9813; = ♕
            &#9814; = ♖
            &#9815; = ♗
            &#9816; = ♘
            &#9817; = ♙
            &#9818; = ♚
            &#9819; = ♛
            &#9820; = ♜
            &#9821; = ♝
            &#9822; = ♞
            &#9823; = ♟
        */
        sprite.getGraphics().drawString(String.valueOf(c), 0, 0);
        
        //mask(MASK_COLOR);
        
        width = w; height = h;
    }
    
    // mask(): masks a given colorsample (maskColor) on this sprite image
    // this means, for each color on this sprite, if that color is maskColor,
    // the alpha channel for that sample is set to 0, effectively hiding that color
    // in practice this means the color will be invisible when this sprite is
    // drawn onto another image. See: http://en.wikipedia.org/wiki/Alpha_compositing
    
    public void mask(int[] maskColor) {
        
        WritableRaster wr = sprite.getRaster();
        
        Color mask = new Color(maskColor[0], maskColor[1], maskColor[2], 255);
        
        for (int y = 0; y < sprite.getHeight(); y++)
        for (int x = 0; x < sprite.getWidth() ; x++) {
            
            if (sprite.getRGB(x, y) == mask.getRGB())
                wr.setSample(x, y, 3, 0); // set alpha(3) to 0
                
        }
        
    }
    
    // loads image to sprite bufferedimage from a file or another resource (url?)
    // returns true if operation successful
    
    public boolean load(String fname) {
        
        sprite = null;
        
        try {
            
            BufferedImage temp = ImageIO.read(
                    Thread.currentThread().getContextClassLoader().getResource(fname)
                    );
            
            width = temp.getWidth();
            height = temp.getHeight();
            
            sprite = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            sprite.getGraphics().drawImage(temp, 0, 0, null);
            
            return true;
            
        } catch (IOException e) {
            
            return false;
            
        }
        
    }
    
    // originally wrote a bitblock transfer function here,
    // but java has internal conversion between samplemodels and so i used drawImage()
    // This function is used to copy a smaller rectangle from source to (0,0) on dest
    
    private void blit(final BufferedImage src, final BufferedImage dst, int sx, int sy, int w, int h) {
        
        dst.getGraphics().drawImage(src, 0, 0, w, h, sx, sy, sx+w, sy+h, null);
        
    }
    
}
