/*
 * Dimension.java
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
 *
 * @author Mark
 */
public class Dimension {
    private double width;
    private double height;
    
    public Dimension(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public Dimension(java.awt.Dimension d) {
        this.width = d.width;
        this.height = d.height;
    }

    public Dimension(java.awt.geom.Dimension2D d) {
        this.width = d.getWidth();
        this.height = d.getHeight();
    }
        
    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    public void setSize(java.awt.Dimension d) {
        this.width = d.width;
        this.height = d.height;
    }

    public void setSize(java.awt.geom.Dimension2D d) {
        this.width = d.getWidth();
        this.height = d.getHeight();
    }
        
    public double getWidth() {
        return this.width;
    }
    
    public void setWidth(double width) {
        this.width = width;
    }
    
    public double getHeight() {
        return this.height;
    }
    
    public void setHeight(double height) {
        this.height = height;
    }
}
