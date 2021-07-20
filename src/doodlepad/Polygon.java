/*
 * Polygon.java
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
import java.awt.geom.Path2D;
import java.util.Arrays;
import java.util.List;

/**
 * A class that implements a general graphical polygon object made up of straight lines
 * 
 * @author Mark F. Russo, Ph.D.
 * @version 1.0
 */
public class Polygon extends Shape
{
    private double[] xPoints;
    private double[] yPoints;
    private int nPoints = 0;
    
    // The internal path object used to draw the path
    private Path2D.Double path;
    
    /**
     * Constructor for objects of class Polygon - int arrays
     * @param xPoints The array of x-coordinates for all Polygon object points.
     * @param yPoints The array of y-coordinates for all Polygon object points.  
     */
    public Polygon(int[] xPoints, int[] yPoints)
    {
        super(0.0, 0.0, 0.0, 0.0, Pad.getPad().getLayer(0));

        double[] dxPts = new double[xPoints.length];
        double[] dyPts = new double[yPoints.length];
        
        for(int i=0; i<xPoints.length; i++) {
            dxPts[i] = xPoints[i];
        }
        
        for(int i=0; i<yPoints.length; i++) {
            dyPts[i] = yPoints[i];
        }
        
        // Fill the internal arrays with all points
        nPoints = xPoints.length;
        this.xPoints = Arrays.copyOf(dxPts, nPoints);
        this.yPoints = Arrays.copyOf(dyPts, nPoints);
        
        // Update shape
        this.rebuildPath();
        this.updateBoundingBox();
    }

    /**
     * Constructor for objects of class Polygon - double arrays
     * @param xPoints The array of x-coordinates for all Polygon object points.
     * @param yPoints The array of y-coordinates for all Polygon object points.
     */
    public Polygon(double[] xPoints, double[] yPoints)
    {
        this(xPoints, yPoints, Pad.getPad());
    }

    /**
     * Constructor for objects of class Polygon - double arrays and Pad
     * @param xPoints The array of x-coordinates for all Polygon object points.
     * @param yPoints The array of y-coordinates for all Polygon object points.
     * @param pad    The Pad to which the Polygon will be added.
     */
    public Polygon(double[] xPoints, double[] yPoints, Pad pad) {
        this( xPoints, yPoints, pad.getLayer(0));
    }
    
    /**
     * Constructor for objects of class Polygon - double arrays and Layer
     * @param xPoints The array of x-coordinates for all Polygon object points.
     * @param yPoints The array of y-coordinates for all Polygon object points.
     * @param layer   The Layer object to which the Polygon will be added, or null if not to add to a Pad.
     */
    public Polygon(double[] xPoints, double[] yPoints, Layer layer)
    {
        super(0, 0, 0, 0, layer);
        
        // Fill the internal arrays with all points
        nPoints = xPoints.length;
        this.xPoints = Arrays.copyOf(xPoints, nPoints);
        this.yPoints = Arrays.copyOf(yPoints, nPoints);
        
        // Update shape
        this.rebuildPath();
        this.updateBoundingBox();
    }
   
    /**
     * Constructor for objects of class Polygon
     * @param points List of Point objects that define coordinate points of Polygon
     */
    public Polygon( List<Point> points ) {
        this( points, Pad.getPad().getLayer(0));
    }
    
    /**
     * Constructor for objects of class Polygon
     * @param points List of Point objects that define coordinate points of Polygon
     * @param pad    The Pad to which the Polygon will be added.
     */
    public Polygon(List<Point> points, Pad pad) {
        this( points, pad.getLayer(0));
    }
    
    /**
     * Constructor for objects of class Polygon
     * @param   points  List of Point objects that define coordinate points of Polygon
     * @param   layer   The Layer object to which the Polygon will be added, or null if not to add to a Pad.
     */
    public Polygon( List<Point> points, Layer layer ) {
        super(0, 0, 0, 0, layer);
        
        // Compute the number of points in the Polygon.
        nPoints = points.size();

        // Create and fill arrays
        this.xPoints = new double[nPoints];
        this.yPoints = new double[nPoints];
        
        if (nPoints > 0) {
            for (int i=0; i<nPoints; i++) {
                Point pt = points.get(i);
                this.xPoints[i] = pt.getX();
                this.yPoints[i] = pt.getY();
            }                
        }
        
        // Update shape
        this.rebuildPath();
        this.updateBoundingBox();
    }
    
