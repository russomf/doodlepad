/*
 * Oval.java
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

import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.Random;

/**
 * A class that implements a graphical oval shape.
 * 
 * @author Mark F. Russo, Ph.D.
 * @version 1.0
 */
public class Oval extends Shape
{   
    /**
     * Constructor for the Oval constructor.
     * @param   x       The x-coordinate of the upper left-hand corner of the Oval object.
     * @param   y       The y-coordinate of the upper left-hand corner of the Oval object.
     * @param   width   The width of the Oval object.
     * @param   height  The height of the Oval object.
     */
    public Oval(double x, double y, double width, double height) {
        this(x, y, width, height, Pad.getPad().getLayer(0));
    }
    
    /**
     * Constructor for the Oval constructor.
     * @param   x       The x-coordinate of the upper left-hand corner of the Oval object.
     * @param   y       The y-coordinate of the upper left-hand corner of the Oval object.
     * @param   width   The width of the Oval object.
     * @param   height  The height of the Oval object.
     * @param   pad     The Pad to which the Oval should be added.
     */
    public Oval(double x, double y, double width, double height, Pad pad) {
        this(x, y, width, height, pad.getLayer(0));
    }
    
    /**
     * Constructor for the Oval constructor.
     * @param   x       The x-coordinate of the upper left-hand corner of the Oval object.
     * @param   y       The y-coordinate of the upper left-hand corner of the Oval object.
     * @param   width   The width of the Oval object.
     * @param   height  The height of the Oval object.
     * @param   layer   The Layer object to which the Oval should be added, or null if not to add to a Layer.
     */
    public Oval(double x, double y, double width, double height, Layer layer) {
        super(x, y, width, height, layer);
    }

    /**
     * Default constructor for the Oval object.
     * Creates a new 100x100 draggable shape and positions it randomly.
     */
    public Oval() {
        this(0.0, 0.0, 100.0, 100.0);
        Pad pad = Pad.getPad();
        Random rnd = new Random();
        double x = rnd.nextDouble()*(pad.getWidth()-100.0);
        double y = rnd.nextDouble()*(pad.getHeight()-100.0);
        this.setLocation(x, y);
        this.setDraggable(true);
    }
    
    /**
     * Generate a representation of the Oval object
     * @return Oval String representation
     */
    @Override
    public String toString() {
        return "Oval x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", layer=" + layer;
    }
    
    /**
     * Draw the Oval shape
     * @param g         The Graphics2D object on which to draw the Oval
     */
    @Override
    public void draw(Graphics2D g)
    {
        // Create the shape
        Ellipse2D.Double ellipse = new Ellipse2D.Double(x, y, width, height);
        
        if (filled == true) {
            g.setColor(this.fillColor);
            g.fill( ellipse );
        }
        
        if (stroked == true && strokeWidth > 0.0) {
            g.setColor(this.strokeColor);
            
            float _strokeWidth = (float)strokeWidth;
            g.setStroke( new BasicStroke(_strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND) );
            g.draw(ellipse);
        }
        
        if (selected) drawSelRect(g);
    }
    
    /**
     * Complete the area of the Oval object
     * @return An Area object
     */
    @Override
    public Area getArea() {
        return new Area( new java.awt.geom.Ellipse2D.Double(x, y, width, height));
    }
    
//    /**
//     * Draw the region that will be used to detect a hit on the shape
//     * @param g The Graphics2D on which to draw the hit region
//     * @param clr The unique color used to fill on the hit region
//     */
//    @Override
//    public void drawHitRegion(Graphics2D g, Color clr)
//    {
//        g.setColor(clr);
//        g.fillOval(x, y, width, height);
//    }
}
