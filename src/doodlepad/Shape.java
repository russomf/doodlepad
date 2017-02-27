/*
 * Shape.java
 * 
 * Author: Mark F. Russo, Ph.D.
 * Copyright (c) 2012-2016 Mark F. Russo
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

/**
 * Abstract base class for all graphical shape objects
 * 
 * @author Mark F. Russo, Ph.D.
 * @version 1.0
 */
public abstract class Shape
{
    /**
     * The Layer on which this Shape is to be drawn
     */
    protected Layer layer = null;
    
    /**
     * The x-coordinate of the upper left-hand coordinate of the Shape’s bounding box.
     */
    protected double x;
    
    /**
     * The y-coordinate of the upper left-hand coordinate of the Shape’s bounding box.
     */
    protected double y;
    
    /**
     * The width of the Shape’s bounding box.
     */
    protected double width;
    
    /**
     * The height of the Shape’s bounding box.
     */
    protected double height;
    
    /**
     * The current affine transform to be used when drawing this Shape
     */
    protected AffineTransform transform;
    
    /**
     * The fill color to be used to draw this Shape.
     */
    protected Color fillColor;
    
    /**
     * The stroke color to be used to draw this Shape.
     */
    protected Color strokeColor;
    
    /**
     * The width of the stroke used to draw this Shape.
     */
    protected double strokeWidth;
    
    /**
     * Flag to determine if Shape should be drawn.
     */
    protected boolean visible = true;
    
    /**
     * Flag to determine if Shape should be filled.
     */
    protected boolean filled = true;
    
    /**
     * Flag to determine if Shape should be stroked.
     */
    protected boolean stroked = true;
    
    /**
     * Flag indicating if this Shape should receive events
     */
    protected boolean eventsEnabled = true;
    
    /**
     * Flag to indicate if this Shape is currently selected
     */
    protected boolean selected = false;
    
    /**
     * Flag to indicate if this Shape can be selected.
     */
    protected boolean selectable = false;
    
    /**
     * Initial mouse location when mouse pressed initiates dragging
     */
    private double dragDeltaX = 0.0;
    private double dragDeltaY = 0.0;
    
    /**
     * Boolean indicating if this shape can be dragged.
     */
    protected boolean draggable = false;
    
    // Removed traditional Java event handling method
//    /**
//     * List of custom listeners registered to receive events
//     */
//    private java.util.List<ShapeMouseListener> mouseListeners = new ArrayList<>();
    
    /**
     * Interface used by methods that assign mouse event handlers
     * given a method reference as a parameter.
     */
    public interface ShapeMouseEventHandler {
        public void f(Shape shp, double x, double y, int button);
    }
    
    /**
     * Interface used by methods that assign selection event handlers
     * given a method reference as a parameter.
     */
    public interface ShapeSelectionEventHandler {
        public void f(Shape shp, boolean sel);
    }
    
    /**
     * Private fields that hold event handlers assigned using method references.
     * See for example setMouseClickedHandler()
     */
    private ShapeMouseEventHandler mouseClickedHandler = null;
    private ShapeMouseEventHandler mouseDoubleClickedHandler = null;
    private ShapeMouseEventHandler mouseDraggedHandler = null;
    private ShapeMouseEventHandler mouseEnteredHandler = null;
    private ShapeMouseEventHandler mouseExitedHandler = null;
    private ShapeMouseEventHandler mouseMovedHandler = null;
    private ShapeMouseEventHandler mousePressedHandler = null;
    private ShapeMouseEventHandler mouseReleasedHandler = null;
    private ShapeSelectionEventHandler selectionChangedHandler = null;