    /**
     * Generate a representation of the Polygon object.
     * @return String representation
     */
    @Override
    public String toString() {
        return "Polygon x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", layer=" + layer;
    }
    
    /**
     * Rebuild and save a new path object 
     */
    private void rebuildPath() {
        // Rebuild the internal path object given the new position
        path = new Path2D.Double();
        
        if (nPoints == 0) return;
        
        path.moveTo(xPoints[0], yPoints[0]);
        for (int i=1; i<nPoints; i++) {
            path.lineTo(xPoints[i], yPoints[i]);
        }
        path.closePath();
    }
    
    /**
     * Recalculate and save the position and size of this shape
     */
    private void updateBoundingBox() 
    {
        // Compute the number of points in the Polygon.
        nPoints = Math.min(xPoints.length, yPoints.length);
        
        // Do nothing when no points
        if (nPoints == 0) return;
        
        // Start by finding upper left corner and saving as position
        double minX, maxX, minY, maxY;
        minX = maxX = xPoints[0];
        minY = maxY = yPoints[0];

        for (int i=1; i<nPoints; i++) {
            double xx = xPoints[i];
            double yy = yPoints[i];

            // Update bounding box
            if (xx < minX) {
                minX = xx;
            } else if (xx > maxX) {
                maxX = xx;
            }

            if (yy < minY) {
                minY = yy;
            } else if (yy > maxY) {
                maxY = yy;
            }
        }

        // Reset bounding box
        this.x = minX;
        this.y = minY;
        this.width  = maxX - minX;
        this.height = maxY - minY;
    }
    
    /**
     * Set the current position of the polygon and redraw it.
     * @param   x   the x-value of the position
     * @param   y   the y-value of the position
     */
    @Override
    public void setLocation(double x, double y)
    {
        // Adjust points in internal arrays to new offsets
        for (int i=0; i<nPoints; i++) {
            xPoints[i] = xPoints[i] - this.x + x;
            yPoints[i] = yPoints[i] - this.y + y;
        }
        
        // Update shape
        this.rebuildPath();
        this.updateBoundingBox();
        
        super.setLocation(x, y);
    }
    
    /**
     * Update all polygon points to reflect new size
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
        
        // Adjust points in internal arrays to new offsets
        for (int i=0; i<nPoints; i++) {
            xPoints[i] = this.x + rw*(xPoints[i] - this.x);
            yPoints[i] = this.y + rh*(yPoints[i] - this.y);
        }

        // Reset everything else
        rebuildPath();
        updateBoundingBox();
        super.setSize(w, h);
    }
    
    /**
     * Return the x-coordinate of the i<sup>th</sup> polygon point
     * @param i Point number
     * @return x-coordinate of i<sup>th</sup> point
     */
    public double getX(int i) {
        return xPoints[i];
    }
    
    /**
     * Update the x-coordinate of the i<sup>th</sup> polygon point
     * @param i Point number
     * @param x New x-coordinate value for i<sup>th</sup> point
     */
    public void setX(int i, double x) {
        xPoints[i] = x;
        this.rebuildPath();
        this.updateBoundingBox();
    }
    
    /**
     * Return the y-coordinate of the i<sup>th</sup> polygon point
     * @param i point number
     * @return y-coordinate of i<sup>th</sup> point
     */
    public double getY(int i) {
        return yPoints[i];
    }
    
    /**
     * Update the y-coordinate of the i<sup>th</sup> polygon point
     * @param i Point number
     * @param y New y-coordinate value for i<sup>th</sup> point
     */
    public void setY(int i, double y) {
        yPoints[i] = y;
        this.rebuildPath();
        this.updateBoundingBox();
    }
    
    /**
     * Draw the Polygon object
     * @param g         The Graphics2D object on which to draw the Polygon
     */
    @Override
    public void draw(Graphics2D g)
    {
        if (filled == true) {
            g.setColor( fillColor );
            g.fill(path);
        }
        
        if (stroked == true && strokeWidth > 0.0) {
            g.setColor( strokeColor );
            
            float _strokeWidth = (float)strokeWidth;
            g.setStroke( new BasicStroke(_strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND) );
            
            g.draw(path);
        }
        
        // If text, draw it.
        if (this.text != null) { drawText(g); }

        // If selected, draw select rect.
        if (selected) drawSelRect(g);
    }
}
