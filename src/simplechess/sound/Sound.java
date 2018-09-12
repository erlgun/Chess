package simplechess.sound;

import java.io.IOException;
import javax.sound.sampled.*;

/**
 * 
 * Loads an audio file and plays it with play() function
 * 
 * http://docs.oracle.com/javase/1.5.0/docs/guide/sound/programmer_guide/contents.html
 * 
 * After a clip has been played the line thread is still open, so a LineListener was
 * necessary. There was also an issue with replaying the clips over time, which was
 * resolved by remaking the clip for each play.
 * In that sense, it would have been okay to just add filename to constructor and let
 * the play() function load the clip each time and play it. I chose not to.
 * 
**/

public class Sound {
    
    private Clip soundClip;
    private LineListener line;
    private boolean isLoaded, isUsed;
    private String filename;
    
    public Sound () { isLoaded = false; }
    
    // loads an audio file from filename and returns true if successful
    // can throw missing file or any other io exception etc.
    // can throw mixer unavailable if soundcard is in-use or unavailable
    // can throw unsupported file exception
    
    public boolean load(String fname) {
        
        filename = fname;
        isUsed = false;
        
        try {
            
            soundClip = AudioSystem.getClip();
            AudioInputStream input = AudioSystem.getAudioInputStream(
                    Thread.currentThread().getContextClassLoader().getResource(fname)
                    );
            
            soundClip.open(input);
            isLoaded = true;
            
            // add event that fires on state change
            // to avoid littering of control threads
            
            line = new LineListener() {

                @Override
                public void update(LineEvent e) {
                    
                    // close line
                    
                    if (e.getType() == LineEvent.Type.STOP)
                        e.getLine().close();
                        
                }
            };
            soundClip.addLineListener(line);
            
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            
            System.err.println(e.getMessage());
            isLoaded = false;
            
        }
        return isLoaded;
        
    }
    
    // plays a preloaded audiosample
    // does nothing if file was never loaded successfully (deliberately)
    
    // due to certain mechanics in JavaSound library, this function reloads
    // the Clip object after each play.. some people also have issues with replaying
    // sounds over time, so the general consencus is remaking the clip each play.
    // this is not an apology, only the result of testing and investigating
    
    public void play() {
        
        if (soundClip != null && isLoaded) {
            
            if (isUsed) {
                load(filename);
                play();
                return;
            }
            
            isUsed = true;
            soundClip.start();
        }
        
    }
}