    /**
     * Inner class used to handle Shape mouse events.
     */
    protected ShapeMouseListener shapeListener = new ShapeMouseListener() {
        
        /**
         * Gets the current Shape.
         * @return The current Shape.
         */
        @Override
        public Shape getShape() {
            return Shape.this;
        }
        
        /**
         * Apply current transform and invoke the onMousePressed method.
         * @param   x       x-coordinate of mouse event.
         * @param   y       y-coordinate of mouse event.
         * @param   button  Integer identifying button pressed during mouse event.
         */
        @Override
        public void mousePressed(double x, double y, int button) {

            // If layer is null, the shape has been deleted and should not receive the event
            if (layer == null) return;
            
            // Perform inverse transforms for the Shape coodinate system
            Point2D.Double pt = new Point2D.Double(x, y);
            try {
                layer.transform.inverseTransform(pt, pt);
                transform.inverseTransform(pt, pt); 
            } catch (NoninvertibleTransformException ex) {
                System.err.println(ex);
            }
            double eX = pt.getX();
            double eY = pt.getY();
            
            setSelected(true);
            
            // Invoke method to override
            onMousePressed(eX, eY, button);
            
                // Removed traditional Java event handling method
//            // Notify other registerd listeners
//            for (ShapeMouseListener ml : mouseListeners) {
//                ml.mousePressed(eX, eY, button);
//            }
        }
        
        /**
         * Apply current transform and invoke the onMouseReleased method.
         * @param   x       x-coordinate of mouse event.
         * @param   y       y-coordinate of mouse event.
         * @param   button  Integer identifying button pressed during mouse event.
         */
        @Override
        public void mouseReleased(double x, double y, int button) {
            // If layer is null, the shape has been deleted and should not receive the event
            if (layer == null) return;

            stopDrag();
            
            // Perform inverse transforms for the Shape coodinate system
            Point2D.Double pt = new Point2D.Double(x, y);
            try {
                layer.transform.inverseTransform(pt, pt);
                transform.inverseTransform(pt, pt); 
            } catch (NoninvertibleTransformException ex) {
                System.err.println(ex);
            }
            double eX = pt.getX();
            double eY = pt.getY();
            
            // Invoke method to override
            onMouseReleased(eX, eY, button);
            
            // Removed traditional Java event handling method
//            // Notify other registerd listeners
//            for (ShapeMouseListener ml : mouseListeners) {
//                ml.mouseReleased(eX, eY, button);
//            }
        }
        
        /**
         * Apply current transform and invoke onMouseMoved method.
         * @param   x       x-coordinate of mouse event.
         * @param   y       y-coordinate of mouse event.
         * @param   button  Integer identifying button pressed during mouse event.
         */
        @Override
        public void mouseMoved(double x, double y, int button) {
            // and perform inverse transforms for the Shape coodinate system

            // If layer is null, the shape has been deleted and should not receive the event
            if (layer == null) return;

            Point2D.Double pt = new Point2D.Double(x, y);
            try {
                layer.transform.inverseTransform(pt, pt);
                transform.inverseTransform(pt, pt); 
            } catch (NoninvertibleTransformException ex) {
                System.err.println(ex);
            }
            double eX = pt.getX();
            double eY = pt.getY();
            
            // Invoke method to override
            onMouseMoved(eX, eY, button);
            
            // Removed traditional Java event handling method
//            // Notify other registerd listeners
//            for (ShapeMouseListener ml : mouseListeners) {
//                ml.mouseMoved(eX, eY, button);
//            }
        }
        
        /**
         * Apply current transform and invoke the onMouseDragged method.
         * @param   x       x-coordinate of mouse event.
         * @param   y       y-coordinate of mouse event.
         * @param   button  Integer identifying button pressed during mouse event.
         */
        @Override
        public void mouseDragged(double x, double y, int button) {
            // Perform inverse transforms for the Shape coodinate system

            // If layer is null, the shape has been deleted and should not receive the event
            if (layer == null) return;

            Point2D.Double pt = new Point2D.Double(x, y);
            try {
                layer.transform.inverseTransform(pt, pt);
                transform.inverseTransform(pt, pt); 
            } catch (NoninvertibleTransformException ex) {
                System.err.println(ex);
            }
            double eX = pt.getX();
            double eY = pt.getY();
            
            // Invoke method to override
            onMouseDragged(eX, eY, button);
            
            // Removed traditional Java event handling method
//            // Notify other registerd listeners
//            for (ShapeMouseListener ml : mouseListeners) {
//                ml.mouseDragged(eX, eY, button);
//            }
        }
        
        /**
         * Apply current transform and invoke the onMouseClicked method.
         * @param   x       x-coordinate of mouse event.
         * @param   y       y-coordinate of mouse event.
         * @param   button  Integer identifying button pressed during mouse event.
         */
        @Override
        public void mouseClicked(double x, double y, int button) {
            // Perform inverse transforms for the Shape coodinate system

            // If layer is null, the shape has been deleted and should not receive the event
            if (layer == null) return;

            Point2D.Double pt = new Point2D.Double(x, y);
            try {
                layer.transform.inverseTransform(pt, pt);
                transform.inverseTransform(pt, pt); 
            } catch (NoninvertibleTransformException ex) {
                System.err.println(ex);
            }
            double eX = pt.getX();
            double eY = pt.getY();
            
            // Invoke method to override
            onMouseClicked(eX, eY, button);
            
            // Removed traditional Java event handling method
//            // Notify other registerd listeners
//            for (ShapeMouseListener ml : mouseListeners) {
//                ml.mouseClicked(eX, eY, button);
//            }
        }
        
        /**
         * Apply current transform and invoke the onMouseDoubleClicked method.
         * @param   x       x-coordinate of mouse event.
         * @param   y       y-coordinate of mouse event.
         * @param   button  Integer identifying button pressed during mouse event.
         */
        @Override
        public void mouseDoubleClicked(double x, double y, int button) {
            // Perform inverse transforms for the Shape coodinate system

            // If layer is null, the shape has been deleted and should not receive the event
            if (layer == null) return;

            Point2D.Double pt = new Point2D.Double(x, y);
            try {
                layer.transform.inverseTransform(pt, pt);
                transform.inverseTransform(pt, pt); 
            } catch (NoninvertibleTransformException ex) {
                System.err.println(ex);
            }
            double eX = pt.getX();
            double eY = pt.getY();
            
            // Invoke method to override
            onMouseDoubleClicked(eX, eY, button);
            
            // Removed traditional Java event handling method
//            // Notify other registerd listeners
//            for (ShapeMouseListener ml : mouseListeners) {
//                ml.mouseDoubleClicked(eX, eY, button);
//            }
        }        
        
        /**
         * Apply current transform and invoke the onMouseEntered method.
         * @param   x       x-coordinate of mouse event.
         * @param   y       y-coordinate of mouse event.
         * @param   button  Integer identifying button pressed during mouse event.
         */
        @Override
        public void mouseEntered(double x, double y, int button) {
            // Perform inverse transforms for the Shape coodinate system

            // If layer is null, the shape has been deleted and should not receive the event
            if (layer == null) return;

            Point2D.Double pt = new Point2D.Double(x, y);
            try {
                layer.transform.inverseTransform(pt, pt);
                transform.inverseTransform(pt, pt); 
            } catch (NoninvertibleTransformException ex) {
                System.err.println(ex);
            }
            double eX = pt.getX();
            double eY = pt.getY();
            
            // Invoke method to override
            onMouseEntered(eX, eY, button);
            
            // Removed traditional Java event handling method
//            // Notify other registerd listeners
//            for (ShapeMouseListener ml : mouseListeners) {
//                ml.mouseEntered(eX, eY, button);
//            }
        }
        
        /**
         * Apply current transform and invoke the onMouseExited method.
         * @param   x       x-coordinate of mouse event.
         * @param   y       y-coordinate of mouse event.
         * @param   button  Integer identifying button pressed during mouse event.
         */
        @Override
        public void mouseExited(double x, double y, int button) {
            // Perform inverse transforms for the Shape coodinate system

            // If layer is null, the shape has been deleted and should not receive the event
            if (layer == null) return;

            Point2D.Double pt = new Point2D.Double(x, y);
            try {
                layer.transform.inverseTransform(pt, pt);
                transform.inverseTransform(pt, pt); 
            } catch (NoninvertibleTransformException ex) {
                System.err.println(ex);
            }
            double eX = pt.getX();
            double eY = pt.getY();
            
            // Invoke method to override
            onMouseExited(eX, eY, button);
            
            // Removed traditional Java event handling method
//            // Notify other registerd listeners
//            for (ShapeMouseListener ml : mouseListeners) {
//                ml.mouseExited(eX, eY, button);
//            }
        }
    };
    
