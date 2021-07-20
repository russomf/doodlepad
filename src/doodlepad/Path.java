/*
 * Path.java
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

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

/**
 * A class that implements a general graphical path object.
 * 
 * @author Mark F. Russo, Ph.D.
 * @version 1.1
 */
public class Path extends Shape
{
    // A collection of PathOp objects that define the path
    private final ArrayList<PathOp> ops = new ArrayList<>();
    
    // The internal path object used to draw the path
    private Path2D.Double path;
    
    /**
     * The path operation inner base class
     */
    private class PathOp {
        public double x;
        public double y;
        public PathOp(double x, double y) {
            this.x = x;
            this.y = y;
        }
        public double getX() { return x; }
        public double getY() { return y; }
        public void addOp() { }
        
        // Move the path op location by give increments
        public void move(double dx, double dy) {
            x += dx;
            y += dy;
        }
        
        // Scale the path op point given x and y scale factors
        public void resize(double rw, double rh) {
            this.x = Path.this.x + rw*(this.x - Path.this.x);
            this.y = Path.this.y + rh*(this.y - Path.this.y);
        }
    }
    
    private class MoveTo extends PathOp 
    {
        public MoveTo(double x, double y) {
            super(x, y);
        }
        
        @Override
        public void addOp() {
            path.moveTo(x, y);
        }
    }
    
    private class LineTo extends PathOp 
    {
        public LineTo(double x, double y) {
            super(x, y);
        }
        
        @Override
        public void addOp() {
            path.lineTo(x, y);
        }
    }
    
    private class QuadTo extends PathOp 
    {
        public double cx1;
        public double cy1;
        
        public QuadTo(double cx1, double cy1, double x, double y) {
            super(x, y);
            this.cx1 = cx1;
            this.cy1 = cy1;
        }
        
        @Override
        public void addOp() {
            path.quadTo(cx1, cy1, x, y);
        }
        
        @Override
        public void move(double dx, double dy) {
            super.move(dx, dy);
            cx1 += dx;
            cy1 += dy;
        }
        
        @Override
        public void resize(double rw, double rh) {
            this.cx1 = Path.this.x + rw*(this.cx1 - Path.this.x);
            this.cy1 = Path.this.y + rh*(this.cy1 - Path.this.y);
            super.resize(rw, rh);
        }
    }
    
    private class CurveTo extends PathOp {
        public double cx1;
        public double cy1;
        public double cx2;
        public double cy2;
        
        public CurveTo(double cx1, double cy1, double cx2, double cy2, double x, double y) {
            super(x, y);
            this.cx1 = cx1;
            this.cy1 = cy1;
            this.cx2 = cx2;
            this.cy2 = cy2;
        }
        
        @Override
        public void addOp() {
            path.curveTo(cx1, cy1, cx2, cy2, x, y);
        }
        
        @Override
        public void move(double dx, double dy) {
            super.move(dx, dy);
            cx1 += dx;
            cy1 += dy;
            cx2 += dx;
            cy2 += dy;
        }
        
        @Override
        public void resize(double rw, double rh) {
            this.cx1 = Path.this.x + rw*(this.cx1 - Path.this.x);
            this.cy1 = Path.this.y + rh*(this.cy1 - Path.this.y);
            this.cx2 = Path.this.x + rw*(this.cx2 - Path.this.x);
            this.cy2 = Path.this.y + rh*(this.cy2 - Path.this.y);
            super.resize(rw, rh);
        }
    }
    
    private class ClosePath extends PathOp 
    {
        public ClosePath() {
            super(0,0);
        }
        
        @Override
        public void addOp() {
            path.closePath();
        }
    }
    
    /**
     * Constructor for objects of class Path
     */
    public Path()
    {
        this(Pad.getPad().getLayer(0));
    }
    
    /**
     * Constructor for objects of class Path
     * @param   pad     The Pad to which this object should be added.
     */
    public Path(Pad pad) {
        this(pad.getLayer(0));
    }
    
    /**
     * Constructor for objects of class Path
     * @param   layer   The Layer object to which the Path should be added, or null if not to add to a Layer.
     */
    public Path(Layer layer)
    {
        super(0, 0, 0, 0, layer);
        path = new Path2D.Double();
    }
    
    /**
     * Generate a representation of the Path object.
     * @return String representation
     */
    @Override
    public String toString() {
        return "Path x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", layer=" + layer;
    }
    
    /**
     * Add a moveTo operation to the Path object
     * @param x The x-coordinate of the moveTo point.
     * @param y The y-coordinate of the moveTo point.
     */
    public void moveTo(double x, double y) {
        ops.add( new MoveTo(x, y) );
        rebuildPath();
        updateBoundingBox();
    }
    
