/*
 * Arc.java
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
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.geom.Area;
import java.awt.geom.Arc2D;

/**
 * A class that implements a graphical arc shape.
 * An Arc is a partial section of an ellipse that is bounded by the rectangle
 * defined by the parameters passed to the Arc constructor. The start angle and
 * angle extent define the part of the ellipse that make up the Arc.
 * 
 * @author Mark F. Russo, Ph.D.
 * @version 1.0
 */
public class Arc extends Shape
{
    private double startAngle = 0;
    private double arcAngle = 0;
    
    /**
     * Arc constructor
     * @param   x           The upper x-coordinate of the Arc`s related ellipse bounding box (pixels).
     * @param   y           The upper y-coordinate of the Arc`s related ellipse bounding box (pixels).
     * @param   width       The width of the Arc`s related ellipse (pixels).
     * @param   height      The height of the Arc`s related ellipse bounding box (pixels).
     * @param   startAngle  The starting angle at which to begin drawing the Arc (degrees).
     * @param   arcAngle    The angular extent of the Arc, which defines its length (degrees).
     */
    public Arc(double x, double y, double width, double height, double startAngle, double arcAngle) {
        this(x, y, width, height, startAngle, arcAngle, Pad.getPad().getLayer(0));
    }
    
//    /**
//     * Arc constructor
//     * @param   x           The upper x-coordinate of the Arc`s related ellipse bounding box (pixels).
//     * @param   y           The upper y-coordinate of the Arc`s related ellipse bounding box (pixels).
//     * @param   width       The width of the Arc`s related ellipse (pixels).
//     * @param   height      The height of the Arc`s related ellipse bounding box (pixels).
//     * @param   startAngle  The starting angle at which to begin drawing the Arc (degrees).
//     * @param   arcAngle    The angular extent of the Arc, which defines its length (degrees).
//     * @param   pad         The Pad object on which to create the Arc.
//     */
//    public Arc(double x, double y, double width, double height, double startAngle, double arcAngle, Pad pad) {
//        this(x, y, width, height, startAngle, arcAngle, pad.getLayer(0));
//    }
    
    /**
     * Arc constructor
     * @param   x           The upper x-coordinate of the Arc`s related ellipse bounding box (pixels).
     * @param   y           The upper y-coordinate of the Arc`s related ellipse bounding box (pixels).
     * @param   width       The width of the Arc`s related ellipse (pixels).
     * @param   height      The height of the Arc`s related ellipse bounding box (pixels).
     * @param   startAngle  The starting angle at which to begin drawing the Arc (degrees).
     * @param   arcAngle    The angular extent of the Arc, which defines its length (degrees).
     * @param   layer       Layer object to add Arc to, or null if not to add Arc to a Layer.
     */
    public Arc(double x, double y, double width, double height, double startAngle, double arcAngle, Layer layer) {
        super(x, y, width, height, layer);
        this.startAngle = startAngle;
        this.arcAngle = arcAngle;
        this.fillColor = new Color(0, 0, 0, 0);     // Start transparent
    }
    
    /**
     * Default constructor for an Arc.
     * Specifies generic parameters to allow an Arc object to be created quickly and easily.
     */
    public Arc() {
        this(100., 100., 100., 100., 0., 270.);
    }
    
    /**
     * Get the start angle for the Arc Shape.
     * @return the start angle for this Arc
     */
    public double getStartAngle() {
        return this.startAngle;
    }
    
    /**
     * Get the arc angle for the Arc Shape.
     * @return the arc angle for this Arc
     */
    public double getArcAngle() {
        return this.arcAngle;
    }
    
    /**
     * Set the start angle for the Arc Shape.
     * @param angle start angle
     */
    public void setStartAngle(double angle) {
        this.startAngle = angle;
        repaint();
    }
    
    /**
     * Set the arc angle for the Arc Shape.
     * @param angle arc angle
     */
    public void setArcAngle(double angle) {
        this.arcAngle = angle;
        repaint();
    }
    
    @Override
    public String toString() {
        return "Arc x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", layer=" + layer;
    }
    
     /**
     * Draw the shape   
     * @param g         The Graphics2D object on which to draw
     */
    @Override
    public void draw(Graphics2D g)
    {
        // Set the type based on the filled setting
        int typ;
        if (filled == true) {
            typ = java.awt.geom.Arc2D.PIE;
        } else {
            typ = java.awt.geom.Arc2D.OPEN;
        }
        
        // Create the shape
        Arc2D.Double arc = new Arc2D.Double(x, y, width, height, startAngle, arcAngle, typ);
        
        if (filled == true) {
            g.setColor(this.fillColor);
            g.fill( arc );
            //g.fillArc(x, y, width, height, startAngle, arcAngle);
        }
        
        if (stroked == true && strokeWidth > 0.0) {
            g.setColor(this.strokeColor);
            
            float _strokeWidth = (float)strokeWidth;
            //if (selected) _strokeWidth = 2.0f*_strokeWidth;
            g.setStroke( new BasicStroke(_strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND) );
            g.draw( arc );
        }
        
        if (selected) drawSelRect(g);
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
//        if (this.fillColor.getAlpha() > 0)   g.fillArc(x, y, width, height, startAngle, arcAngle);
//        if (this.strokeColor.getAlpha() > 0) g.drawArc(x, y, width, height, startAngle, arcAngle);
//    }
    
    @Override
    public Area getArea() {
        int typ;
        if (filled == true) {
            typ = java.awt.geom.Arc2D.PIE;
        } else {
            typ = java.awt.geom.Arc2D.OPEN;
        }
        return new Area( new java.awt.geom.Arc2D.Double(x, y, width, height, startAngle, arcAngle, typ));
    }
}
