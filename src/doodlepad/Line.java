/*
 * Line.java
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
import java.awt.geom.Line2D;
import java.util.Random;

/**
 * A class that implements a graphical straight line shape.
 * 
 * @author Mark F. Russo, Ph.D.
 * @version 1.0
 */
public class Line extends Shape
{
    /**
     * Line object constructor.
     * @param   x1  The x-coordinate of the Line object's first point.
     * @param   y1  The y-coordinate of the Line object's first point.
     * @param   x2  The x-coordinate of the Line object's second point.
     * @param   y2  The y-coordinate of the Line object's second point.
     */
    public Line(double x1, double y1, double x2, double y2) {
        this(x1, y1, x2, y2, Pad.getPad().getLayer(0));
    }
    
    /**
     * Line object constructor.
     * @param   x1  The x-coordinate of the Line object's first point.
     * @param   y1  The y-coordinate of the Line object's first point.
     * @param   x2  The x-coordinate of the Line object's second point.
     * @param   y2  The y-coordinate of the Line object's second point.
     * @param   pad The Pad object to which this Line should be added.
     */
    public Line(double x1, double y1, double x2, double y2, Pad pad) {
        this(x1, y1, x2, y2, pad.getLayer(0));
    }
    
    /**
     * Line object constructor.
     * @param   x1      The x-coordinate of the Line object's first point.
     * @param   y1      The y-coordinate of the Line object's first point.
     * @param   x2      The x-coordinate of the Line object's second point.
     * @param   y2      The y-coordinate of the Line object's second point.
     * @param   layer    The Layer object to which the Line should be added, or null if not to add to a Layer.
     */
    public Line(double x1, double y1, double x2, double y2, Layer layer) {
        super(x1, y1, x2-x1, y2-y1, layer);
    }
    
    /**
     * Default constructor for the Line object.
     * Creates a new 100x100 draggable shape and positions it randomly.
     */
    public Line() {
        this(0, 0, 100.0, 100.0);
        Pad pad = Pad.getPad();
        Random rnd = new Random();
        double x = rnd.nextDouble()*(pad.getWidth()-100.0);
        double y = rnd.nextDouble()*(pad.getHeight()-100.0);
        this.setLocation(x, y);
        this.setDraggable(true);
    }

    /**
     * Set new endpoint locations for the Line object
     * @param x1 The x-coordinate of the line's first point.
     * @param y1 The y-coordinate of the line's first point.
     * @param x2 The x-coordinate of the line's second point.
     * @param y2 The y-coordinate of the line's second point.
     */
    public void setPoints(double x1, double y1, double x2, double y2) {
        x = x1;
        y = y1;
        width = x2-x1;
        height = y2-y1;
    }
    
    /**
     * Generate a representation of the Line object.
     * @return String representation
     */
    @Override
    public String toString() {
        return "Line x1=" + x + ", y1=" + y + ", x2=" + (x+width) + ", y2=" + (y+height) + ", layer=" + layer;
    }
    
     /**
     * Draw the Line object
     * @param g         The Graphics2D object on which to draw the Line
     */
    @Override
    public void draw(Graphics2D g)
    {
        if (stroked == true && strokeWidth > 0.0) {
            g.setColor(this.strokeColor);

            float _strokeWidth = (float)strokeWidth;
            //if (selected) _strokeWidth = 2.0f*_strokeWidth;
            g.setStroke( new BasicStroke(_strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND) );
            
            g.draw( new Line2D.Double(x, y, x+width, y+height));
        }
        
        if (selected) drawSelRect(g);
    }
    
    /**
     * Complete the area of the Line object
     * @return An Area object
     */
    @Override
    public Area getArea() {
        return new Area( new java.awt.geom.Line2D.Double(x, y, x+width, y+height));
    }
}
