/*
 * Pad.java
 * 
 * Author: Mark F. Russo, Ph.D.
 * Copyright (c) 2012-2018 Mark F. Russo
 * 
 * This file is part of DoodlePad
 * 
 * DoodlePad is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * DoodlePad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with DoodlePad.  If not, see <http://www.gnu.org/licenses/>.
 */

package doodlepad;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.ref.WeakReference;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * A class that implements a drawing surface and window for graphical shapes.
 * 
 * @author Mark F. Russo, Ph.D.
 * @version 1.0
 */
public class Pad extends JFrame implements Iterable<Shape>
{
    // Temporary stash of Graphics2D object.
    // May be needed if direct drawing commands added to class
    //private Graphics2D g2D;

	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = 1L;
	
    /**
     * Layers on the Pad
     */
    private List<Layer> layers = null;
    
    /**
     * The current affine transform to be used when drawing all Shapes on this Pad
     */
    //protected AffineTransform transform;
    
    /**
     * Background color used to create the Pad on redraw
     */
    private Color background;
    
    /**
     * If true, all graphical changes trigger a repaint
     */
    private boolean immediateMode = true;
    
    /**
     * Flag that keeps event enabled state
     */
    private boolean eventsEnabled = false;
    
    /**
     * This HashMap should not leak ShapeMouseListener objects
     * because the HashMap is rebuilt every time the paint() method is called.
     */
    private HashMap<Color, ShapeMouseListener> hitHash;
    
    /**
     * In-memory image used to determine target shapes for mouse events.
     */
    private BufferedImage hitImg;

    /**
     * Timer event support
     */
    private Timer timer = null;
    private double tickRate = 60.0;
    
    /**
     * Lists of custom listeners registered to receive events
     */
    private java.util.List<PadTickListener> tickListeners = new ArrayList<>();
    private java.util.List<PadMouseListener> mouseListeners = new ArrayList<>();
    private java.util.List<PadKeyListener> keyListeners = new ArrayList<>();
    
