/*
 * Rectangle.java
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
import java.awt.geom.Rectangle2D;
import java.util.Random;

/**
 * A class that implements a graphical rectangle object
 * 
 * @author Mark F. Russo, Ph.D.
 * @version 1.0
 */
public class Rectangle extends Shape
{
    /**
     * Constructor for the Rectangle constructor.
     * @param   x       The x-coordinate of the upper left-hand corner of the Rectangle object.
     * @param   y       The y-coordinate of the upper left-hand corner of the Rectangle object.
     * @param   width   The width of the Rectangle object.
     * @param   height  The height of the Rectangle object.
     */
    public Rectangle(double x, double y, double width, double height) {
        this(x, y, width, height, Pad.getPad().getLayer(0)); //Pad.getPad());
    }
    
//    /**
//     * Constructor for the Rectangle constructor.
//     * @param   x       The x-coordinate of the upper left-hand corner of the Rectangle object.
//     * @param   y       The y-coordinate of the upper left-hand corner of the Rectangle object.
//     * @param   width   The width of the Rectangle object.
//     * @param   height  The height of the Rectangle object.
//     * @param   pad     The Pad to which the Rectangle should be added.
//     */
//    public Rectangle(double x, double y, double width, double height, Pad pad) {
//        this(x, y, width, height, pad.getLayer(0));
//    }
    
    /**
     * Constructor for the Rectangle constructor.
     * @param   x       The x-coordinate of the upper left-hand corner of the Rectangle object.
     * @param   y       The y-coordinate of the upper left-hand corner of the Rectangle object.
     * @param   width   The width of the Rectangle object.
     * @param   height  The height of the Rectangle object.
     * @param   layer   The Layer object to which the Oval should be added, or null if not to add to a Layer.
     */
    public Rectangle(double x, double y, double width, double height, Layer layer) {
        super(x, y, width, height, layer);
    }

    /**
     * Default constructor for the Rectangle object.
     * Creates a new 100x100 draggable shape and positions it randomly.
     */
    public Rectangle() {
        this(0, 0, 100.0, 100.0);
        Pad pad = Pad.getPad();
        Random rnd = new Random();
        double x = rnd.nextDouble()*(pad.getWidth()-100.0);
        double y = rnd.nextDouble()*(pad.getHeight()-100.0);
        this.setLocation(x, y);
        this.setDraggable(true);
    }
    
     /**
     * Draw the Rectangle
     * @param g         The Graphics2D object on which to draw the Rectangle
     */
    @Override
    public void draw(Graphics2D g)
    {
        Rectangle2D.Double rect = new Rectangle2D.Double(x, y, width, height);
        
        if (filled == true) {
            g.setColor(fillColor);
            g.fill( rect);
        }
        
        if (stroked == true && strokeWidth > 0.0) {
            g.setColor(strokeColor);
            
            float _strokeWidth = (float)strokeWidth;
            //if (selected) _strokeWidth = 2.0f*_strokeWidth;
            g.setStroke( new BasicStroke(_strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND) );
            g.draw( rect );
        }
        
        if (selected) drawSelRect(g);
    }
    
    /**
     * Generate a representation of the Rectangle object
     * @return String representation
     */
    @Override
    public String toString() {
        return "Rectangle x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", layer=" + layer;
    }
}
