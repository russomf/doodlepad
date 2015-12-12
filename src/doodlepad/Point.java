/*
 * Point.java
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

/**
 * A simple Point class
 * @author Mark
 */
public class Point {
    private double x;
    private double y;
    
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public Point(java.awt.Point p) {
        this.x = p.x;
        this.y = p.y;
    }
    
    public Point(java.awt.geom.Point2D.Double p) {
        this.x = p.x;
        this.y = p.y;
    }

    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void setLocation(java.awt.Point p) {
        this.x = p.x;
        this.y = p.y;
    }
    
    public void setLocation(java.awt.geom.Point2D.Double p) {
        this.x = p.x;
        this.y = p.y;
    }
    
    public double getX() {
        return this.x;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public void setY(double y) {
        this.y = y;
    }
}
