import doodlepad.*;

public class Sounds {
    public static Sound snd = null;
    public static Oval o;
    public static Rectangle r1;
    public static Rectangle r2;
    
    public static void main(String[] args) {
        r1 = new Rectangle(10, 10, 50, 50);
        r1.setFillColor(0,255,0);
        r1.setMousePressedHandler( Sounds::onOpen );

        o = new Oval(100, 10, 50, 50);
        o.setMousePressedHandler( Sounds::onClick );
        
        r2 = new Rectangle(200, 10, 50, 50);
        r2.setFillColor(255,0,0);
        r2.setMousePressedHandler( Sounds::onClose );

        snd = new Sound();
        snd.setSoundOpenedHandler( Sounds::onSoundOpened );
        snd.setSoundClosedHandler( Sounds::onSoundClosed );
        snd.setSoundStartedHandler( Sounds::onSoundStarted );
        snd.setSoundStoppedHandler( Sounds::onSoundStopped );
    }
    
    public static void onClick(Shape shp, double x, double y, int button) {
        snd.play();
    }
    
    public static void onOpen(Shape shp, double x, double y, int button) {
        try {
            snd.open("LASER.WAV");
        } catch (Exception ex) {
        }
    }

    public static void onClose(Shape shp, double x, double y, int button) {
        if (snd != null) {
            snd.close();
        }
    }
    
    public static void onSoundStarted(Sound snd) {
    	System.out.println("Sound started");
    }
    
    public static void onSoundStopped(Sound snd) {
    	System.out.println("Sound stopped");
    }
    
    public static void onSoundOpened(Sound snd) {
    	System.out.println("Sound opened");
    }
    
    public static void onSoundClosed(Sound snd) {
    	System.out.println("Sound closed");
    }
}