/*
 * DetachedShapeTest.java
 * 
 * Author: Mark F. Russo, Ph.D.
 * Copyright (c) 2012-2018 Mark F. Russo
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

/**
 * Test ability to create shapes not automatically attached to a Pad.
 * Create a Pad object separately and manually attach shape.
 * Remove shape when clicked.
 */
public class DetachedShapeTest {

    // Extend Rectangle class overriding onMouseClicked
    class MyRectangle extends Rectangle 
    {
        public MyRectangle(int x, int y, int w, int h, Layer lay) {
            super(x, y, w, h, lay);
        }

        @Override
        public void onMouseClicked(double x, double y, int btn) {
            this.getPad().removeShape(this);
        }
    }
    
    public DetachedShapeTest() {
        // Create the Shape unattached to a Pad
        MyRectangle r = new MyRectangle(10, 10, 100, 100, null);
        r.setFillColor(255, 0, 0);
        r.setDraggable(true);
        
        // Add to a Pad
        Pad p1 = new Pad(1000, 600);
        p1.setResizable(false);
        p1.setBackground(0,0,0);
        p1.addShape(r);   
    }
    
    public static void main(String[] args) {
        DetachedShapeTest ds = new DetachedShapeTest();
    }
}