    /**
     * Constructor for Shape object. Optional Pad object parameter.
     * @param   x       Shape x-coordinate
     * @param   y       Shape y-coordinate
     * @param   width   Shape width
     * @param   height  Shape height
     * @param   layer   Layer object to add Shape to, or null if not to add Shape to a Layer
     */
    public Shape(double x, double y, double width, double height, Layer layer)
    {
        // initialize instance variables
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.transform = new AffineTransform();
        
        this.fillColor = Color.white;
        this.strokeColor = Color.black;
        this.strokeWidth = 1;

        if (layer != null) layer.addShape(this);
    }
    
    // Removed traditional Java event handling method
//    /**
//     * Add object to the list of items that are notified on Pad's mouse events.
//     * @param o The PadMouseListener object to be added.
//     */
//    public void addMouseListener(ShapeMouseListener o) {
//        mouseListeners.add(o);
//    }
//    
//    /**
//     * Remove object from Pad's mouse listener list.
//     * @param o The PadMouseListener object to be removed.
//     */
//    public void removeMouseListener(ShapeMouseListener o) {
//        mouseListeners.remove(o);
//    }
    
    /**
     * A utility method that returns this Shape’s Pad instance
     * @return The current Pad singleton object.
     */
    public Pad getPad(){
        return Pad.getPad();
    }
    
    /**
     * Get flag indicating whether or not Shape should be filled.
     * @return Boolean indicating whether or not Shape should be filled.
     */
    public boolean getFilled() {
        return this.filled;
    }
    
    /**
     * Set flag indicating whether or not Shape should be filled.
     * @param filled true if Shape to be filled when drawn.
     */
    public void setFilled(boolean filled) {
        this.filled = filled;
        repaint();
    }
    
    /**
    * Get fill color that currently used to fill the shape when drawn
    * @return     The current fill color object
    */
    public Color getFillColor() {
        return this.fillColor;
    }

    /**
     * Set the gray scale fill color with which to draw the shape.
     * @param gray the gray scale value in the range [0, 255]
     */
    public void setFillColor(double gray) {
        this.setFillColor(gray, gray, gray, 255);
    }
    
