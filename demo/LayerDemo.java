/*
 * LayerDemo.java
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

import doodlepad.*;

// Create a subclass of Pad
public class LayerDemo extends Pad {
    
    public LayerDemo() {
        super(500, 500);
        
        // Add a new Layer to the PAd
        int l = addLayer();
        Layer lay = getLayer(l);
        
        // Create a rectangle on the new Layer and transform it
        Rectangle r2 = new Rectangle(150, 150, 100, 100, lay);
        r2.setDraggable(true);
        r2.setFillColor(255, 0, 0);
        r2.rotate(0.3);
        
        // Create a red rectangle on the default layer
        Rectangle r1 = new Rectangle(100, 100, 100, 100);
        r1.setDraggable(true);
    }
    
    public static void main(String[] args) {
        new LayerDemo();
    }
}
