/*
 * PadMouseListener.java
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
 * The interface to be implemented when handling PadMouseListener events.
 * 
 * @author Mark F. Russo, Ph.D.
 * @version 1.0
 */
public interface PadMouseListener
{
    /**
     * The onMousePressed method is invoked by PadMouseListeners
     * @param x         x-position of the mouse when event occurred
     * @param y         y-position of the mouse when event occurred
     * @param button    Integer indicating button pressed during event.
     */
    void onMousePressed(double x, double y, int button);
    
    /**
     * The onMouseReleased method is invoked by PadMouseListeners
     * @param x         x-position of the mouse when event occurred
     * @param y         y-position of the mouse when event occurred
     * @param button    Integer indicating button pressed during event.
     */
    void onMouseReleased(double x, double y, int button);
    
    /**
     * The onMouseMoved method is invoked by PadMouseListeners
     * @param x         x-position of the mouse when event occurred
     * @param y         y-position of the mouse when event occurred
     * @param button    Integer indicating button pressed during event.
     */
    void onMouseMoved(double x, double y, int button);
    
    /**
     * The onMouseDragged method is invoked by PadMouseListeners
     * @param x         x-position of the mouse when event occurred
     * @param y         y-position of the mouse when event occurred
     * @param button    Integer indicating button pressed during event.
     */
    void onMouseDragged(double x, double y, int button);;
    
    /**
     * The onMouseClicked method is invoked by PadMouseListeners
     * @param x         x-position of the mouse when event occurred
     * @param y         y-position of the mouse when event occurred
     * @param button    Integer indicating button pressed during event.
     */
    void onMouseClicked(double x, double y, int button);

    /**
     * The onMouseDoubleClicked method is invoked by PadMouseListeners
     * @param x         x-position of the mouse when event occurred
     * @param y         y-position of the mouse when event occurred
     * @param button    Integer indicating button pressed during event.
     */
    void onMouseDoubleClicked(double x, double y, int button);
    
    /**
     * The onMouseEntered method is invoked by PadMouseListeners
     * @param x         x-position of the mouse when event occurred
     * @param y         y-position of the mouse when event occurred
     * @param button    Integer indicating button pressed during event.
     */
    void onMouseEntered(double x, double y, int button);
    
    /**
     * The onMouseExited method is invoked by PadMouseListeners
     * @param x         x-position of the mouse when event occurred
     * @param y         y-position of the mouse when event occurred
     * @param button    Integer indicating button pressed during event.
     */
    void onMouseExited(double x, double y, int button);
}
