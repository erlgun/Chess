package simplechess.board;

import java.awt.image.WritableRaster;

/*
 * Alf-Andre Walla, 2012
 * 
 * This class manages a starfield.
 * Extra fluff added to the background of the chessboard.
 * 
 * Warning: if initStars() is initialized with sizes outside the board bounds,
 * the drawStars() procedure will crash!
 * 
 * I originally wanted the stars to animate (flicker), but it might simply
 * be too costly to update the background on a regular basis. This can be seen
 * from the animation constants and functions.
 * 
 */

public class Stars {
    
    private final int NUM_STARS = 200;
    private Star stars[];

    private class Star {
        
        public static final float STARSPEED = 0.25f;
        public static final int MAXTICKS = 100;
        
        public int x, y, rad, waitticks;
        public int color[]; // star-color using lumatest
        private float progress; // 0.0f to 1.0f = start shining, 1.0f to 2.0f = stop
        
        public Star(int x, int y) {
            
            this.x = x;
            this.y = y;
        }
        
        public void tick() {
            if (waitticks != 0)
                waitticks--;
            else {
                progress += STARSPEED;
                if (progress >= 2.0f) {
                    progress = 0.0f;
                    waitticks = (int) Math.random() * MAXTICKS;
                }
            }
        }

    }
    
    // initializes the stars array, and creates NUM_STARS random stars
    // Warning: if sizeX or sizeY is outside the bounds of the image buffer,
    // the drawStars procedure will likely fail.
    
    public void initStars(int sizeX, int sizeY) {

        if (stars == null) stars = new Star[NUM_STARS];
        
        Star s;
        
        for (int i = 0; i < NUM_STARS; i++) {

            stars[i] = new Star( (int)(Math.random() * sizeX), 
                                 (int)(Math.random() * sizeY)
                                );
            s = stars[i];
            s.color = new int[3];
            s.color[0] = (int)(Math.random() * 256.0d);
            s.color[1] = (int)(Math.random() * 256.0d);
            s.color[2] = (int)(Math.random() * 256.0d);
            s.waitticks = (int)(Math.random() * Star.MAXTICKS);
            
        }

    }
    
    // draws all the initialized stars from the stars array
    // onto an image buffers writable raster
    
    public void drawStars(WritableRaster wr) {
        
        if (wr == null) return;
        
        int dx;
        int[] starcolor = new int[3];
        
        final int starsize = 5;
        final float starpower = 3.f;
        
        int w = wr.getWidth();
        int h = wr.getHeight();
        
        for (Star s : stars) {
            
            if (s != null) {
                
                for (dx = -starsize; dx <= starsize; dx++) {
                    
                    for (int c = 0; c < 3; c++)
                        starcolor[c] = rgb3.clamp( (int)(s.color[c] * ( (float)starsize - Math.abs(dx)) / starpower) );
                    
                    if (s.x + dx >= 0 && s.x + dx < w)
                        wr.setPixel(s.x + dx, s.y, starcolor);
                    if (s.y + dx >= 0 && s.y + dx < h)
                        wr.setPixel(s.x, s.y + dx, starcolor);
                    
                }
                
            }
            
        }
        
    }
    
}