    /**
     * Add a line to the Path object
     * @param x The x-coordinate of the line endpoint.
     * @param y The y-coordinate of the line endpoint.
     */
    public void lineTo(double x, double y) {
        ops.add( new LineTo(x, y) );
        rebuildPath();
        updateBoundingBox();
    }
    
    /**
     * Add a quadratic curve to the Path object
     * @param cx1 The x-coordinate of the control point.
     * @param cy1 The y-coordinate of the control point.
     * @param x The x-coordinate of the endpoint.
     * @param y The y-coordinate of the endpoint.
     */
    public void quadTo(double cx1, double cy1, double x, double y) {
        ops.add( new QuadTo(cx1, cy1, x, y) );
        rebuildPath();
        updateBoundingBox();
    }
    
    /**
     * Add a curve to the Path object.
     * @param cx1 The x-coordinate of the first control point.
     * @param cy1 The y-coordinate of the first control point.
     * @param cx2 The x-coordinate of the second control point.
     * @param cy2 The y-coordinate of the second control point.
     * @param x The x-coordinate of the curve endpoint.
     * @param y The y-coordinate of the curve endpoint.
     */
    public void curveTo(double cx1, double cy1, double cx2, double cy2, double x, double y) {
        ops.add( new CurveTo(cx1, cy1, cx2, cy2, x, y) );
        rebuildPath();
        updateBoundingBox();
    }
    
    /**
     * Close the Path object.
     */
    public void closePath() {
        ops.add( new ClosePath() );
        rebuildPath();
    }
    
    /**
     * Recalculate and save the position and size of this shape
     */
    private void updateBoundingBox() 
    {
//        double minX, maxX, minY, maxY;
//        if (ops.isEmpty()) {
//            x = 0;
//            y = 0;
//            width = 0;
//            height = 0;
//            return;
//        }
//        
//        // Init bounds
//        PathOp op = ops.get(0);
//        minX = op.x;
//        maxX = op.x;
//        minY = op.y;
//        maxY = op.y;
//        
//        // Inspect all points and update min and max vals
//        for (int i=1; i<ops.size(); i++) {
//            PathOp o = ops.get(i);
//            if (o.x < minX) {
//                minX = o.x;
//            } else if (o.x > maxX) {
//                maxX = o.x;
//            }
//            if (o.y < minY) {
//                minY = o.y;
//            } else if (o.y > maxY) {
//                maxY = o.y;
//            }
//        }
//        
//        // Reset bounding box fields
//        x = (int)minX;
//        y = (int)minY;
//        width = (int)(maxX - minX);
//        height = (int)(maxY - minY);
        
        // Reset bounding box fields
        Rectangle2D r = path.getBounds2D();
        x = r.getX();
        y = r.getY();
        width = r.getWidth();
        height = r.getHeight();
    }
    
    /**
     * Rebuild and save a new path object 
     */
    private void rebuildPath() {
        // Rebuild the internal path object given the new position
        path = new Path2D.Double();
        for (PathOp op : ops) { 
            op.addOp(); 
        }
    }
    
    /**
     * Update all path operations to reflect new position
     * @param x New x position of upper-left corner
     * @param y New y position of upper-left corner
     */
    @Override
    public void setLocation(double x, double y)
    {
        // Compute the difference between the current and new position
        double dx = x - this.x;
        double dy = y - this.y;
        
        // Tell all path operations to update themselves
        for (PathOp op : ops) { 
            op.move(dx, dy);
        }

        rebuildPath();
        updateBoundingBox();
        super.setLocation(x, y);
    }
    
    /**
     * Update all path operations to reflect new size
     * @param w New width
     * @param h New height
     */
    @Override
    public void setSize(double w, double h)
    {
        // Compute the ratio of the current and new width and height
        double rw = 1.0;
        double rh = 1.0;
        if (this.width != 0.0) rw = w/this.width;
        if (this.height != 0.0) rh = h/this.height;
        
        // Compute new positions for all points
        for (PathOp op : ops) {
            op.resize(rw, rh);
        }

        // Reset everything else
        rebuildPath();
        updateBoundingBox();
        super.setSize(w, h);
    }
    
    /**
     * Draw the Path object
     * @param g The Graphics2D object on which to draw the Path
     */
    @Override
    public void draw(Graphics2D g)
    {
        if (filled == true) {
            g.setColor(this.fillColor);
            g.fill(path);
        }
        
        if (stroked == true && strokeWidth > 0.0) {
            g.setColor(this.strokeColor);
            
            float _strokeWidth = (float)strokeWidth;
            //if (selected) _strokeWidth = 2.0f*_strokeWidth;
            g.setStroke( new BasicStroke(_strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND) );
            
            g.draw(path);
        }
        
        // If text, draw it.
        if (this.text != null) { drawText(g); }

        // If selected, draw select rect.
        if (selected) drawSelRect(g);
    }
}