    /**
     * Create a new Pad object, properly invoking on the event dispatch thread.
     */
    public static void create() {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            Pad app = new Pad(600, 600);
            app.setVisible(true);
          }
        });
    }
    
    /**
     * Create a new Pad object, properly invoking on the event dispatch thread.
     * @param width  The width of the window
     * @param height The height of the window
     */
    public static void create(int width, int height) {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            Pad app = new Pad(width, height);
            app.setVisible(true);
          }
        });
    }
    
    /**
     * Create a new Pad object, properly invoking on the event dispatch thread.
     * @param title  The Pad window title
     * @param width  The width of the window
     * @param height The height of the window
     */
    public static void create(String title, int width, int height) {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            Pad app = new Pad(title, width, height);
            app.setVisible(true);
          }
        });
    }
    
    /**
     * Interface used by methods that assign mouse event handlers
     * given a method reference as a parameter.
     */
    public interface PadMouseEventHandler {
        public void f(Pad pad, double x, double y, int button);
    }
    
    /**
     * Interface used by methods that assign key event handlers
     * given a method reference as a parameter.
     */
    public interface PadKeyEventHandler {
        public void f(Pad pad, String keyText, String keyModifiers);
    }
    
    /**
     * Interface used by methods that assign key typed event handlers
     * given a method reference as a parameter.
     */
    public interface PadKeyTypedEventHandler {
        public void f(Pad pad, char keyChar);
    }
    
    /**
     * Interface used by methods that assign timer event handlers
     * given a method reference as a parameter.
     */
    public interface PadTimerEventHandler {
        public void f(Pad pad, long when);
    }
    
    /**
     * Private fields that hold event handlers assigned using method references.
     * See for example setMouseClickedHandler()
     */
    private PadMouseEventHandler mouseClickedHandler = null;
    private PadMouseEventHandler mouseDoubleClickedHandler = null;
    private PadMouseEventHandler mouseDraggedHandler = null;
    private PadMouseEventHandler mouseEnteredHandler = null;
    private PadMouseEventHandler mouseExitedHandler = null;
    private PadMouseEventHandler mouseMovedHandler = null;
    private PadMouseEventHandler mousePressedHandler = null;
    private PadMouseEventHandler mouseReleasedHandler = null;
    private PadKeyEventHandler keyPressedHandler = null;
    private PadKeyEventHandler keyReleasedHandler = null;
    private PadKeyTypedEventHandler keyTypedHandler = null;
    private PadTimerEventHandler tickHandler = null;
    
    // Flag indicating if dragging is underway.
    private boolean dragging = false;

    // Class that manages the listening server socket on a separate thread
    private ServerHandler server;
    
    // Counter to hand out client ids. Guarantees atomic operations to prevent concurrency issues.
    private final AtomicInteger connectionCount = new AtomicInteger();
    
    // Map of a counter to all currently connected clientConnections
    private final ConcurrentHashMap<Integer, ClientConnection> clientConnections = new ConcurrentHashMap<>();
    
    /**
     * Inner class extending JPanel and performing core paint operations
     */
    private final JPanel cvs = new JPanel() {
        
    	/**
    	 * Serialization ID
    	 */
    	private static final long serialVersionUID = 1L;

        /**
         * Override the JPanel's default paint method
         * @param   g   Graphics object on which to draw doodle
         */
        @Override
        public void paintComponent(Graphics g) 
        {
            //System.out.println("paintComponent");
            
            // Execute base class paint() method
            super.paintComponent(g);
            
            // Use Graphics2D api with antialising enabled
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

            // Clear the background to start drawing anew
            g2.setColor(background);
            g2.fillRect(0, 0, getWidth(), getHeight());

            // Get ready to draw to the in-memory image used for hit detection
            Graphics2D gh = (Graphics2D)hitImg.createGraphics();
            gh.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_OFF);
            gh.setColor( Color.white );
            gh.fillRect(0, 0, getWidth(), getHeight());

            // Clear the shape hash
            hitHash.clear();

            // Set the initial transform for the entire Pad
            // !!! This may break printing.
            // !!! It may be necessary to store the transform here
            // !!! and then reapply it before this method completes.
            int count = 0;
            for (int l=0; l<layers.size(); l++) {
                Layer layer = layers.get(l);
                
                AffineTransform saveTransform = g2.getTransform();
                g2.transform( layer.transform );
                gh.transform( layer.transform );
                //g2.setTransform( layer.transform );
                //gh.setTransform( layer.transform );
                
                List<Shape> shapes = layer.getShapes();
                for (int i=0; i<shapes.size(); i++) {
                    // Get the shape
                    Shape s = shapes.get(i);

                    // If visible, add the Shape`s transform and draw the shape on the main Pad
                    if (s.visible) {
                        // Save a copy of the Pad transform
                        AffineTransform g2at = g2.getTransform();

                        // Apply the Shape transform
                        //g2.setTransform( s.transform );
                        g2.transform( s.transform );
                        s.draw(g2);

                        // If the Shape is configured to receive events, draw to the internal hit Graphic2D
                        // image with consecutive fill color and store shape in HashMap
                        if (s.eventsEnabled) {
                            // Save a copy of the Pad transform
                            AffineTransform ghat = gh.getTransform();

                            // Apply the Shape transform
                            //gh.setTransform( s.transform );
                            gh.transform( s.transform );

                            // Draw the shape filled and stroked, with color corresponding to hash key
                            Color key = new Color(count++);
                            s.draw(gh, key);
                            hitHash.put(key, s.shapeListener);

                            // Reset the Pad transform
                            gh.setTransform(ghat);
                        }
                        // Reset the Pad transform
                        g2.setTransform(g2at);
                    }

                    // Draw hit-image to main pad canvas for debugging
                    //g.drawImage(hitImg, 0, 0, this);
                }

                // Restore the original transforms
                g2.setTransform(saveTransform);
                gh.setTransform(saveTransform);
                
            }
            
            // Allow Pad subclasses to do custom drawing
            //g2D = g2;
            //onDraw();
            //g2D = null;
            
            // Clean up
            g2.dispose();
            gh.dispose();
            g.dispose();
        }
    };
    
    /**
     * Inner class to handle key events
     */
    private final KeyListener keyListener = new KeyListener() 
    {
        @Override
        public void keyPressed(KeyEvent e) {
            // Delegate to any subclass overriding methods
            String keyText = KeyEvent.getKeyText(e.getKeyCode());
            String keyModifiers = InputEvent.getModifiersExText(e.getModifiersEx());
            //String keyModifiers = KeyEvent.getKeyModifiersText(e.getModifiers());
            
            onKeyPressed(keyText, keyModifiers);

            // Delegate to any registered PadKeyListeners
            keyListeners.stream().forEach((s) -> {
                s.onKeyPressed(keyText, keyModifiers);
            });
        }
        
        @Override
        public void keyReleased(KeyEvent e) {
            // Delegate to any subclass overriding methods
            String keyText = KeyEvent.getKeyText(e.getKeyCode());
            String keyModifiers = InputEvent.getModifiersExText(e.getModifiersEx());
            //String keyModifiers = KeyEvent.getKeyModifiersText(e.getModifiers());
            
            onKeyReleased(keyText, keyModifiers);

            // Delegate to any registered PadKeyListeners
            keyListeners.stream().forEach((s) -> {
                s.onKeyReleased(keyText, keyModifiers);
            });
        }
        
        @Override
        public void keyTyped(KeyEvent e) {
            // Delegate to any subclass overriding methods
            char keyChar = e.getKeyChar();
            onKeyTyped(keyChar);

            // Delegate to any registered PadKeyListeners
            keyListeners.stream().forEach((s) -> {
                s.onKeyTyped(keyChar);
            });
        }
    };
    
    /**
     * Inner class to handle component listener events
     */
    private final ComponentListener componentListener = new ComponentListener()
    {
        @Override
        public void componentResized(ComponentEvent e)
        {   
            //System.out.println(e.getComponent().getClass().getName() + " --- Resized");
            // Create a new buffered image used for hit detection when the window size changes
            hitImg = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            repaint();
        }

        @Override
        public void componentHidden(ComponentEvent e)
        {
            //System.out.println(e.getComponent().getClass().getName() + " --- Hidden");
        }

        @Override
        public void componentMoved(ComponentEvent e)
        {
            //System.out.println(e.getComponent().getClass().getName() + " --- Moved");
        }

        @Override
        public void componentShown(ComponentEvent e)
        {
            //System.out.println(e.getComponent().getClass().getName() + " --- Shown");
        }  
    };
    
    /**
     * Inner class to handle Pad mouse events
     */
    private final MouseListener mouseListener = new MouseListener() 
    {
        /**
         * Handle the mousePressed event
         * @param e The MouseEvent object that describes the mouse event.
         */
        @Override
        public void mousePressed(MouseEvent e)
        {
            // Get the event point 
            // and perform inverse transforms for the Pad coodinate system
            java.awt.Point pt = e.getPoint();
            double eX = pt.getX();
            double eY = pt.getY();
            int eBut = e.getButton();
            
            // If a Shape is under the mouse, invoke it's ShapeMouseListener.mousePressed methods.
            // Otherwise, invoke all registered PadMouseListeners onMousePressed methods.
            ShapeMouseListener s = getShapeMouseListener(e);

            if (s != null) {
                // If the shift key is not down and the shape is not selected, deselect all.
                if (!e.isShiftDown() && !s.getShape().getSelected()) deselectAll();
                
                // Start the dragging process for all eligable Shapes
                dragging = true;
                for (int l=0; l<layers.size(); l++) {
                    Layer lay = layers.get(l);
                    ArrayList<Shape> shapes = lay.getShapes();
                    
                    for (int i=0; i<shapes.size(); i++) {
//                        Shape ts = shapes.get(i);
//                        if (ts.getSelected() == true) {
//                            ts.startDrag(eX, eY);
//                        }
                        shapes.get(i).startDrag(eX, eY);
                    }

                    // Delegate the mousePressed event
                    s.mousePressed(eX, eY, eBut);
                }

            } else {
                // If the shift key is not down, deselect all.
                if (!e.isShiftDown()) deselectAll();

                // Get the default layer and calculate the inverse transform
                try {
                    Layer lay = layers.get(0);
                    lay.transform.inverseTransform(pt, pt); 
                } catch (NoninvertibleTransformException ex) {
                    System.err.println(ex);
                }

                // Reset the point
                eX = pt.getX();
                eY = pt.getY();

                // Invoke method to override and notify any listeners
                onMousePressed(eX, eY, eBut);
                for (PadMouseListener ml : mouseListeners) {
                    ml.onMousePressed(eX, eY, eBut);
                }
            }
        }

        /**
         * Handle mouseReleased event
         * @param e The MouseEvent object that describes the mouse event.
         */
        @Override
        public void mouseReleased(MouseEvent e)
        {
            // Get the event point
            // and perform inverse transforms for the Pad coodinate system
            java.awt.Point pt = e.getPoint(); 
            double eX = pt.getX();
            double eY = pt.getY();
            int eBut = e.getButton();

            // Stop dragging
            dragging = false;
            for (int l=0; l<layers.size(); l++) {
                Layer lay = layers.get(l);
                ArrayList<Shape> shapes = lay.getShapes();
                
                for (int i=0; i<shapes.size(); i++) {
                    shapes.get(i).stopDrag();
                }
            }
            
            // If a Shape is under the mouse, invoke it's ShapeMouseListener.mouseReleased methods
            // Otherwise, invoke all registered PadMouseListeners onMouseReleased methods
            ShapeMouseListener s = getShapeMouseListener(e);
            if (s != null) {
                s.mouseReleased(eX, eY, eBut);
                
            } else {
                // Invoke method to override and notify any listeners on default layer
                Layer lay = layers.get(0);
                
                try {
                    lay.transform.inverseTransform(pt, pt);
                    eX = pt.getX();
                    eY = pt.getY();
                    
                    onMouseReleased(eX, eY, eBut);
                    for (PadMouseListener ml : mouseListeners) {
                        ml.onMouseReleased(eX, eY, eBut);
                    }
                    
                } catch (NoninvertibleTransformException ex) {
                    System.err.println(ex);
                }
            }
        }

        /**
         * Handle mouseClicked event
         * @param e The MouseEvent object that describes the mouse event.
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            // Get the event point 
            // and perform inverse transforms for the Pad coodinate system
            java.awt.Point pt = e.getPoint();
            double eX = pt.getX();
            double eY = pt.getY();
            int eBut = e.getButton();
            
            // If a Shape is under the mouse, invoke it's ShapeMouseListener.mouseClicked methods
            // Otherwise, invoke all registered PadMouseListeners onMouseClicked methods
            ShapeMouseListener s = getShapeMouseListener(e);
            if (s != null) {
                if (e.getClickCount() == 2) {
                    s.mouseDoubleClicked(eX, eY, eBut);
                } else {
                    s.mouseClicked(eX, eY, eBut);
                }

            } else {
                // Invoke method on default layer and notify any listeners
                try {
                    Layer lay = layers.get(0);
                    lay.transform.inverseTransform(pt, pt);
                    
                    eX = pt.getX();
                    eY = pt.getY();
                    
                    if (e.getClickCount() == 2) {
                        onMouseDoubleClicked(eX, eY, eBut);
                        for (PadMouseListener ml : mouseListeners) {
                            ml.onMouseDoubleClicked(eX, eY, eBut);
                        }                    
                    } else {
                        onMouseClicked(eX, eY, eBut);
                        for (PadMouseListener ml : mouseListeners) {
                            ml.onMouseClicked(eX, eY, eBut);
                        }
                    }
                    
                } catch (NoninvertibleTransformException ex) {
                    System.err.println(ex);
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e)
        {
            // Get the event point
            // and perform inverse transforms for the default layer coodinate system
            java.awt.Point pt = e.getPoint();
            int eBut = e.getButton();

            try {
                Layer lay = layers.get(0);           // Get default layer
                lay.transform.inverseTransform(pt, pt); 
                
                double eX = pt.getX();
                double eY = pt.getY();
            
                // Invoke method to override and notify any listeners
                onMouseEntered(eX, eY, eBut);
                mouseListeners.stream().forEach((ml) -> {
                    ml.onMouseEntered(eX, eY, eBut);
                });
                
            } catch (NoninvertibleTransformException ex) {
                System.err.println(ex);
            }
        }

        @Override
        public void mouseExited(MouseEvent e)
        {
            // Get the event point 
            // and perform inverse transforms for the Pad coodinate system
            java.awt.Point pt = e.getPoint();
            int eBut = e.getButton();

            try {
                Layer lay = layers.get(0);       // Get default layer
                lay.transform.inverseTransform(pt, pt);
                
                double eX = pt.getX();
                double eY = pt.getY();

                // Invoke method to override and notify any listeners
                onMouseExited(eX, eY, eBut);
                for (PadMouseListener ml : mouseListeners) {
                    ml.onMouseExited(eX, eY, eBut);
                }
            } catch (NoninvertibleTransformException ex) {
                System.err.println(ex);
            }
        }
    };

    /**
     * Inner class to handle mouse motion listener events
     */
    private final MouseMotionListener mouseMotionListener = new MouseMotionListener() {
        
        //private ShapeMouseListener lastListener = null;
        WeakReference<ShapeMouseListener> lastListener = new WeakReference<>(null);
        
        /**
         * Handle mouseMoved event
         * @param e The MouseEvent object that describes the mouse event.
         */
        @Override
        public void mouseMoved(MouseEvent e)
        {
            // Get the event point 
            // and perform inverse transforms for the Pad coodinate system
            java.awt.Point pt = e.getPoint();
            double eX = pt.getX();
            double eY = pt.getY();
            int eBut = e.getButton();
                                    
            // Raise mouseEntered/mouseExited/mouseMoved as appropriate
            ShapeMouseListener s = getShapeMouseListener(e);
            ShapeMouseListener ll = lastListener.get();

            // If moving over a shape and the shape is different than the last listener
            // then raise mouseEntered on shape
            if (s != null && ll != s) {
                s.mouseEntered(eX, eY, eBut);
            }
            
            // If s is null and ll is not null
            // then raise mouseEntered on Pad
            if (s == null && ll != s) {
                try {
                    Layer lay = layers.get(0);          // Default Layer
                    Point2D.Double pt2 = new Point2D.Double();
                    lay.transform.inverseTransform(pt, pt2);

                    eX = pt.getX();
                    eY = pt.getY();

                    onMouseEntered(eX, eY, eBut);
                    for (PadMouseListener ml : mouseListeners) {
                        ml.onMouseEntered(eX, eY, eBut);
                    }

                } catch (NoninvertibleTransformException ex) {
                    System.err.println(ex);
                }
            }
            
            // If moving and the last listener is different than the next listener
            // then raise mouseExited
            if (ll != null && ll != s) {
                ll.mouseExited(eX, eY, eBut);
            }
            
            // If last listener shape is null, and the new shape is not null
            // then raise mouseExited on Pad
            if (s != null && ll == null) {
                try {
                    Layer lay = layers.get(0);          // Default Layer
                    Point2D.Double pt2 = new Point2D.Double();
                    lay.transform.inverseTransform(pt, pt2);

                    eX = pt.getX();
                    eY = pt.getY();

                    onMouseExited(eX, eY, eBut);
                    for (PadMouseListener ml : mouseListeners) {
                        ml.onMouseExited(eX, eY, eBut);
                    }

                } catch (NoninvertibleTransformException ex) {
                    System.err.println(ex);
                }
            }

            // If a ShapeMouseListener is under the mouse, invoke it's onMouseMoved methods
            // Otherwise, invoke all registered PadMouseListeners onMouseMoved methods
            if (s != null)          // Raise events on new shape
            {
                s.mouseMoved(eX, eY, eBut);

            // Otherwise, raise onMouseMoved events on Pad
            } else {
            
                try {
                    Layer lay = layers.get(0);          // Default layer
                    Point2D.Double pt1 = new Point2D.Double();
                    lay.transform.inverseTransform(pt, pt1);
                    
                    double eX2 = pt1.getX();
                    double eY2 = pt1.getY();
            
                    onMouseMoved(eX2, eY2, eBut);
                    for (PadMouseListener ml : mouseListeners) {
                        ml.onMouseMoved(eX2, eY2, eBut);
                    }
            
                } catch (NoninvertibleTransformException ex) {
                    System.err.println(ex);
                }
            }

            // Reset last listener
            lastListener = new WeakReference<>(s);
        }

        /**
         * Handle mouseDragged event
         * @param e The MouseEvent object that describes the mouse event.
         */
        @Override
        public void mouseDragged(MouseEvent e)
        {
            // Get the event point 
            // and perform inverse transforms for the Pad coodinate system
            java.awt.Point pt = e.getPoint();
            double eX = pt.getX();
            double eY = pt.getY();
            int eBut = e.getButton();

            // If dragging, move all selected shapes.
            // This also delegates the mouseDragged events
            if (dragging) {
                for (int l=0; l<layers.size(); l++) {
                    Layer lay = layers.get(l);
                    ArrayList<Shape> shapes = lay.getSelectedShapes();
                    for (Shape shape : shapes) {
                        shape.doDrag(eX, eY);
                        shape.shapeListener.mouseDragged(eX, eY, eBut);
                    }
                }
            }

            // Note that dragging is handled outside of the Shape’s mouseDragged event.
            // The reason for this is that it breaks if the mouse moves off the shape being dragged
            // while in the dragging process. This can happen when the mouse is moving quickly.
            // When the mouse moves off the primary shape being dragged, dragging stops.
            // By handling dragging as a separate action, this problem does not occur.
            
            // Get the Shape under this mouse location
            ShapeMouseListener s = getShapeMouseListener(e);
            
            if (s == null) {
                onMouseDragged(eX, eY, eBut);
                for (PadMouseListener ml : mouseListeners) {
                    ml.onMouseDragged(eX, eY, eBut);
                }
            }
            
            // Raise mouseEntered and mouseExited events as appropriate
            ShapeMouseListener ll = lastListener.get();

            // If moving over a shape and the shape is different than the last listener
            // then raise mouseEntered on shape
            if (s != null && ll != s) {
                s.mouseEntered(eX, eY, eBut);
            }
            
            // If s is null and ll is not null
            // then raise mouseEntered on Pad
            if (s == null && ll != s) {
                try {
                    Layer lay = layers.get(0);      // Default Layer
                    Point2D.Double pt2 = new Point2D.Double();
                    lay.transform.inverseTransform(pt, pt2);

                    eX = pt.getX();
                    eY = pt.getY();

                    onMouseEntered(eX, eY, eBut);
                    for (PadMouseListener ml : mouseListeners) {
                        ml.onMouseEntered(eX, eY, eBut);
                    }

                } catch (NoninvertibleTransformException ex) {
                    System.err.println(ex);
                }
            }
            
            // If moving and the last listener is different than the next listener
            // then raise mouseExited
            if (ll != null && ll != s) {
                ll.mouseExited(eX, eY, eBut);
            }
            
            // If last listener shape is null, and the new shape is not null
            // then raise mouseExited on Pad
            if (s != null && ll == null) {
                try {
                    Layer lay = layers.get(0);      // Default Layer
                    Point2D.Double pt2 = new Point2D.Double();
                    lay.transform.inverseTransform(pt, pt2);

                    eX = pt.getX();
                    eY = pt.getY();

                    onMouseExited(eX, eY, eBut);
                    for (PadMouseListener ml : mouseListeners) {
                        ml.onMouseExited(eX, eY, eBut);
                    }

                } catch (NoninvertibleTransformException ex) {
                    System.err.println(ex);
                }
            }

            // Reset last listener
            lastListener = new WeakReference<>(s);
        }
    };
    
    /**
     * Inner class to handle action listener events
     */
    private final ActionListener actionListener = new ActionListener() {
        /**
         * Inform all Shapes that receive events about the tick event.
         * @param e The ActionEvent object describing the action performed.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            long when = e.getWhen();
            
            // Delegate to any subclass overriding methods
            onTick(when);
            
            // Delegate to any registered PadTickListeners
            for (PadTickListener s : tickListeners) {
                s.onTick(when);
            }
        }
    };

    /**
     * All event type constants
     */
    private enum SocketEventType {
        ServerStarted, ServerStopped, ServerInfo, ServerError, 
        ClientOpened, ClientClosed, ClientReceived, ClientInfo, ClientError
    }
    
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    
    /**
     * Inner class that creates and handles server socket on separate thread
     */
    private class ServerHandler implements Runnable {
        
        private ServerSocket socket;
        private volatile boolean continueRunning = false;
        private final int port;
    
        /**
         * Constructor for ServerHandler object
         * @param port The port on which to listen for connection requests
         */
        public ServerHandler(int port)
        {
            this.port = port;
            
            try {
                socket = new ServerSocket(port);
            } catch (IOException e) {
                invokeOnDispatch(SocketEventType.ServerError, "Server could not bind port " + port + ".");
            }
        }

        /**
         * Accessor method that gets the value of flag that indicates if 
         * the server should continue accepting connection requests
         * @return boolean indicating flag value
         */
        private synchronized boolean getContinueRunning() {
            return this.continueRunning;
        }

        /**
         * Mutator method that sets the value of the flag indicating if 
         * the server should continue accepting connection requests.
         * @param val boolean to be assigned to the internal flag
         */
        private synchronized void setContinueRunning(boolean val) {
            this.continueRunning = val;
        }
        
        /**
         * Method to implement the Runnable interface. Will be invoked on separate thread.
         */
        @Override
        public void run()
        {
            // Indicate that the server is listening on the given port
            invokeOnDispatch(SocketEventType.ServerInfo, "Server listening on port " + port);
            
            // Ensure the internal flag is set to true to start
            setContinueRunning(true);

            // Continue accepting connection requests until the internal flag goes false
            while (getContinueRunning())
            {
                try {
                    // Get the next client id
                    int id = connectionCount.incrementAndGet();
                    
                    // Wait for a new connection request
                    ClientConnection ch = new ClientConnection( socket.accept(), id );
                    
                    // Handle the new connection on its own thread
                    Thread worker = new Thread( ch );
                    worker.start();

                    // Track the connection in the internal concurrent HashMap
                    clientConnections.put(id, ch);
                    
                    // Notify events
                    invokeOnDispatch(SocketEventType.ClientOpened, id);
                    invokeOnDispatch(SocketEventType.ServerInfo, "New connection accepted with id " + id);
                    
                } catch (IOException ex) {
                    // Break out of the listening loop on any IOException
                    break;
                }
            }
            
            // Stop listening and close the server socket
            close();
        }

        /**
         * Close and stop the socket server from listening for new connection requests
         */
        public void close()
        {
            // Stop the listening loop
            setContinueRunning(false);
            
            // If the socket is already closed, nothing left to do.
            if (socket == null) return;
            if (socket.isClosed()) return;

            // Try to close the server socket
            try {
                socket.close();
                invokeOnDispatch(SocketEventType.ServerStopped);
            } catch (IOException ex) {
                invokeOnDispatch(SocketEventType.ServerError, "While attempting to close... " + ex);
            }
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    
    /**
     * Inner class that manages client socket connections.
     */
    private class ClientConnection implements Runnable
    {
        private final Socket socket;                        // Client socket
        private BufferedReader in = null;                   // In and out streams
        private PrintWriter out = null;
        private volatile boolean continueRunning = false;   // Internal flag to manage client reading loop
        private final int id;                               // Unique id for this client

        /**
         * Constructor for object that manages client socket connection
         * @param socket    The connected socket
         * @param id        The unique id for this connection object
         */
        public ClientConnection(Socket socket, int id)
        {
            this.socket = socket;
            this.id = id;
            
            // Try to get the input and output streams for reading and writing the socket
            try {
                in = new BufferedReader(new InputStreamReader( socket.getInputStream() ));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException ex) {
                invokeOnDispatch(SocketEventType.ClientError, id, "In or out buffer creation failed: " + ex);
            }
        }
        
        /**
         * Method to implement the Runnable interface. Will be invoked on separate thread.
         */
        @Override
        public void run()
        {
            // Start by setting the loop flag
            setContinueRunning(true);
            
            // Continue the read loop while the flag remains true
            while (getContinueRunning())
            {
                // Wait on the next line to read from the connected socket
                try {
                    String msg = in.readLine();
                    
                    // When read null client has closed
                    if (msg == null) break;
                    
                    // Raise event indicating when data is received
                    invokeOnDispatch(SocketEventType.ClientReceived, id, msg);
                    
                } catch (IOException e) {
                    // Connection was closed
                    break;
                }
            }
            
            // Close local connection
            close();
        }
        
        /**
         * Send a message to the connected socket
         * @param msg The message to send.
         */
        public void send(String msg) 
        {
            // Notify if there is no connected socket
            if (socket == null) {
                invokeOnDispatch(SocketEventType.ClientError, id, "Cannot send. Socket is null.");
                return;
            }
            
            // Notify if the socket is not connected
            if (socket.isConnected() == false) {
                invokeOnDispatch(SocketEventType.ClientError, id, "Cannot send. Socket is not connected.");
                return;
            }
            
            // Notify if the socket is closed
            if (socket.isClosed() == true) {
                invokeOnDispatch(SocketEventType.ClientError, id, "Cannot send. Socket is closed.");
                return;
            }
            
            // Write the message and flush the buffer
            try {
                out.println(msg);
                out.flush();
            } catch (Exception ex) {
                invokeOnDispatch(SocketEventType.ClientError, id, "Problem sending message: " + ex);
            }
        }
        
        /**
         * Close the connected socket
         */
        public void close()
        {
            // Set the flag to false to stop the read loop
            setContinueRunning(false);
            
            // If no socket or already closed then nothing more to do
            if (socket == null) return;
            if (socket.isClosed()) return;
            
            // Close the socket and notify
            try {
                socket.close();
                invokeOnDispatch(SocketEventType.ClientClosed, id);
            } catch (IOException ex) {
                invokeOnDispatch(SocketEventType.ClientError, id, "While closing client: " + ex);
            }
        }
        
        /**
         * Accessor method that gets the read-loop continue flag
         * @return Value of the continue flag
         */
        private synchronized boolean getContinueRunning() {
            return this.continueRunning;
        }
        
        /**
         * Mutator method that sets the read-loop continue flag
         * @param val New boolean value for continue flag
         */
        private synchronized void setContinueRunning(boolean val) {
            this.continueRunning = val;
        }
    }
    
    /**
     * Get or create the Pad singleton object
     * @return The Pad singleton object
     */
    private static volatile Pad padSingleton = null;
    
    /**
     * A static method that creates a new Pad singleton object or returns the current Pad singleton object.
     * Ensures that only one singleton is created by synchronizing execution.
     * @return The current Pad singleton object
     */
    public static Pad getPad()
    {
        if (padSingleton == null) {
            synchronized(Pad.class) {
                if (padSingleton == null) {
                    padSingleton = new Pad("DoodlePad", 600, 600, Color.WHITE);
                }
            }
        }
        padSingleton.setVisible(true);
        return padSingleton;
    }
    
    /**
     * Get the default layer for the Pad.
     * @return Default shape Layer.
     */
    public static Layer getDefaultLayer() {
        return Pad.getPad().getLayer(0);
    }
    
    /**
     * Create and add a new Layer to the Pad`s layers
     * @return idx The new Layer`s index
     */
    public int addLayer() {
        layers.add( new Layer(this) );
        return layers.size() - 1;
    }
    
    /**
     * Remove a Layer given the layer index.
     * Do nothing when index of Layer is 0, the default Layer
     * @param idx Index of Layer to remove.
     */
    public void removeLayer(int idx) {
        if (idx == 0) return;
        try {
            layers.remove(idx); //.clearPad();
        } catch (Exception ex) {
            // ignore errors
        }
    }
    
    /**
     * Get the named Layer from the layers Map
     * @param idx The index of the Layer
     * @return Layer at index idx
     */
    public Layer getLayer(int idx) {
        return layers.get(idx);
    }
    
    /**
     * Simple Pad constructor taking only window dimensions
     * @param width The width of the window
     * @param height The height of the window
     */
    public Pad(int width, int height) {
        this("DoodlePad", width, height, Color.WHITE);
    }
    
    /**
     * Simple Pad constructor taking only title and window dimensions
     * @param title The Pad window title
     * @param width The width of the window
     * @param height The height of the window
     */
    public Pad(String title, int width, int height) {
        this(title, width, height, Color.WHITE);
    }
    
    /**
     * Simple Pad constructor taking only window dimensions
     * @param title The Pad window title
     * @param width The width of the window
     * @param height The height of the window
     * @param singleton True if this Pad object is to be set to the internal singleton
     */
    public Pad(String title, int width, int height, boolean singleton) {
        this(title, width, height, Color.WHITE, singleton);
    }
    
    /**
     * Constructor for objects of class Pad
     * @param title The Pad window title
     * @param width The width of the window
     * @param height The height of the window
     * @param background The default color of the background
     */
    public Pad(String title, int width, int height, Color background)
    {   // Set singleton by default
        this(title, width, height, background, true);
    }
    
    /**
     * Constructor for objects of class Pad
     * @param title The Pad window title
     * @param width The width of the window
     * @param height The height of the window
     * @param background The default color of the background
     * @param singleton True if this Pad object is to be set to the internal singleton
     */
    public Pad(String title, int width, int height, Color background, boolean singleton)
    {
        // Create and configure a new Canvas, which subclasses JPanel
        cvs.setPreferredSize(new java.awt.Dimension(width, height));
        cvs.setDoubleBuffered(true);
        //RepaintManager.currentManager(cvs).setDoubleBufferingEnabled(false);
        
        // Set background color
        this.background = background;
        cvs.setBackground(background);

        // Init the Pad's transform
        //this.transform = new AffineTransform();
        
        // Create the layers List with two default Layers
        layers = new ArrayList<>();
        layers.add(new Layer(this));        // Shapes Layer
        
        // For hit detection. Use an image with alpha for maximum range of values.
        hitImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        hitHash = new HashMap<>();
        
        // Set up event handler inner class objects
        cvs.addMouseListener(mouseListener);
        cvs.addMouseMotionListener(mouseMotionListener);
        addComponentListener(componentListener);
        addKeyListener(keyListener);
        eventsEnabled = true;
        
        setDefaultCloseOperation (EXIT_ON_CLOSE);
        setTickRate(tickRate);      // Start with a default timer setup
        setTitle(title);
        //setResizable(false);
        
        // Build and show
        add(cvs);
        pack();
        setVisible(true);

        // Reset the singleton object when the Pad constructor is invoked, if requested
        if (singleton) {
            if (padSingleton != null) padSingleton.dispose();
            padSingleton = this;
        }
    }
    
    /**
     * Close the Pad as if by clicking the [X] close button.
     */
    public void close() {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
    
    /**
     * Free all resources when Pad object is garbage collected.
     * @throws java.lang.Throwable May throw exceptions 
     */
    @Override
    protected void finalize() throws Throwable {
        try {
            stopTimer();                // Stop timer, in case it is running
            closeAllConnections();      // Close all client socket connections, if any
            stopListening();            // Close listening socket and terminate listening loop 
        } finally {
            super.finalize();           // Always call base class implementation
        }
    }
    
    /**
     * Return the current width of the drawing area
     * @return Width of the drawing area
     */
    public int getPadWidth() {
        return cvs.getWidth();
    }
    
    /**
     * Return the current height of the drawing area
     * @return Height of the drawing area
     */
    public int getPadHeight() {
        return cvs.getHeight();
    }
    
    /**
     * Set the background color for the Pad 
     * @param red The background color red component
     * @param green The background color green component
     * @param blue  The background color blue component
     */
    public void setBackground(double red, double green, double blue) {
        int r = (int)Math.round(Util.constrain(red, 0, 255));
        int g = (int)Math.round(Util.constrain(green, 0, 255));
        int b = (int)Math.round(Util.constrain(blue, 0, 255));
        this.background = new Color(r, g, b);
        this.repaint();
    }
    
    /**
     * Set the background color for the Pad to a gray value
     * @param gray The background gray scale
     */
    public void setBackground(double gray) {
        this.setBackground(gray, gray, gray);
    }
    
    /**
     * Return the current background color used to clear the Pad
     * @return Color object used for the Pad background
     */
    @Override
    public Color getBackground() {
        return this.background;
    }
    
    /**
     * Return an ArrayList of Shape objects currently managed by the Pad instance
     * @return An ArrayList&lt;Shape&gt; of all Shapes being managed
     */
    public ArrayList<Shape> getShapes() {
        ArrayList<Shape> allShapes = new ArrayList<>();
        for (int l=0; l<layers.size(); l++) {
            Layer ll = layers.get(l);
            ArrayList<Shape> ss = ll.getShapes();
            allShapes.addAll( ss );
        }
        return allShapes;
        //return (ArrayList<Shape>)this.shapes;
    }
    
    /**
     * Return an iterator for loop over Shapes
     * @return Iterator for Shapes
     */
    @Override
    public Iterator<Shape> iterator() {
        ArrayList<Shape> all = this.getShapes();
        return all.iterator();
        //return shapes.iterator();
    }
    
    /**
     * Override Component repaint method 
     */
    @Override
    public void repaint() {
        if (immediateMode == true) {
            cvs.repaint();
        }
    }
    
    /**
     * Force a redraw of all Shapes
     */
    public void redraw() {
        cvs.repaint();
    }
    
    /**
     * Add a shape to the shapes list
     * @param s The Shape object to add to the Pad
     */
    public void addShape(Shape s)
    {
        layers.get(0).addShape(s);
    }
    
    /**
     * Remove a shape from the shapes list
     * @param s The Shape to remove
     */
    public void removeShape(Shape s)
    {
        layers.get(0).removeShape(s);
    }
    
    /**
     * Remove all shapes from the Pad
     */
    public void clear() {
        for (int l=0; l<layers.size(); l++) {
            layers.get(l).clear();
        }
        //shapes.clear();
        //repaint();
    }
    
//    /**
//     * Make sure the Pad is created and visible.
//     * @param vis A boolean indicating if the Pad should be visible
//     */
//    @Override
//    public void setVisible(boolean vis) {
//        Pad p = Pad.getPad();
//        Window.getWindow().setVisible(vis);
//    }

    /**
     * Enable or disable add Pad events.
     * @param enabled True to enable. False to disable
     */
    public void setEventsEnabled( boolean enabled )
    {
        if (enabled == false && eventsEnabled == true) {
            cvs.removeMouseListener(mouseListener);
            cvs.removeMouseMotionListener(mouseMotionListener);
            removeComponentListener(componentListener);
            removeKeyListener(keyListener);
            eventsEnabled = false;
        } else if (enabled == true && eventsEnabled == false) {
            cvs.addMouseListener(mouseListener);
            cvs.addMouseMotionListener(mouseMotionListener);
            addComponentListener(componentListener);
            addKeyListener(keyListener);
            eventsEnabled = true;
        }
    }
        
    /**
     * Set the immediateMode flag
     * @param mode true or false
     */
    public void setImmediateMode(boolean mode) {
        immediateMode = mode;
        redraw();
    }
    
    /**
     * Return the current value of the immediateMode flag
     * @return Gets the Pad's immediate mode state. If true, all graphical updates will trigger and redraw. If false, not draw will happen until redraw() is called explicitly.
     */
    public boolean getImmediateMode() {
        return immediateMode;
    }
    
    /**
     * Return the milliseconds since midnight, January 1, 1970 UTC.
     * @return Milliseconds since midnight, January 1, 1970 UTC.
     */
    public long getTimeStamp() {
        return System.currentTimeMillis();
    }
    
    /**
     * Deselect all Shapes
     */
    void deselectAll() {
        for (int l=0; l<layers.size(); l++) {
            layers.get(l).deselectAll();
        }
//        for (int i=0; i<shapes.size(); i++) {
//            shapes.get(i).setSelected(false);
//        }
    }
    
    /**
     * Set the rate at which to fire onTick events
     * @param tps Ticks per second
     */
    public final void setTickRate(double tps) {
        // Check for validity of tick rate
        if (tps < 0.0) {
            throw new IllegalArgumentException("tick rate cannot be less than 0");
        }
        
        // Set up new timer
        tickRate = tps;
        int delay = (int)(1000.0/tps);
        if (timer != null) timer.stop();
        timer = new Timer(delay, actionListener);
        timer.stop();
    }

    /**
     * Return the current rate at which the timer ticks
     * @return A double indicating ticks per second.
     */
    final public double getTickRate() {
        return tickRate;
    }
    
    /**
     * Add object to the list if items that are notified on Pad's timer tick action.
     * @param o An object that implements the ActionListener interface.
     */
    public void addTickListener(PadTickListener o) {
        tickListeners.add(o);
    }
    
    /**
     * Remove object from Pad's timer tick action listener list.
     * @param o The ActionListener object to be removed.
     */
    public void removeTickListener(PadTickListener o) {
        tickListeners.remove(o);
    }

    /**
     * Start the timer firing onTick events.
     */
    public void startTimer() {
        // If invalid tickRate, fix.
        if (tickRate < 1.0) tickRate = 1.0;
        
        // If no timer, create one.
        if (timer == null) {
            setTickRate(tickRate);
        }
        
        // Make sure running
        timer.start();
    }
    
    /**
     * Stop timer
     */
    public void stopTimer() {
        if (timer != null) timer.stop();
    }
    
    /**
     * Toggle the state of the timer. Started/Stopped.
     */
    public void toggleTimer() {
        if (this.isRunning()) {
            this.stopTimer();
        } else {
            this.startTimer();
        }
    }

    /**
     * Determine if the timer is running
     * @return True or false indicating if the timer is running.
     */
    public boolean isRunning() {
        if (timer == null) {
            return false;
        } else {
            return timer.isRunning();
        }
    }
    
    /**
     * Add object to the list of items that are notified on Pad's mouse events.
     * @param o The PadMouseListener object to be added.
     */
    public void addMouseListener(PadMouseListener o) {
        mouseListeners.add(o);
    }
    
    /**
     * Remove object from Pad's mouse listener list.
     * @param o The PadMouseListener object to be removed.
     */
    public void removeMouseListener(PadMouseListener o) {
        mouseListeners.remove(o);
    }
    
    /**
     * Add object to the list of items that are notified on Pad's key events.
     * @param   o The PadKeyListener object to be added.
     */
    public void addKeyListener(PadKeyListener o) {
        keyListeners.add(o);
    }
    
    /**
     * Remove object from Pad's key listener list.
     * @param o The PadKeyListener object to be removed.
     */
    public void removeKeyListener(PadKeyListener o) {
        keyListeners.remove(o);
    }
    
    /**
     * Assign an onMouseClicked event handler using a method reference.
     * @param handler Method reference to an event handler
     */
    public void setMouseClickedHandler( PadMouseEventHandler handler ) {
        this.mouseClickedHandler = handler;
    }
    
    /**
     * Assign an onMouseDoubleClicked event handler using a method reference.
     * @param handler Method reference to an event handler
     */
    public void setMouseDoubleClickedHandler( PadMouseEventHandler handler ) {
        this.mouseDoubleClickedHandler = handler;
    }
    
    /**
     * Assign an onMouseDragged event handler using a method reference.
     * @param handler Method reference to an event handler
     */
    public void setMouseDraggedHandler( PadMouseEventHandler handler ) {
        this.mouseDraggedHandler = handler;
    }
    
    /**
     * Assign an onMouseEntered event handler using a method reference.
     * @param handler Method reference to an event handler
     */
    public void setMouseEnteredHandler( PadMouseEventHandler handler ) {
        this.mouseEnteredHandler = handler;
    }
    
    /**
     * Assign an onMouseExited event handler using a method reference.
     * @param handler Method reference to an event handler
     */
    public void setMouseExitedHandler( PadMouseEventHandler handler ) {
        this.mouseExitedHandler = handler;
    }
    
    /**
     * Assign an onMouseMoved event handler using a method reference.
     * @param handler Method reference to an event handler
     */
    public void setMouseMovedHandler( PadMouseEventHandler handler ) {
        this.mouseMovedHandler = handler;
    }
    
    /**
     * Assign an onMousePressed event handler using a method reference.
     * @param handler Method reference to an event handler
     */
    public void setMousePressedHandler( PadMouseEventHandler handler ) {
        this.mousePressedHandler = handler;
    }
    
    /**
     * Assign an onMouseReleased event handler using a method reference.
     * @param handler Method reference to an event handler
     */
    public void setMouseReleasedHandler( PadMouseEventHandler handler ) {
        this.mouseReleasedHandler = handler;
    }

    /**
     * Assign an onKeyPressed event handler using a method reference.
     * @param handler Method reference to an event handler
     */
    public void setKeyPressedHandler( PadKeyEventHandler handler ) {
        this.keyPressedHandler = handler;
    }

    /**
     * Assign an onKeyReleased event handler using a method reference.
     * @param handler Method reference to an event handler
     */
    public void setKeyReleasedHandler( PadKeyEventHandler handler ) {
        this.keyReleasedHandler = handler;
    }

    /**
     * Assign an onKeyTyped event handler using a method reference.
     * @param handler Method reference to an event handler
     */
    public void setKeyTypedHandler( PadKeyTypedEventHandler handler ) {
        this.keyTypedHandler = handler;
    }
    
    /**
     * Assign an onTick event handler using a method reference.
     * @param handler Method reference to an event handler
     */
    public void setTickHandler( PadTimerEventHandler handler ) {
        this.tickHandler = handler;
    }
    
    /**
     * Move shape to the front of diagram.
     * Assumes Shape is on default layer 0.
     * @param s The Shape to bring to the front of the display list.
     */
    public void toFront(Shape s) {
        this.getLayer(0).toFront(s);
    }
    
    /**
     * Move shape to the back of diagram
     * @param s The Shape to move to the back of the display list.
     */
    public void toBack(Shape s)
    {
        this.getLayer(0).toBack(s);
    }
    
    /**
     * Add a rotation angle to Pad transform.
     * @param angle Adds the rotation angle to the current transform (degrees)
     */
    public void rotate(double angle) {
        this.getLayer(0).rotate(angle*Math.PI/180.0);
    }
    
    /**
     * Add a rotation angle to Pad transform.
     * @param angle Adds the rotation angle to the current transform (degrees)
     * @param cx x-coordinate of point about which rotation occurs
     * @param cy y-coordinate of point about which rotation occurs
     */
    public void rotate(double angle, double cx, double cy) {
    	Layer lyr = this.getLayer(0);
        lyr.translate(-cx, -cy);
        lyr.rotate(angle*Math.PI/180.0);
    	lyr.translate( cx,  cy);
        repaint();
    }
    
    /**
     * Add a translate to Pad transform.
     * @param deltaX Translate shape in the x-direction by deltaX
     * @param deltaY Translate shape in the y-direction by deltaY
     */
    public void translate(double deltaX, double deltaY) {
        this.getLayer(0).translate(deltaX, deltaY);
    }
    
    /**
     * Add a scale factor to Pad transform.
     * @param factor Scale the shape by a scale factor
     */
    public void scale(double factor) {
        this.getLayer(0).scale(factor);
    }

    /**
     * Add a scale factor to Pad transform.
     * @param factor Scale the shape by a scale factor
     * @param cx x-coordinate of point about which scaling occurs
     * @param cy y-coordinate of point about which scaling occurs
     */
    public void scale(double factor, double cx, double cy) {
    	Layer lyr = this.getLayer(0);
    	lyr.translate(-cx, -cy);
        lyr.scale(factor, factor);
    	lyr.translate( cx,  cy);
        repaint();
    }

    /**
     * Add a scale factor to Pad transform.
     * @param xFactor Scale the shape in the x-direction by a xFactor
     * @param yFactor Scale the shape in the y-direction by a yFactor

     */
    public void scale(double xFactor, double yFactor) {
        this.getLayer(0).scale(xFactor, yFactor);
    }
    
    /**
     * Add a scale factor to Pad transform.
     * @param xFactor Scale the shape in the x-direction by a xFactor
     * @param yFactor Scale the shape in the y-direction by a yFactor
     * @param cx x-coordinate of point about which scaling occurs
     * @param cy y-coordinate of point about which scaling occurs
     */
    public void scale(double xFactor, double yFactor, double cx, double cy) {
    	Layer lyr = this.getLayer(0);
    	lyr.translate(-cx, -cy);
        lyr.scale(xFactor, yFactor);
    	lyr.translate( cx,  cy);
        repaint();
    }
    
    /**
     * Resets the Pad to have no transformation.
     */
    public void reset() {
        this.getLayer(0).reset();
    }
    
    /**
     * Clone the Pad's default Layer AffineTransform.
     * @return The cloned AffineTransform.
     */
    public AffineTransform getTransform() {
        return this.getLayer(0).getTransform();
    }
    
    /**
     * Set a new AffineTransform for the Pad's default Layer.
     * @param transform The new transform.
     */
    public void setTransform(AffineTransform transform) {
        this.getLayer(0).setTransform(transform);
    }
    
    /**
     * Convert transformed Pad coordinates to window coordinates 
     * returned as a new Point2D object.
     * This method is useful for converting transformed coordinates received 
     * from a Pad mouse event method back to original window coordinates.
     * @param x Transformed x-coordinate
     * @param y Transformed y-coordinate
     * @return A Point2D object containing the transformed coordinates
     */
    public double[] toWindowCoords(double x, double y) {
        double[] pts = new double[] {x, y};
        layers.get(0).transform.transform(pts, 0, pts, 0, 1);
        return pts;
        //Point pt = toWindowCoords( new Point(x, y));
        //return new double[] { pt.getX(), pt.getY() };
    }
    
    /**
     * Convert transformed Pad coordinates in a Point2D object
     * to window coordinates returned as a new Point2D object.
     * This method is useful for converting transformed coordinates received 
     * from a Pad mouse event method back to original window coordinates.
     * @param pt A Point object 
     * @return A new Point object containing the transformed coordinates
     */
    public Point toWindowCoords(Point pt) {
        Point2D pt1 = new Point2D.Double(pt.getX(), pt.getY());
        layers.get(0).transform.transform(pt1, pt1);
        return new Point(pt1.getX(), pt1.getY());
    }
    
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // Methods in support of network communications
    
    /**
     * Start listening for new network connection requests.
     * @param port The port on which to listen.
     */
    public void startListening( int port )
    {
        // As a precaution, attempt to closeConnection everything.
        stopListening();
        
        // ServerHandler manages server socket on separate thread
        server = new ServerHandler( port );
        Thread worker = new Thread( server );
        worker.start();
        invokeOnDispatch(SocketEventType.ServerStarted);
    }
    
    /**
     * Stop listening for new network connection requests.
     */
    public void stopListening()
    {
        closeAllConnections();
        
        // Close socket server
        if (server != null) {
            server.close();
            server = null;
        }
    }
    
    /**
     * Open a connection with a listening socket server.
     * @param host  The host name to connect with.
     * @param port  The port number on the host through which to connect.
     * @return id of the open socket
     */
    public int openConnection(String host, int port) {
        Socket socket;
        try {
            socket = new Socket(host, port);
        } catch (UnknownHostException ex) {
            invokeOnDispatch(SocketEventType.ClientError, "Unknown host: " + host + ". " + ex);
            return -1;
        } catch (IOException ex) {
            invokeOnDispatch(SocketEventType.ClientError, "No I/O: " + ex);
            return -1;
        }
        
        int id = connectionCount.incrementAndGet();
        ClientConnection ch = new ClientConnection(socket, id);
        clientConnections.put(id, ch);
        
        Thread worker = new Thread( ch );
        worker.start();
        invokeOnDispatch(SocketEventType.ClientOpened, id);
        
        return id;
    }

    /**
     * Returns true of a connection exists with the id
     * @param id    A unique integer id identifying the connection.
     * @return A boolean indicating whether or not the connection exists.
     */
    public boolean connectionExists(int id) {
        return clientConnections.containsKey(id);
    }
    
    /**
     * Return the set of current connection Ids.
     * @return A Set&lt;Integer&gt; holding all current connection Ids.
     */
    public Set<Integer> getConnectionIds() {
        return clientConnections.keySet();
    }
    
    /**
     * Send a message to the client given a connection id.
     * @param id    The id of the connection.
     * @param msg   The message to send.
     */
    public void send(int id, String msg) 
    {
        try {
            ClientConnection ch = clientConnections.get(id);
            if (ch != null) {
                ch.send(msg);
            } else {
                invokeOnDispatch(SocketEventType.ClientError, id, "No client with id " + id + ".");
            }
        } catch (Exception ex) {
            invokeOnDispatch(SocketEventType.ClientError, id, "Error sending on client " + id + ". " + ex);
        }
    }
    
    /**
     * Broadcast a message to all open network connections.
     * @param msg The message to broadcast.
     */
    public void broadcast(String msg) 
    {
        if (clientConnections != null && clientConnections.size() > 0)
        {
            Iterator<Integer> it = clientConnections.keySet().iterator();
            while (it.hasNext())
            {
                Integer id = it.next();
                send(id, msg);
            }
        }
    }
    
    /**
     * Close the identified network connection.
     * @param id ID of the connection to close.
     */
    public void closeConnection(int id)
    {
        try {
            ClientConnection ch = clientConnections.get(id);
            if (ch != null) {
                ch.close();
            } else {
                invokeOnDispatch(SocketEventType.ClientError, id, "No client with id " + id + ".");
            }
        } catch (Exception ex) {
            invokeOnDispatch(SocketEventType.ClientError, "Error sending on client " + id + ". " + ex);
        }
    }
    
    /**
     * Close all open network connections.
     */
    public void closeAllConnections() 
    {
        if (clientConnections != null && clientConnections.size() > 0)
        {
            invokeOnDispatch(SocketEventType.ClientInfo, "Closing " + clientConnections.size() + " client(s).");
            
            // Close all open ClientHandlers
            Iterator<Integer> it = clientConnections.keySet().iterator();
            while (it.hasNext())
            {
                Integer id = it.next();
                closeConnection(id);
                it.remove();
            }
        }
    }

    /**
     * Return the number of open network connections.
     * @return Number of open connections.
     */
    public int getNumConnections()
    {
        return clientConnections.size();
    }

    /**
     * Collect and return all IPv4 addresses for this computer
     * @return List&lt;String&gt; of IPv4 addresses
     * @throws java.net.SocketException May throw SocketExceptions
     */
    public List<String> getIPv4Addresses() throws SocketException
    {
        List<String> IPs = new ArrayList<>();
        
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements())
        {
            NetworkInterface niface = interfaces.nextElement();

            if (!niface.isUp() || niface.isLoopback() || niface.isVirtual()) continue;
            
            Enumeration<InetAddress> addresses = niface.getInetAddresses();
            while (addresses.hasMoreElements())
            {
                InetAddress addr = addresses.nextElement();
                if (addr.isLoopbackAddress() || !(addr instanceof Inet4Address)) continue;
                IPs.add( addr.getHostAddress() );
            }
        }
        
        return IPs;
    }
    
    /**
     * Collect and return all IPv6 addresses for this computer
     * @return List&lt;String&gt; of IPv6 addresses
     * @throws java.net.SocketException May throw SocketExceptions
     */
    public List<String> getIPv6Addresses() throws SocketException
    {
        List<String> IPs = new ArrayList<>();
        
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements())
        {
            NetworkInterface niface = interfaces.nextElement();

            if (!niface.isUp() || niface.isLoopback() || niface.isVirtual()) continue;
            
            Enumeration<InetAddress> addresses = niface.getInetAddresses();
            while (addresses.hasMoreElements())
            {
                InetAddress addr = addresses.nextElement();
                if (addr.isLoopbackAddress() || !(addr instanceof Inet6Address)) continue;
                IPs.add( addr.getHostAddress() );
            }
        }
        
        return IPs;
    }
    
    /**
     * Utility method that invokes the proper event and method on the dispatch thread
     */
    private void invokeOnDispatch(final SocketEventType typ) {
        invokeOnDispatch(typ, 0, "");
    }
    
    /**
     * Utility method that invokes the proper event and method on the dispatch thread
     */
    private void invokeOnDispatch(final SocketEventType typ, final int id) {
        invokeOnDispatch(typ, id, "");
    }
    
    /**
     * Utility method that invokes the proper event and method on the dispatch thread
     */
    private void invokeOnDispatch(final SocketEventType typ, final String msg) {
        invokeOnDispatch(typ, 0, msg);
    }
    
    /**
     * Utility method that invokes the proper event and method on the dispatch thread
     */
    private void invokeOnDispatch(final SocketEventType typ, final int id, final String msg)
    {
        if (!SwingUtilities.isEventDispatchThread())
        {
            SwingUtilities.invokeLater(new Runnable() 
            {
                @Override
                public void run() {
                    raiseEvent(typ, id, msg);
                }
            });
        } else {
            raiseEvent(typ, id, msg);
        }
    }
    
    /**
     * Interface for the dispatch method with a signature having no parametes.
     */
    public interface DispatchMethodNoParameters {
        public void f();
    }
    
    /**
     * Invoke a method on the GUI event dispatch thread
     * @param meth A method implementing the DispatchMethodNoParameters interface, which is a method taking nor parameters.
     */
    public void invokeOnDispatch( DispatchMethodNoParameters meth ) {
        SwingUtilities.invokeLater(meth::f);
    }
    
//    public void invokeOnDispatch( DispatchMethodNoParameters meth ) {
//        SwingUtilities.invokeLater(new Runnable() 
//        {
//            @Override
//            public void run() {
//                meth.f();
//            }
//        });
//    }
    
    /**
     * Utility method for invokeOnDispatch()
     */
    private void raiseEvent(SocketEventType typ, int id, String msg)
    {
        String longMsg;
        
        switch (typ) {
        case ServerStarted:
            onServerStarted();
            break;
            
        case ServerStopped:
            onServerStopped();
            break;
            
        case ServerInfo:
            onServerInfo(msg);
            break;
            
        case ServerError:
            longMsg = "Server error: " + msg;
            System.out.println(longMsg);
            onServerError(msg);
            break;
            
        case ClientOpened:
            onClientOpened(id);
            break;
            
        case ClientClosed:
            clientConnections.remove(id);
            onClientClosed(id);
            break;
            
        case ClientReceived:
            onClientReceived(id, msg);
            break;
            
        case ClientInfo:
            onClientInfo(id, msg);
            break;
            
        case ClientError:
            longMsg = "Client " + id + " error: " + msg;
            System.out.println(longMsg);
            onClientError(id, msg);
            break;
        }
    }
    
    /**
     * Invoked when the listening server starts
     */
    public void onServerStarted() {
        // Override to implement
    }
    
    /**
     * Invoked when the listening server stops and all connections are closed
     */
    public void onServerStopped() {
        // Override to implement
    }
    
    /**
     * Invoked when the listening server has some information to share
     * @param msg A message containing information to share
     */
    public void onServerInfo(String msg) {
        // Override to implement
    }
    
    /**
     * Invoked when the listening server has an error
     * @param msg A message containing the error message
     */
    public void onServerError(String msg) {
        // Override to implement
    }
    
    /**
     * Invoked when a new client connection opens
     * @param id Unique id for the client connection
     */
    public void onClientOpened(int id) {
        // Override to implement
    }
    
    /**
     * Invoked when a client connection is closed
     * @param id Unique of the client id that is closed
     */
    public void onClientClosed(int id) {
        // Override to implement
    }
    
    /**
     * Invoked when a connected client socket receives a message
     * @param id    Unique client id
     * @param msg   Message String received
     */
    public void onClientReceived(int id, String msg) {
        // Override to implement
    }
    
    /**
     * Invoked when a connected client socket has some information to share
     * @param id    Unique client id
     * @param msg   Message from the client
     */
    public void onClientInfo(int id, String msg) {
        // Override to implement
    }
    
    /**
     * Invoked when a connected client socket has has error
     * @param id    Unique client id
     * @param msg   Error message from the client
     */
    public void onClientError(int id, String msg) {
        // Override to implement
    }
    
    /**
     * A private utility method that determines if an event should be delegated to a ShapeMouseListener
     */
    private ShapeMouseListener getShapeMouseListener(MouseEvent e)
    {
        try {
            // Get the shape key, which is the color
            java.awt.Point p = e.getPoint();
            int i = hitImg.getRGB(p.x, p.y);
            Color key = new Color(i, true);
            
            // Check to see if the color has an associated Shape
            if ( hitHash.containsKey(key) ) {
                return hitHash.get(key);
            }
        } catch (Exception ex) {
            // Ignore errors. Just return null.
            return null;
        }
        
        // No Shape at event location.
        return null;
    }
    
// Beginnings of a set of functions that draw directly on Pad 
//    public void line(int x1, int y1, int x2, int y2) {
//        if (g2D != null) g2D.drawLine(x1, y1, x2, y2);
//    }
//    
//    public void setStrokeColor(int r, int g, int b) {
//        if (g2D != null) g2D.setColor( new Color(r, g, b) ); 
//    }
    
    /**
     * A method that can be overridden to handle key typed events
     * @param keyChar The char of the key typed
     */
    public void onKeyTyped(char keyChar) //KeyEvent e)
    {
        // Also, override to implement.
        if (keyTypedHandler != null) {
            keyTypedHandler.f(this, keyChar);
        }
    }
    
    /**
     * A method that can be overridden to handle key pressed events
     * @param keyText       Text of the key pressed
     * @param keyModifiers  Special keys pressed
     */
    public void onKeyPressed(String keyText, String keyModifiers) //KeyEvent e)
    {
        // Also, override to implement.
        if (keyPressedHandler != null) {
            keyPressedHandler.f(this, keyText, keyModifiers);
        }
    }
    
    /**
     * A method that can be overridden to handle key released events
     * @param keyText       Text of the key pressed
     * @param keyModifiers  Special keys pressed
     */
    public void onKeyReleased(String keyText, String keyModifiers) //KeyEvent e)
    {
        // Also, override to implement.
        if (keyReleasedHandler != null) {
            keyReleasedHandler.f(this, keyText, keyModifiers);
        }
    }
    
    /**
     * A method that can be overridden to handle mouse pressed events
     * @param x x-location 
     * @param y y-location
     * @param button The button pressed
     */
    public void onMousePressed(double x, double y, int button) {
        // Also, override to implement.
        if (mousePressedHandler != null) {
            mousePressedHandler.f(this, x, y, button);
        }
    }
    
    /**
     * A method that can be overridden to handle mouse released events
     * @param x x-location 
     * @param y y-location
     * @param button The button pressed
     */
    public void onMouseReleased(double x, double y, int button) {
        // Also, override to implement.
        if (mouseReleasedHandler != null) {
            mouseReleasedHandler.f(this, x, y, button);
        }
    }
    
    /**
     * A method that can be overridden to handle mouse clicked events.
     * A mouse clicked event is composed of a mouse pressed event followed by a mouse released event at the same mouse location.
     * @param x x-location 
     * @param y y-location
     * @param button The button pressed
     */
    public void onMouseClicked(double x, double y, int button) {
        // Also, override to implement.
        if (mouseClickedHandler != null) {
            mouseClickedHandler.f(this, x, y, button);
        }
    }

    /**
     * A method that can be overridden to handle mouse double-clicked events.
     * @param x x-location 
     * @param y y-location
     * @param button The button pressed
     */
    public void onMouseDoubleClicked(double x, double y, int button) {
        // Also, override to implement.
        if (mouseDoubleClickedHandler != null) {
            mouseDoubleClickedHandler.f(this, x, y, button);
        }
    }
    
    /**
     * A method that can be overridden to handle mouse entered events
     * @param x x-location 
     * @param y y-location
     * @param button The button pressed
     */
    public void onMouseEntered(double x, double y, int button) {
        // Also, override to implement.
        if (mouseEnteredHandler != null) {
            mouseEnteredHandler.f(this, x, y, button);
        }
    }
    
    /**
     * A method that can be overridden to handle mouse exited events
     * @param x x-location 
     * @param y y-location
     * @param button The button pressed
     */
    public void onMouseExited(double x, double y, int button) {
        // Also, override to implement.
        if (mouseExitedHandler != null) {
            mouseExitedHandler.f(this, x, y, button);
        }
    }
    
    /**
     * A method that can be overridden to handle mouse moved events
     * @param x x-location 
     * @param y y-location
     * @param button The button pressed
     */
    public void onMouseMoved(double x, double y, int button) {
        // Also, override to implement.
        if (mouseMovedHandler != null) {
            mouseMovedHandler.f(this, x, y, button);
        }
    }
    
    /**
     * A method that can be overridden to handle mouse dragged events.
     * A mouse dragged event is the same as a mouse moved event while the mouse is pressed.
     * @param x x-location 
     * @param y y-location
     * @param button The button pressed
     */
    public void onMouseDragged(double x, double y, int button) {
        // Also, override to implement.
        if (mouseDraggedHandler != null) {
            mouseDraggedHandler.f(this, x, y, button);
        }
    }
    
    /**
     * A method that can be overridden to handle the Pad timer`s tick event.
     * The timer tick rate is set with the setTickRate() method. 
     * The timer is started by invoking the startTimer() method.
     * The timer is stopped by invoking the stopTimer() method.
     * @param when The difference in milliseconds between the timestamp of when this event occurred and midnight, January 1, 1970 UTC.
     */
    public void onTick(long when) {
        //System.out.println(System.nanoTime());
        // Also, override to implement.
        if (tickHandler != null) {
            tickHandler.f(this, when);
        }
    }
}
