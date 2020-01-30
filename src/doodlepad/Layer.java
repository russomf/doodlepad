/*
 * Layer.java
 * 
 * Author: Mark F. Russo, Ph.D.
 * Copyright (c) 2012-2020 Mark F. Russo
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

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * An object representing a drawing layer for the Pad class.
 * Each Layer maintains its own collection of Shapes.
 * 
 * @author Mark F. Russo, PhD
 * @version 1.0
 */
public class Layer implements Iterable<Shape>
{
    /**
     * Master list of Shape objects being managed by the Pad
     */
    private final java.util.List<Shape> shapes = new ArrayList<>();
    
    /**
     * The current affine transform to be used when drawing all Shapes on this Pad
     */
    AffineTransform transform;
    
    /**
     * The Pad that holds this Layer
     */
    private Pad pad = null;
    
    /**
     * Layer constructor
     * @param pad   The Pad that holds this Layer
     */
    public Layer(Pad pad) {
        // Save a reference to the Pad object that contains this Layer
        this.pad = pad;
        
        // Init the Pad's transform
        this.transform = new AffineTransform();
    }
    
    /**
     * Return the Pad that holds this layer
     * @return Pad
     */
    public Pad getPad() {
        return pad;
    }
    
    /**
     * Clear the Pad reference
     */
    public void clearPad() {
        this.pad = null;
    }
    
    /**
     * Return an ArrayList of Shape objects currently managed by the Pad instance
     * @return An ArrayList&lt;Shape&gt; of all Shapes being managed
     */
    public ArrayList<Shape> getShapes() {
        return (ArrayList<Shape>)this.shapes;
    }
    
    /**
     * Return an ArrayList of selected Shape objects currently managed by the Pad instance
     * @return An ArrayList&lt;Shape&gt; of all selected Shapes being managed
     */
    public ArrayList<Shape> getSelectedShapes() {
        ArrayList<Shape> sel = new ArrayList<>();
        for (Shape s : shapes) {
            if (s.getSelected() == true) {
                sel.add(s);
            }
        }
        return sel;
    }
    
    /**
     * Return an iterator for loop over Shapes
     * @return Iterator&lt;Shape&gt; for Shapes
     */
    @Override
    public Iterator<Shape> iterator() {
        return shapes.iterator();
    }
    
    /**
     * Add a shape to the shapes list
     * @param s The Shape object to add to the Pad
     */
    public void addShape(Shape s)
    {
        shapes.add(s);
        s.layer = this;
        repaint();
    }
    
    /**
     * Remove a shape from the shapes list
     * @param s The Shape to remove
     */
    public void removeShape(Shape s)
    {
        s.layer = null;
        shapes.remove(s);
        repaint();
    }
    
    /**
     * Remove all shapes from the DoodlePad
     */
    public void clear() {
        shapes.clear();
        repaint();
    }
    
    /**
     * Deselect all Shapes
     */
    void deselectAll() {
        for (int i=0; i<shapes.size(); i++) {
            shapes.get(i).setSelected(false);
        }
    }
    
    /**
     * Move shape to the front of diagram
     * @param s The Shape to bring to the front of the display list.
     */
    public void toFront(Shape s) {
        shapes.remove(s);
        shapes.add(s);
    }
    
    /**
     * Move shape to the back of diagram
     * @param s The Shape to move to the back of the display list.
     */
    public void toBack(Shape s)
    {
        shapes.remove(s);
        shapes.add(0, s);
    }
    
    /**
     * Add a rotation angle to Layer transform.
     * @param angle Adds the rotation angle to the current transform (radians)
     */
    public void rotate(double angle) {
        transform.rotate(angle);
        repaint();
    }
    
    /**
     * Add a rotation angle to Layer transform.
     * @param angle Adds the rotation angle to the current transform (degrees)
     * @param cx x-coordinate of point about which rotation occurs
     * @param cy y-coordinate of point about which rotation occurs
     */
    public void rotate(double angle, double cx, double cy) {
    	double radians = angle*Math.PI/180.0;
    	double cosa = Math.cos(radians);
    	double sina = Math.sin(radians);
    	AffineTransform Tx = new AffineTransform(cosa, sina, -sina, cosa, cx-cx*cosa+cy*sina, cy-cx*sina-cy*cosa);
    	transform.concatenate(Tx);
        repaint();
    }
    
    /**
     * Add a translate to Layer transform.
     * @param deltaX Translate shape in the x-direction by deltaX
     * @param deltaY Translate shape in the y-direction by deltaY
     */
    public void translate(double deltaX, double deltaY) {
        transform.translate(deltaX, deltaY);
        repaint();
    }
    
    /**
     * Add a scale factor to Layer transform.
     * @param factor Scale the shape by a scale factor
     */
    public void scale(double factor) {
        transform.scale(factor, factor);
        repaint();
    }

    /**
     * Add a scale factor to Layer transform.
     * @param factor Scale the shape by a scale factor
     * @param cx x-coordinate of point about which scaling occurs
     * @param cy y-coordinate of point about which scaling occurs
     */
    public void scale(double factor, double cx, double cy) {
    	AffineTransform Tx = new AffineTransform(factor, 0, 0, factor, cx-factor*cx, cy-factor*cy);
    	transform.concatenate(Tx);
        repaint();
    }
    
    /**
     * Add a scale factor to Layer transform.
     * @param xFactor Scale the shape in the x-direction by a xFactor
     * @param yFactor Scale the shape in the y-direction by a yFactor

     */
    public void scale(double xFactor, double yFactor) {
        transform.scale(xFactor, yFactor);
        repaint();
    }
    
    /**
     * Add a scale factor to Layer transform.
     * @param xFactor Scale the shape in the x-direction by a xFactor
     * @param yFactor Scale the shape in the y-direction by a yFactor
     * @param cx x-coordinate of point about which scaling occurs
     * @param cy y-coordinate of point about which scaling occurs
     */
    public void scale(double xFactor, double yFactor, double cx, double cy) {
    	AffineTransform Tx = new AffineTransform(xFactor, 0, 0, yFactor, cx-xFactor*cx, cy-yFactor*cy);
    	transform.concatenate(Tx);
        repaint();
    }
    
    /**
     * Resets the Layer to have no transformation.
     */
    public void reset() {
        transform.setToIdentity();
        repaint();
    }
    
    /**
     * Clone the Layer's current AffineTransform.
     * @return The cloned AffineTransform.
     */
    public AffineTransform getTransform() {
        return (AffineTransform)transform.clone();
    }
    
    /**
     * Set a new AffineTransform for the Layer.
     * @param transform The new transform.
     */
    public void setTransform(AffineTransform transform) {
        this.transform = transform;
    }
    
    /**
     * Repaint Layer by delegating to parent Pad object.
     */
    public void repaint() {
        pad.repaint();
    }
}
