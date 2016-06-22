/*
 * SolarSystem.java
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

public class SolarSystem extends Pad
{
    private final Oval sun;
    private final Oval planet1;
    private final Oval planet2;
    private final Oval planet3;
    
    public SolarSystem()
    {
        super(600, 600);
        setBackground(0,0,0);
        
        // Translate entire Pad coordinate system
        translate(300, 300);
        
        // Sun
        sun = new Oval(-20, -20, 40, 40);
        sun.setFillColor(255, 255, 0);
        sun.setStroked(false);
        
        // Planet 1
        planet1 = new Oval(75, 0, 20, 20);
        planet1.setFillColor(0, 127, 255);
        planet1.setStroked(false);
        
        // Planet 2
        planet2 = new Oval(150, 0, 15, 15);
        planet2.setFillColor(127, 127, 0);
        planet2.setStroked(false);
        
        // Planet 3
        planet3 = new Oval(200, 0, 10, 10);
        planet3.setFillColor(127, 0, 0);
        planet3.setStroked(false);
        
        // Set up and start timer
        setTickRate(30);
        startTimer();
    }

    @Override
    public void onTick(long now) {
        // Rotation angles are added to each shape
        planet1.rotate(0.04);
        planet2.rotate(0.03);
        planet3.rotate(0.02);
    }
    
    @Override
    public void onMousePressed(double x, double y, int but) {
        // When mouse is clicked, convert transformed mouse coordinates
        // back to window coordinates.
        // Then reset the Pad transformation and re-translate 
        // diagram so center is at new mouse location.
        Pad p = Pad.getPad();
        double[] pts = p.toWindowCoords(x, y);
        reset();
        translate(pts[0], pts[1]);
    }
    
    public static void main(String[] args) {
        SolarSystem ss = new SolarSystem();
    }
}
