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
import doodlepad.easing.*;

public class EasingDemo extends Pad
{
    private final Rectangle[] rect;
    private final String[] names;
    
    private boolean easing = false;
    private double t = 0.0;
    
    public static void main(String[] args) {
        EasingDemo e = new EasingDemo();
    }
    
    public EasingDemo()
    {
        super(550, 650);
        
        // Array of Rectangles
        rect = new Rectangle[11];
        
        // Array of names
        names = new String[] {"Linear", "Quad", "Cubic", "Quart", "Quint", "Circ", "Sine", "Expo", "Back", "Bounce", "Elastic"};
         
        double y = 50.0;
        for (int i=0; i<11; i++) {
            rect[i] = new Rectangle(50.0, y, 40.0, 40.0);
            new Text(names[i], 250.0, y+3.0, 24);
            y += 50.0;
        }
        
        setTickRate(100);
    }
    
    public void resetEasing() {
        stopTimer();
        t = 0.0;
        for (int i=0; i<11; i++) {
            rect[i].setX(50.0);
        }
    }
    
    @Override
    public void onTick(long now) {
        if (easing == false) return;
        
        double x;
        int i = 0;
        
        // Linear
        x = Linear.easeInOut(t, 50, 400, 100);
        rect[i].setX(x);
        i++;
        
        // Quadratic
        x = Quad.easeInOut(t, 50, 400, 100);
        rect[i].setX(x);
        i++;
        
        // Cubic
        x = Cubic.easeInOut(t, 50, 400, 100);
        rect[i].setX(x);
        i++;
        
        // Quartic
        x = Quart.easeInOut(t, 50, 400, 100);
        rect[i].setX(x);
        i++;
        
        // Quintic
        x = Quint.easeInOut(t, 50, 400, 100);
        rect[i].setX(x);
        i++;
        
        // Circular
        x = Circ.easeInOut(t, 50, 400, 100);
        rect[i].setX(x);
        i++;
                
        // Sine
        x = Sine.easeInOut(t, 50, 400, 100);
        rect[i].setX(x);
        i++;
        
        // Exponential
        x = Expo.easeInOut(t, 50, 400, 100);
        rect[i].setX(x);
        i++;
        
        // Back
        x = Back.easeInOut(t, 50, 400, 100);
        rect[i].setX(x);
        i++;
        
        // Bounce
        x = Bounce.easeOut(t, 50, 400, 100);
        rect[i].setX(x);
        i++;
        
        // Elastic
        x = Elastic.easeInOut(t, 50, 400, 100);
        rect[i].setX(x);
        i++;
        
        // Increment time and constrain
        t = t + 1.0;
        if (t > 100.0) {
            t = 100.0;
            stopTimer();
        }
    }
    
    @Override
    public void onMousePressed(double x, double y, int button) {
        easing = !easing;
        if (!easing) {
            resetEasing();
        } else {
            startTimer();
        }
    }
}