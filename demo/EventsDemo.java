/*
 * EasingDemo.java
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

public class EventsDemo
{
    public EventsDemo() {
        EventsPad ep = new EventsPad();
        EventsRect er1 = new EventsRect(100, 100, ep);
        EventsRect er2 = new EventsRect(150, 150, ep);
    }
    
    public class EventsPad extends Pad
    {
        public EventsPad() {
            super(400, 400);
        }

        @Override
        public void onMouseEntered(double x, double y, int b) {
            System.out.println("Pad.onMouseEntered x=" + x + " y=" + y + "b=" + b);
        }

        @Override
        public void onMouseExited(double x, double y, int b) {
            System.out.println("Pad.onMouseExited x=" + x + " y=" + y + "b=" + b);
        }
        
    //     @Override
    //     public void onMousePressed(double x, double y, int b) {
    //         System.out.println("Pad.onMousePressed x=" + x + " y=" + y + "b=" + b);
    //     }
    //     
    //     @Override
    //     public void onMouseMoved(double x, double y, int b) {
    //         System.out.println("Pad.onMouseMoved x=" + x + " y=" + y + "b=" + b);
    //     }
    }

    public class EventsRect extends Rectangle
    {
        public EventsRect(double x, double y, Pad pad) {
            super(x, y, 100, 75, pad.getLayer(0));
            translate(-50, 50);
            //setDraggable(true);
        }

        @Override
        public void onMouseEntered(double x, double y, int b) {
            System.out.println("onMouseEntered x=" + x + " y=" + y + "b=" + b);
        }

        @Override
        public void onMouseExited(double x, double y, int b) {
            System.out.println("onMouseExited x=" + x + " y=" + y + "b=" + b);
        }

    //     @Override
    //     public void onMousePressed(double x, double y, int b) {
    //         System.out.println("Shape.onMousePressed x=" + x + " y=" + y + "b=" + b);
    //         double[] pts = this.toWindowCoords(x, y);
    //         System.out.println("Pad.onMousePressed x=" + pts[0] + " y=" + pts[1] + "b=" + b);
    //     }
    //     
    //     @Override
    //     public void onMouseReleased(double x, double y, int b) {
    //         System.out.println("onMouseReleased x=" + x + " y=" + y + "b=" + b);
    //     }
    //     
    //     @Override
    //     public void onMouseClicked(double x, double y, int b) {
    //         System.out.println("onMouseClicked x=" + x + " y=" + y + "b=" + b);
    //     }
    //     
    //     @Override
    //     public void onMouseDoubleClicked(double x, double y, int b) {
    //         System.out.println("onMouseDoubleClicked x=" + x + " y=" + y + "b=" + b);
    //     }
    //     
    //     @Override
    //     public void onMouseMoved(double x, double y, int b) {
    //         System.out.println("onMouseMoved x=" + x + " y=" + y + "b=" + b);
    //     }
    //     
    //     @Override
    //     public void onMouseDragged(double x, double y, int b) {
    //         System.out.println("onMouseDragged x=" + x + " y=" + y + "b=" + b);
    //     }
    }
    
    public static void main(String[] args) {
        EventsDemo et = new EventsDemo();
    }
}
