/*
 * MultiPadTest.java
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

public class MultiPadDemo {
    public static void main(String[] args) {
        Pad p1 = new Pad("Pad 1", 300, 300, false);
        Pad p2 = new Pad("Pad 2", 300, 300, false);
        
        Oval o1 = new Oval(10., 10., 30., 30., p1.getLayer(0));
        Rectangle r1 = new Rectangle(10, 10, 30, 30, p2.getLayer(0));
        
        r1.setDraggable(true);
        o1.setDraggable(true);
    }
}
