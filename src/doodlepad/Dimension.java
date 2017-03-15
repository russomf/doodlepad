/*
 * Dimension.java
 * 
 * Author: Mark F. Russo, Ph.D.
 * Copyright (c) 2012-2017 Mark F. Russo
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
 * A class that encapsulates width and height parameters.
 * 
 * @author Mark F. Russo, Ph.D.
 * @version 1.0
 */
public class Dimension {
    private double width;
    private double height;
    
    /**
     * Creates a new Dimension object encapsulating width and height parameters.
     * @param width     A width value
     * @param height    A height value
     */
    public Dimension(double width, double height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Create a DoodlePad Dimension object from a Java Dimension object.
     * @param d     A native java.awt.Dimension object
     */
    public Dimension(java.awt.Dimension d) {
        this.width = d.width;
        this.height = d.height;
    }

    /**
     * Create a DoodlePad Dimension object from a Java Dimension2D object.
     * @param d     A native java.awt.geom.Dimension object
     */
    public Dimension(java.awt.geom.Dimension2D d) {
        this.width = d.getWidth();
        this.height = d.getHeight();
    }
    
    /**
     * Sets the width and height values of a Dimension object
     * @param width     new width of the Dimension
     * @param height    new height of the Dimension
     */
    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    /**
     * Sets the width and height values of a Dimension object with a native Java Dimension object
     * @param d     java.awt.Dimension object
     */
    public void setSize(java.awt.Dimension d) {
        this.width = d.width;
        this.height = d.height;
    }

    /**
     * Sets the width and height values of a Dimension object with a native Java Dimension2D object
     * @param d     java.awt.Dimension2D object
     */
    public void setSize(java.awt.geom.Dimension2D d) {
        this.width = d.getWidth();
        this.height = d.getHeight();
    }
    
    /**
     * Returns the width of the Dimension object
     * @return Dimension width
     */
    public double getWidth() {
        return this.width;
    }
    
    /**
     * Sets the width of the Dimension object
     * @param width of the Dimension
     */
    public void setWidth(double width) {
        this.width = width;
    }
    
    /**
     * Returns the height of the Dimension object
     * @return Dimension height
     */
    public double getHeight() {
        return this.height;
    }
    
    /**
     * Sets the height of the Dimension object
     * @param height Dimension height
     */
    public void setHeight(double height) {
        this.height = height;
    }
}
