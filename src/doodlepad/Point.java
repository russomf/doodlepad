/*
 * Point.java
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

/**
 * A simple utility Point class.
 * 
 * @author Mark F. Russo, PhD
 * @version 1.0
 */
public class Point {
    private double x;
    private double y;
    
    /**
     * Point object constructor from coordinates
     * @param   x   x-coordinate or Point
     * @param   y   y-coordinate of Point
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Point object constructor from an AWT Point object
     * @param   p   An AWT Point object
     */
    public Point(java.awt.Point p) {
        this.x = p.x;
        this.y = p.y;
    }
    
    /**
     * Point object constructor from an AWT Point2D object
     * @param p     An AWT Point@D object
     */
    public Point(java.awt.geom.Point2D.Double p) {
        this.x = p.x;
        this.y = p.y;
    }

    /**
     * Update the coordinates stored by the Point object from coordinates.
     * @param   x   The new Point object x-coordinate value.
     * @param   y   The new Point object y-coordinate value.
     */
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Update the coordinates stored by the Point object from an AWT Point object.
     * @param   p   An AWT Point object
     */
    public void setLocation(java.awt.Point p) {
        this.x = p.x;
        this.y = p.y;
    }
    
    /**
     * Update the coordinates stored by the Point object from an AWT Point2D object.
     * @param   p   An AWT Point2D object
     */
    public void setLocation(java.awt.geom.Point2D.Double p) {
        this.x = p.x;
        this.y = p.y;
    }
    
    /**
     * Gets the Point object's current x-coordinate.
     * @return x-coordinate value.
     */
    public double getX() {
        return this.x;
    }
    
    /**
     * Sets the Point object's current x-coordinate.
     * @param x The new x-coordinate value.
     */
    public void setX(double x) {
        this.x = x;
    }
    
    /**
     * Gets the Point object's current y-coordinate.
     * @return y-coordinate value.
     */
    public double getY() {
        return this.y;
    }
    
    /**
     * Sets the Point object's current y-coordinate.
     * @param y The new y-coordinate value.
     */
    public void setY(double y) {
        this.y = y;
    }
    
    /**
     * Generate a representation of the Point object.
     * @return String representation
     */
    @Override
    public String toString() {
        return "Point x=" + x + ", y=" + y;
    }
}