    /**
     * Set the fill color with which to draw the shape
     * @param   red     the red component of the color [0, 255]
     * @param   green   the green component of the color [0, 255]
     * @param   blue    the blue component of the color [0, 255]
     */
    //public void setFillColor(int red, int green, int blue) {
    public void setFillColor(double red, double green, double blue) {
        setFillColor( red, green, blue, 255 );
    }

    /**
     * Set the fill color with which to fill the shape
     * @param   red     the red component of the color [0, 255]
     * @param   green   the green component of the color [0, 255]
     * @param   blue    the blue component of the color [0, 255]
     * @param   alpha   the color transparency [0, 255]
     */
    //public void setFillColor(int red, int green, int blue, int alpha) {
    public void setFillColor(double red, double green, double blue, double alpha) {
        int r = (int)Math.round(Util.constrain(red, 0, 255));
        int g = (int)Math.round(Util.constrain(green, 0, 255));
        int b = (int)Math.round(Util.constrain(blue, 0, 255));
        int a = (int)Math.round(Util.constrain(alpha, 0, 255));
        setFillColor( new Color(r, g, b, a) );
    }
    
    /**
     * Set the fill color by passing a Color object
     * @param color the Color to be used for fill
     */
    public void setFillColor(Color color) {
        this.fillColor = color;
        this.setFilled( true );
        repaint();
    }

    /**
     * Get flag indicating whether or not Shape should be stroked.
     * @return boolean indicating whether Shape is stroked
     */
    public boolean getStroked() {
        return this.stroked;
    }
    
    /**
     * Set flag indicating whether or not Shape should be stroked.
     * @param stroked true if Shape to be stroked when drawn.
     */
    public void setStroked(boolean stroked) {
        this.stroked = stroked;
        repaint();
    }
    
    /**
     * Get the current Shape stroke color.
     * @return Current Shape stroke color.
     */
    public Color getStrokeColor() {
        return this.strokeColor;
    }

    /**
     * Set the gray scale stroke color with which to draw the shape.
     * @param gray the gray scale value in the range [0, 255]
     */
    public void setStrokeColor(double gray) {
        this.setStrokeColor(gray, gray, gray, 255);
    }
    
    /**
     * Set the current Shape stroke color.
     * @param red       the red component of the stroke color [0, 255]
     * @param green     the green component of the stroke color [0, 255]
     * @param blue      the blue component of the stroke color [0, 255]
     */
    public void setStrokeColor(double red, double green, double blue) {
        setStrokeColor( red, green, blue, 255 );
    }

    /**
     * Set the fill color with which to stroke the shape
     * @param   red     the red component of the color [0, 255]
     * @param   green   the green component of the color [0, 255]
     * @param   blue    the blue component of the color [0, 255]
     * @param   alpha   the color transparency [0, 255]
     */
    public void setStrokeColor(double red, double green, double blue, double alpha) {
        int r = (int)Math.round(Util.constrain(red, 0, 255));
        int g = (int)Math.round(Util.constrain(green, 0, 255));
        int b = (int)Math.round(Util.constrain(blue, 0, 255));
        int a = (int)Math.round(Util.constrain(alpha, 0, 255));
        setStrokeColor( new Color(r, g, b, a) );
    }
    
    /**
     * Set the stroke color of the Shape
     * @param clr       the Color object to be used as the stroke color
     */
    public void setStrokeColor(Color clr) {
        this.strokeColor = clr;
        this.setStroked( true );
        repaint();
    }
    
    /**
     * Get the current stroke width for this Shape.
     * @return A float value for the current stroke width.
     */
    public double getStrokeWidth() {
        return this.strokeWidth;
    }
    
    /**
     * Set the stroke width for this Shape.
     * @param width The stroke width value to set.
     */
    public void setStrokeWidth(double width) {
        this.strokeWidth = width;
        this.setStroked( true );
        repaint();
    }
    
