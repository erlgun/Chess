package simplechess.board;

/*
 * Alf-Andre Walla, 2012
 * 
 * Class for simplified modulation of colors
 * Specifically intended to be used with BufferedImage
 * 
 * Contains functions to modulate colors using various techniques.
 * 
 */

public class rgb3 {
    
    private static final double MINKOWKSI = 16.0f;
    
    public int r, g, b;
    
    // returns a color-sample (TYPE_INT_RGB) from datafields r,g,b
    // http://docs.oracle.com/javase/1.4.2/docs/api/java/awt/image/BufferedImage.html#TYPE_INT_RGB
    
    public int[] getInts() {
        return new int[] {r, g, b};
    }
    
    // sets this color to white
    
    public void white() {
        r = 255; g = 255; b = 255;
    }
    
    // sets this color to black
    
    public void black() {
        r = 0; g = 0; b = 0;
    }
    
    // inverts this color, or can be seen as additive to subtractive color
    // and vice versa, though that does not apply here
    
    public void invert() {
        r = 255 - r; g = 255 - g; b = 255 - b;
    }
    
    // corona calculates color from minkowski distance and color separation
    // where d = 2.0f equates to the classic euclidian distance formula
    
    public void corona(float x, float y, float d, float p, int str) {
        
        if (p < 1.0f) {
            float dist = 1.0f - getMinkowski(x, y, d);
            excite(str * (float)Math.pow( dist, 1.0f / p ) );
            
        } else {
            float dist = 1.0f - getDist(x, y, d);
            excite(str * (float)Math.pow( dist, p ) );
        }
        
    }
    
    // power ramps a color by addition only
    // will give strange results with negative parameters
    
    public void colorize(int r, int g, int b, float e) {
        
        // inverse dynamic colorizer!
        
        int a = pow(this.r + r, e);
        if (a > 255) {
            this.g -= pow(r, e); // * 0.5;
            this.b -= pow(r, e); // * 0.5;
        } else {
            this.r = a;
        }
        
        a = pow(this.g + g, e);
        if (a > 255) {
            this.r -= pow(g, e); // * 0.5;
            this.b -= pow(g, e); // * 0.5;
        } else {
            this.g = a;
        }
        
        a = pow(this.b + b, e);
        if (a > 255) {
            this.r -= pow(b, e); // * 0.5;
            this.g -= pow(b, e); // * 0.5;
        } else {
            this.b = a;
        }
        
        hdrclamp3(0, 1.f);
        
    }
    
    // simple function to separate or distinguish a color from another linearly
    
    public void excite(float e) {
        
        if (r + e <= 255) r += e; else r -= e;
        if (g + e <= 255) g += e; else g -= e;
        if (b + e <= 255) b += e; else b -= e;
        
    }
    
    // adds value d to color, ramps it by exp and clamps it using
    // a simplified dynamic range formula:
    // a channel too bright means clamping it, and adding the remainder to the
    // remaining channels, and at the end applying a simple clamp again
    
    public void hdrclamp3(int d, float exp) {
        
        // emulate dynamic range
        this.r = pow(this.r + d, exp);
        this.g = pow(this.g + d, exp);
        this.b = pow(this.b + d, exp);
        
        if (this.r > 255) {
            int diff = this.r - 255;
            this.g += diff * 0.5;
            this.b += diff * 0.5;
        }
        if (this.g > 255) {
            int diff = this.g - 255;
            this.r += diff * 0.5;
            this.b += diff * 0.5;
        }
        if (this.b > 255) {
            int diff = this.b - 255;
            this.r += diff * 0.5;
            this.g += diff * 0.5;
        }
        
        // clamp
        if (this.r > 255) this.r = 255;
        if (this.g > 255) this.g = 255;
        if (this.b > 255) this.b = 255;
        if (this.r <   0) this.r =   0;
        if (this.g <   0) this.g =   0;
        if (this.b <   0) this.b =   0;
        
    }
    
    // clamps an integer to 8bits range rgb channel values
    
    public static int clamp(int i) {
        if (i <= 0) return 0;
        if (i >= 255) return 255;
        return i;
    }
    
    // linear interpolation for colors, aka mix(), where mixamount is normalized 0.0 to 1.0
    
    public static int[] mix(int[] a, int[] b, float mix) {
        
        return new int[]
        {
            (int)((float)a[0] * (1.0f - mix) + (float)b[0] * mix),
            (int)((float)a[1] * (1.0f - mix) + (float)b[1] * mix),
            (int)((float)a[2] * (1.0f - mix) + (float)b[2] * mix)
        };
    }
    
    // a basic curve / exponential ramp
    
    public static int pow(int i, double p) {
        return (int) (Math.pow((double) i / 255.0, p) * 255.0);
    }
    
    // minkowski distance generalized euclidian/manhattan
    // http://en.wikipedia.org/wiki/Minkowski_distance
    
    private static float getMinkowski(float dx, float dy, float maxdist) {
        
        float dist = (float) Math.pow( Math.pow( Math.abs(dx), MINKOWKSI )
                             + Math.pow( Math.abs(dy), MINKOWKSI ) , 1.0d / MINKOWKSI );
        
        if (dist >= maxdist) return 0.0f;
        
        return (maxdist - dist) / maxdist;
    }
    
    // euclidian 2d distance
    
    private static float getDist(float dx, float dy, float maxdist) {
        
        float dist = (float) Math.sqrt( dx*dx + dy*dy );
        
        if (dist >= maxdist) return 0.0f;
        
        return (maxdist - dist) / maxdist;
    }
    
}
