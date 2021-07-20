/*
 * RoundRect.java
 * 
 * Author: Mark F. Russo, Ph.D.
 * Copyright (c) 2012-2021 Mark F. Russo
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
import java.awt.geom.RoundRectangle2D;
import java.util.Random;

/**
 * A class that implements a graphical rectangle object with rounded corners
 * 
 * @author Mark F. Russo, Ph.D.
 * @version 1.0
 */
public class RoundRect extends Shape
{
    private double arcWidth = 0;
    private double arcHeight = 0;
    
    /**
     * Constructor for the RoundRect object, a rounded rectangle.
     * @param   x           The x-coordinate of the upper left corner of the RoundRect object.
     * @param   y           The y-coordinate of the upper left corner of the RoundRect object.
     * @param   width       The width of the RoundRect object.
     * @param   height      The height of the RoundRect object.
     * @param   arcWidth    The width of the arc that forms a corner of the RoundRect object.
     * @param   arcHeight   The height of the arc that forms a corner of the RoundRect object.
     */
    public RoundRect(double x, double y, double width, double height, double arcWidth, double arcHeight) {
        this(x, y, width, height, arcWidth, arcHeight, Pad.getPad().getLayer(0));
    }
    
//    /**
//     * Constructor for the RoundRect object, a rounded rectangle.
//     * @param   x           The x-coordinate of the upper left corner of the RoundRect object.
//     * @param   y           The y-coordinate of the upper left corner of the RoundRect object.
//     * @param   width       The width of the RoundRect object.
//     * @param   height      The height of the RoundRect object.
//     * @param   arcWidth    The width of the arc that forms a corner of the RoundRect object.
//     * @param   arcHeight   The height of the arc that forms a corner of the RoundRect object.
//     * @param   pad         The Pad to which this RoundRect should be added.
//     */
//    public RoundRect(double x, double y, double width, double height, double arcWidth, double arcHeight, Pad pad) {
//        this(x, y, width, height, arcWidth, arcHeight, pad.getLayer(0));
//    }
    
    /**
     * Constructor for the RoundRect object, a rounded rectangle.
     * @param   x           The x-coordinate of the upper left corner of the RoundRect object.
     * @param   y           The y-coordinate of the upper left corner of the RoundRect object.
     * @param   width       The width of the RoundRect object.
     * @param   height      The height of the RoundRect object.
     * @param   arcWidth    The width of the arc that forms a corner of the RoundRect object.
     * @param   arcHeight   The height of the arc that forms a corner of the RoundRect object.
     * @param   layer       The Layer object to which the Image should be added, or null if not to add to a Layer.
     */
    public RoundRect(double x, double y, double width, double height, double arcWidth, double arcHeight, Layer layer) {
        super(x, y, width, height, layer);
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
    }
    
    /**
     * Default constructor for the RoundRect object.
     * Creates a new 100x100 draggable shape and positions it randomly.
     */
    public RoundRect() {
        this(0, 0, 100.0, 100.0, 20.0, 20.0);
        Pad pad = Pad.getPad();
        Random rnd = new Random();
        double x = rnd.nextDouble()*(pad.getWidth()-100.0);
        double y = rnd.nextDouble()*(pad.getHeight()-100.0);
        this.setLocation(x, y);
        this.setDraggable(true);
    }
    
    /**
     * Generate a representation of the RoundRect object.
     * @return String representation
     */
    @Override
    public String toString() {
        return "RoundRect x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", layer=" + layer;
    }
    
    /**
     * Draw the Rounded Rectangle
     * @param g         The Graphics2D object on which to draw the rounded rectangle.
     */
    @Override
    public void draw(Graphics2D g)
    {
        RoundRectangle2D.Double rect = new RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight);
        if (filled == true) {
            g.setColor(this.fillColor);
            g.fill( rect );
        }
        
        if (stroked == true && strokeWidth > 0.0) {
            g.setColor(this.strokeColor);
            
            float _strokeWidth = (float)strokeWidth;
            //if (selected) _strokeWidth = 2.0f*_strokeWidth;
            g.setStroke( new BasicStroke(_strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND) );
            
            g.draw( rect );
        }
        
        // If text, draw it.
        if (this.text != null) { drawText(g); }

        // If selected, draw select rect.
        if (selected) drawSelRect(g);
    }
    
    /**
     * Get the Area object associated with this round rectangle.
     * @return The Area object.
     */
    @Override
    public Area getArea() {
        return new Area( new RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight));
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
//        g.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
//    }
}