    /**
     * Set the current location of the shape and redraw it.
     * @param   x   the x-value of the position
     * @param   y   the y-value of the position
     */
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
        repaint();
    }
    
    /**
     * Set the current location of the shape and redraw it.
     * @param   p   a Point object containing the coordinates of the new location
     */
    public void setLocation(Point p) {
        this.setLocation( p.getX(), p.getY() );
    }
    
    /**
     * Set the Pad location of the Shape 
     * @param p The point indicating Pad coordinates
     */
    public void setPadLocation(Point p) {
        Point2D.Double pt = new Point2D.Double(p.getX(), p.getY());
        try {
            transform.inverseTransform(pt, pt);
        } catch (NoninvertibleTransformException ex) {
            System.err.println(ex);
        }
        setLocation(new Point(pt.getX(), pt.getY()));
    }
    
    /**
     * Set the window location of the Shape 
     * @param p The point indicating window coordinates
     */
    public void setWindowLocation(Point p) {
        Point2D.Double pt = new Point2D.Double(p.getX(), p.getY());
        try {
            layer.transform.inverseTransform(pt, pt);
            transform.inverseTransform(pt, pt);
        } catch (NoninvertibleTransformException ex) {
            System.err.println(ex);
        }
        setLocation(new Point(pt.getX(), pt.getY()));
    }
    
    /**
     * Return the current location of the shape as a Point object.
     * @return A Point object representing the current upper left corner of the Shape’s bounding box.
     */
    public Point getLocation() {
        return new Point(x, y);
    }
    
    /**
     * Get the coordinates of the Shape location in the Pad coordinate system
     * @return A Point object holding Pad coordinates of the current Shape location
     */
    public Point getPadLocation() {
        Point2D pt = new Point2D.Double(x, y);
        transform.transform(pt, pt);
        return new Point(pt.getX(), pt.getY());
    }
    
    /**
     * Get the coordinates of the Shape location in the window coordinate system
     * @return A Point object holding window coordinates of the current Shape location
     */
    public Point getWindowLocation() {
        Point2D pt = new Point2D.Double(x, y);
        transform.transform(pt, pt);
        layer.transform.transform(pt, pt);
        return new Point(pt.getX(), pt.getY());
    }
    
    /**
     * Set the Shape position so that the center is at the given coordinates.
     * @param cx the x-value of the position
     * @param cy the y-value of the position
     */
    public void setCenter(double cx, double cy) {
        this.setLocation(cx - 0.5*width, cy - 0.5*height);
    }
    
    /**
     * Return the current center point of the shape as a Point object.
     * @return A Point object representing the current center point of the Shape’s bounding box.
     */
    public Point getCenter() {
        return new Point(x + 0.5*width, y + 0.5*height);
    }
    
    /**
     * Get x-coordinate of Shape
     * @return The Shape’s x-coordinate
     */
    public double getX() {
        return this.x;
    }
    
    /**
     * Get y-coordinate of Shape
     * @return The Shape’s y-coordinate
     */
    public double getY() {
        return this.y;
    }
    
    /**
     * Set x-coordinate of Shape
     * @param x x-coordinate value
     */
    public void setX(double x) {
        this.setLocation(x, this.y);
    }
    
    /**
     * Set y-coordinate of Shape
     * @param y y-coordinate value
     */
    public void setY(double y) {
        this.setLocation(this.x, y);
    }
    
    /**
     * Move the Shape by the amounts specified in the x and y-directions
     * @param dx Amount to move in the x-direction (pixels)
     * @param dy Amount to move in the y-direction (pixels)
     */
    public void move(double dx, double dy) {
        this.setLocation(this.x+dx, this.y+dy);
    }
    
    /**
     * Reset the size of the Shape and trigger a repaint
     * @param   width   new width
     * @param   height   new height
     */
    public void setSize(double width, double height) 
    {
        this.width = width;
        this.height = height;
        repaint();
    }
    
    /**
     * Reset the size of the Shape and trigger a repaint
     * @param   d   Dimension containing Shape size
     */
    public void setSize(Dimension d) 
    {
        setSize( d.getWidth(), d.getHeight() );
    }
    
    /**
     * Return the current Shape size as a Dimension object
     * @return  A Dimension object holding the Shape width and height
     */
    public Dimension getSize()
    {
        return new Dimension(width, height);
    }
    
    /**
     * Get the Shape’s width
     * @return A double that is the Shape’s current width
     */
    public double getWidth() {
        return this.width;
    }
    
    /**
     * Set a new Shape width.
     * @param width A double value that is the new Shape width.
     */
    public void setWidth(double width) {
        //this.width = width;
        //repaint();
        this.setSize(width, this.getHeight());
    }
    
    /**
     * Get the Shape’s height
     * @return A double that is the Shape’s current height
     */
    public double getHeight() {
        return this.height;
    }
    
    /**
     * Set a new Shape height.
     * @param height A double value that is the new Shape height.
     */
    public void setHeight(double height) {
        //this.height = height;
        //repaint();
        this.setSize(this.getWidth(), height);
    }
    
    /**
     * Set the boolean indicating whether or not the Shape will receive events
     * that can be handled by overriding event methods
     * @param enabled Boolean indicating if this Shape should receive events.
     */
    public void setEventsEnabled(boolean enabled) {
        eventsEnabled = enabled;
    }
    
    /**
     * Return setting that indicates if the Shape object will receive events
     * that are implementable by overriding methods
     * @return boolean indicating whether Shape receives events
     */
    public boolean getEventsEnabled() {
        return eventsEnabled;
    }
    
    /**
     * Set visible status of Shape
     * @param visible Boolean indicating whether or not the Shape should be drawn.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
        repaint();
    }
    
    /**
     * Get visible status of Shape
     * @return true if the Shape is currently marked to be visible.
     */
    public boolean getVisible() {
        return visible;
    }
    
    /**
     * Set the selected state of the Shape
     * @param sel Boolean indicating if the Shapes should be selected
     */
    public void setSelected(boolean sel) {
        if (selectable == false) return;    // Do nothing if the shape cannot be selected
        if (selected == sel) return;        // Do nothing if selected state has not changed
        selected = sel;                     // Set the selected value
        
        onSelectionChanged(sel);            // Invoke overridable method
        //if (sel == true) toFront();         // Bring to front if set to selected
        repaint();
    }
    
    /**
     * Return selected state of this Shape.
     * @return Boolean indicating the selected state of the Shape.
     */
    public boolean getSelected() {
        return selected;
    }
    
    /**
     * Set the flag that indicates if the shape can be selected
     * @param   select   a boolean value (true/false) to assign to the flag
     */
    public void setSelectable(boolean select) {
        if (!select) setSelected(false);
        selectable = select;
    }
    
    /**
     * Determine if the Shape is set so that it can be selected
     * @return true if the Shape can be selected
     */
    public boolean getSelectable() {
        return selectable;
    }
    
    /**
     * Set the flag that indicates if the shape can be dragged.
     * If true, then also set selectable to true. Shapes cannot be dragged unless they can be selected.
     * @param   drag   a boolean value (true/false) to assign to the flag
     */
    public void setDraggable(boolean drag) {
        draggable = drag;
        if (drag == true) setSelectable(true);
    }
    
    /**
     * Determine if the Shape is set so that it can be dragged
     * @return true if the Shape can be dragged
     */
    public boolean getDraggable() {
        return draggable;
    }
    
    /**
     * Start dragging this shape
     */
    void startDrag(double x, double y) {
        // Coordinates are provided in the Pad coordinate system
        // Convert to Shape coordinate system and save
        Point2D.Double pt = new Point2D.Double(x, y);
        try {
            layer.transform.inverseTransform(pt, pt);
            transform.inverseTransform(pt, pt);
        } catch (NoninvertibleTransformException ex) {
            System.err.println(ex);
        }
        dragDeltaX = this.x - pt.x;
        dragDeltaY = this.y - pt.y;
    }
    
    /**
     * Stop the dragging process
     */
    void stopDrag() {
        // Anything here?
    }
    
    /**
     * Move the Shape to an offset from the initial drag start position of dX, dY
     * @param dX Change in horizontal distance during dragging
     * @param dY Change in vertical distance during dragging
     */
    void doDrag(double x, double y) {
        // Coordinates are provided in the pad coordinate system
        // Convert to Shape coordinate system
        // A Shape can be dragged only if it is draggable and selected
        if (selected && draggable) {
            Point2D.Double pt = new Point2D.Double(x, y);
            try {
                layer.transform.inverseTransform(pt, pt);
                transform.inverseTransform(pt, pt);
            } catch (NoninvertibleTransformException ex) {
                System.err.println(ex);
            }

            setLocation(pt.x + dragDeltaX, pt.y + dragDeltaY);
        }
    }
    
    /**
     * Move this shape to the end of the draw list
     */
    public void toFront() {
        if (layer != null) layer.toFront(this);
        repaint();
    }
    
    /**
     * Move this shape to the beginning of the draw list
     */
    public void toBack() {
        if (layer != null) layer.toBack(this);
        this.repaint();
    }
    
    /**
     * Generate a representation of the Shape object.
     * @return String representation
     */
    @Override
    public String toString() {
        return "Shape x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", layer=" + layer;
    }
    
    /**
     * Returns true if this shape contains the given point
     * @param x The x-coordinate of the point to test
     * @param y The y-coordinate of the point to test
     * @return  true if the Shape contains the given point.
     */
    public boolean contains(double x, double y) {
        return getArea().contains(x, y);
    }

    /**
     * Returns true if this shape contains the given point
     * @param p a Point object containing the coordinates to be tested 
     * @return  true if the Shape contains the given point.
     */
    public boolean contains(Point p) {
        return this.contains(p.getX(), p.getY());
    }
    
    /**
     * Get a rectangular Area object that surrounds the Shape
     * @return Rectangular Area object
     */
    public Area getArea() {
        return new Area( new java.awt.geom.Rectangle2D.Double(x, y, width, height));
    }
    
    /**
     * Return the Layer on which this Shape exists
     * @return Layer holding this Shape
     */
    public Layer getLayer() {
        return this.layer;
    }
    
    /**
     * Determine if the current Shape intersects with the shp Shape parameter
     * @param shp The Shape with which to test intersection
     * @return true if the intersection is not empty
     */
    public boolean intersects(Shape shp) {
        Area area1 = shp.getArea();
        Area area2 = this.getArea();
        area2.intersect(area1);
        return !area2.isEmpty();
    }
    
    /**
     * Utility method to trigger a repaint of the entire Pad.
     */
    protected void repaint() {
        if (layer != null) layer.repaint();
    }
    
    /**
     * Assign an onMouseClicked event handler using a method reference.
     * @param handler Method reference to an event handler
     */
    public void setMouseClickedHandler( ShapeMouseEventHandler handler ) {
        this.mouseClickedHandler = handler;
    }
    
    /**
     * Assign an onMouseDoubleClicked event handler using a method reference.
     * @param handler Method reference to an event handler
     */
    public void setMouseDoubleClickedHandler( ShapeMouseEventHandler handler ) {
        this.mouseDoubleClickedHandler = handler;
    }
    
    /**
     * Assign an onMouseDragged event handler using a method reference.
     * @param handler Method reference to an event handler
     */
    public void setMouseDraggedHandler( ShapeMouseEventHandler handler ) {
        this.mouseDraggedHandler = handler;
    }
    
    /**
     * Assign an onMouseEntered event handler using a method reference.
     * @param handler Method reference to an event handler
     */
    public void setMouseEnteredHandler( ShapeMouseEventHandler handler ) {
        this.mouseEnteredHandler = handler;
    }
    
    /**
     * Assign an onMouseExited event handler using a method reference.
     * @param handler Method reference to an event handler
     */
    public void setMouseExitedHandler( ShapeMouseEventHandler handler ) {
        this.mouseExitedHandler = handler;
    }
    
    /**
     * Assign an onMouseMoved event handler using a method reference.
     * @param handler Method reference to an event handler
     */
    public void setMouseMovedHandler( ShapeMouseEventHandler handler ) {
        this.mouseMovedHandler = handler;
    }
    
    /**
     * Assign an onMousePressed event handler using a method reference.
     * @param handler Method reference to an event handler
     */
    public void setMousePressedHandler( ShapeMouseEventHandler handler ) {
        this.mousePressedHandler = handler;
    }
    
    /**
     * Assign an onMouseReleased event handler using a method reference.
     * @param handler Method reference to an event handler
     */
    public void setMouseReleasedHandler( ShapeMouseEventHandler handler ) {
        this.mouseReleasedHandler = handler;
    }
    
    /**
     * Assign a selectionChanged event handler using a method reference.
     * @param handler Method reference to an event handler
     */
    public void setSelectionChangedHandler( ShapeSelectionEventHandler handler ) {
        this.selectionChangedHandler = handler;
    }
    
    /**
     * Handle mouse pressed Shape events. Override in subclasses to implement.
     * @param x         x-position of the mouse when event occurred
     * @param y         y-position of the mouse when event occurred
     * @param button    Integer indicating button pressed during event.
     */
    public void onMousePressed(double x, double y, int button) {
        // Also, override to implement.
        if (mousePressedHandler != null) {
            mousePressedHandler.f(this, x, y, button);
        }
    }
    
    /**
     * Handle mouse released Shape events. Override in subclasses to implement.
     * @param x         x-position of the mouse when event occurred
     * @param y         y-position of the mouse when event occurred
     * @param button    Integer indicating button pressed during event.
     */
    public void onMouseReleased(double x, double y, int button) {
        // Also, override to implement.
        if (mouseReleasedHandler != null) {
            mouseReleasedHandler.f(this, x, y, button);
        }
    }
    
    /**
     * Handle mouse clicked Shape events. Override in subclasses to implement.
     * @param x         x-position of the mouse when event occurred
     * @param y         y-position of the mouse when event occurred
     * @param button    Integer indicating button pressed during event.
     */
    public void onMouseClicked(double x, double y, int button) {
        // Also, override to implement.
        if (mouseClickedHandler != null) {
            mouseClickedHandler.f(this, x, y, button);
        }
    }

    /**
     * Handle mouse double-clicked Shape events. Override in subclasses to implement.
     * @param x         x-position of the mouse when event occurred
     * @param y         y-position of the mouse when event occurred
     * @param button    Integer indicating button pressed during event.
     */
    public void onMouseDoubleClicked(double x, double y, int button) {
        // Also, override to implement.
        if (mouseDoubleClickedHandler != null) {
            mouseDoubleClickedHandler.f(this, x, y, button);
        }
    }

    /**
     * Handle mouse moved Shape events. Override in subclasses to implement.
     * @param x         x-position of the mouse when event occurred
     * @param y         y-position of the mouse when event occurred
     * @param button    Integer indicating button pressed during event.
     */
    public void onMouseMoved(double x, double y, int button) {
        // Also, override to implement.
        if (mouseMovedHandler != null) {
            mouseMovedHandler.f(this, x, y, button);
        }
    }
    
    /**
     * Handle mouse dragged Shape events. Override in subclasses to implement.
     * @param x         x-position of the mouse when event occurred
     * @param y         y-position of the mouse when event occurred
     * @param button    Integer indicating button pressed during event.
     */
    public void onMouseDragged(double x, double y, int button) {
        // Also, override to implement.
        if (mouseDraggedHandler != null) {
            mouseDraggedHandler.f(this, x, y, button);
        }
    }
    
    /**
     * Handle mouse entered Shape events. Override in subclasses to implement.
     * @param x         x-position of the mouse when event occurred
     * @param y         y-position of the mouse when event occurred
     * @param button    Integer indicating button pressed during event.
     */
    public void onMouseEntered(double x, double y, int button) {
        // Also, override to implement.
        if (mouseEnteredHandler != null) {
            mouseEnteredHandler.f(this, x, y, button);
        }
    }
    
    /**
     * Handle mouse exited Shape events. Override in subclasses to implement.
     * @param x         x-position of the mouse when event occurred
     * @param y         y-position of the mouse when event occurred
     * @param button    Integer indicating button pressed during event.
     */
    public void onMouseExited(double x, double y, int button) {
        // Also, override to implement.
        if (mouseExitedHandler != null) {
            mouseExitedHandler.f(this, x, y, button);
        }
    }
    
    /**
     * React to change in selection state by overriding
     * @param sel   Boolean indicating new selection state of Shape.
     */
    public void onSelectionChanged(boolean sel) {
        // Also, override to implement.
        if (selectionChangedHandler != null) {
            selectionChangedHandler.f(this, sel);
        }
    }
    
    /**
     * Add a rotation angle to Shape transform.
     * @param angle Adds the rotation angle to the current transform (degrees)
     */
    public void rotate(double angle) {
        transform.rotate(angle*Math.PI/180.0);
        repaint();
    }
    
    /**
     * Add a translate to Shape transform.
     * @param deltaX Translate shape in the x-direction by deltaX
     * @param deltaY Translate shape in the y-direction by deltaY
     */
    public void translate(double deltaX, double deltaY) {
        transform.translate(deltaX, deltaY);
        repaint();
    }
    
    /**
     * Add a scale factor to Shape transform.
     * @param factor Scale the shape by a scale factor
     */
    public void scale(double factor) {
        transform.scale(factor, factor);
        repaint();
    }

    /**
     * Add a scale factor to Shape transform.
     * @param xFactor Scale the shape in the x-direction by a xFactor
     * @param yFactor Scale the shape in the y-direction by a yFactor

     */
    public void scale(double xFactor, double yFactor) {
        transform.scale(xFactor, yFactor);
        repaint();
    }
    
    /**
     * Resets the shape to have no transformation.
     */
    public void reset() {
        transform.setToIdentity();
        repaint();
    }
    
    /**
     * Clone the Shape\'s current AffineTransform.
     * @return The cloned AffineTransform.
     */
    public AffineTransform getTransform() {
        return (AffineTransform)transform.clone();
    }
    
    /**
     * Set a new AffineTransform for the Shape.
     * @param transform The new transform.
     */
    public void setTransform(AffineTransform transform) {
        this.transform = transform;
        repaint();
    }
    
    /**
     * Convert transformed Shape coordinates to window coordinates 
     * returns as a new Point object.
     * This method is useful for converting transformed coordinates received 
     * from a Shape mouse event method back to original window coordinates.
     * @param x Transformed x-coordinate
     * @param y Transformed y-coordinate
     * @return A Point object containing the transformed coordinates
     */
    public double[] toWindowCoords(double x, double y) {
        Point pt = toWindowCoords( new Point(x, y) );
        return new double[] { pt.getX(), pt.getY() };
    }
    
    /**
     * Convert transformed Shape coordinates in a Point object
     * to window coordinates returned as a new Point object.
     * This method is useful for converting transformed coordinates received 
     * from a Shape mouse event method back to original window coordinates.
     * @param   pt  A Point object 
     * @return  A new Point object containing the transformed coordinates
     */
    public Point toWindowCoords(Point pt) {
        Point2D.Double pt1 = new Point2D.Double(pt.getX(), pt.getY());
        transform.transform(pt1, pt1);
        if (layer != null) layer.transform.transform(pt1, pt1);
        return new Point(pt1.getX(), pt1.getY());
    }
    
    /**
     * Draw a selection rectangle around the shape
     */
    void drawSelRect(Graphics2D g) {
        Rectangle2D.Double rect = new Rectangle2D.Double(x, y, width, height);
        g.setColor(new Color(127, 127, 127, 64));
        g.setStroke( new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND) );
        g.draw( rect );
    }
    
    /**
     * Draw the region that will be used to detect a hit on the shape using the given color.
     * @param g     The Graphics2D on which to draw the hit region
     * @param clr   The unique color used to fill and stroke the hit region
     */
    void draw(Graphics2D g, Color clr) {
        Color oldFillColor = this.fillColor;
        Color oldStrokeColor = this.strokeColor;
        this.fillColor = clr;
        this.strokeColor = clr;
        this.draw(g);
        this.fillColor = oldFillColor;
        this.strokeColor = oldStrokeColor;
    }

    /**
     * Draw the shape. Abstract method to be overridden in subclasses.
     * @param g The Graphics2D object on which to draw.
     */
    public abstract void draw(Graphics2D g);    // Override in subclass    
}
