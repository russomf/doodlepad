/*
 * ShapeMouseListener.java
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
 * The interface to be implemented when handling ShapeMouseListener events.
 * 
 * @author Mark F. Russo, Ph.D.
 * @version 1.0
 */
public interface ShapeMouseListener
{
    /**
     * Get access to the implementing Shape
     * @return The Shape that implements the interface.
     */
    Shape getShape();
    
    /**
     * The mousePressed method is invoked by ShapeMouseListener
     * @param x         x-coordinate of event
     * @param y         y-coordinate of event
     * @param button    button pressed during event
     */
    void mousePressed(double x, double y, int button);
    
    /**
     * The mouseReleased method is invoked by ShapeMouseListener
     * @param x         x-coordinate of event
     * @param y         y-coordinate of event
     * @param button    button pressed during event
     */
    void mouseReleased(double x, double y, int button);
    
    /**
     * The mouseMoved method is invoked by ShapeMouseListener
     * @param x         x-coordinate of event
     * @param y         y-coordinate of event
     * @param button    button pressed during event
     */
    void mouseMoved(double x, double y, int button);
    
    /**
     * The mouseDragged method is invoked by ShapeMouseListener
     * @param x         x-coordinate of event
     * @param y         y-coordinate of event
     * @param button    button pressed during event
     */
    void mouseDragged(double x, double y, int button);
    
    /**
     * The mouseClicked method is invoked by PadMouseListeners
     * @param x         x-coordinate of event
     * @param y         y-coordinate of event
     * @param button    button pressed during event
     */
    void mouseClicked(double x, double y, int button);
    
    /**
     * The mouseDoubleClicked method is invoked by PadMouseListeners
     * @param x         x-coordinate of event
     * @param y         y-coordinate of event
     * @param button    button pressed during event
     */
    void mouseDoubleClicked(double x, double y, int button);
    
    /**
     * The mouseEntered method is invoked by PadMouseListeners
     * @param x         x-coordinate of event
     * @param y         y-coordinate of event
     * @param button    button pressed during event
     */
    void mouseEntered(double x, double y, int button);
    
    /**
     * The mouseExited method is invoked by PadMouseListeners
     * @param x         x-coordinate of event
     * @param y         y-coordinate of event
     * @param button    button pressed during event
     */
    void mouseExited(double x, double y, int button);
}
