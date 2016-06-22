/*
 * ShapeGallery.java
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
import java.util.ArrayList;

/**
 *
 * @author Mark
 */
public class ShapeGallery 
{
    public static void main(String[] args) {
        Arc s1 = new Arc(100, 100, 150, 130, 0.0, 270);
        s1.setDraggable(true);
        
        Line s2 = new Line(200, 200, 250, 100);
        s2.setStrokeColor(120, 0, 0);
        s2.setStrokeWidth(5);
        s2.setDraggable(true);
        
        Oval s3 = new Oval(0, 0, 50, 30);
        s3.setStrokeColor(120, 0, 0);
        s3.setFillColor(0, 255, 255);
        s3.setStrokeWidth(5);
        s3.translate(250, 150);
        s3.setDraggable(true);
        
        Text s4 = new Text("Hello", 100, 200, 32);
        s4.setStrokeColor(120, 0, 0);
        s4.setStrokeWidth(5);
        s4.setDraggable(true);
        
        // Rectangle
        Rectangle rectangle = new Rectangle( 10, 10, 80, 50 );
        rectangle.setFillColor(255, 200, 200);
        rectangle.setDraggable(true);
        
        // RoundRect
        RoundRect roundrect = new RoundRect(110, 10, 80, 50, 30, 30);
        roundrect.setStrokeColor(0, 255, 0);
        roundrect.setDraggable(true);
        
        // Oval
        Oval oval = new Oval(210, 10, 80, 50);
        oval.setStrokeWidth(3);
        oval.setDraggable(true);

        // Arc
        Arc arc = new Arc(310, 10, 80, 50, 10, 270);
        arc.setFillColor(255, 255, 0);
        arc.setDraggable(true);
        arc.rotate(1.0);
        
        // Line
        Line line = new Line(10, 180, 80, 130);
        line.setStrokeColor(0, 0, 255);
        line.setStrokeWidth(5);
        line.setDraggable(true);
        
        // Text
        Text text = new Text("Hello World", 100, 150, 24);
        text.setStrokeColor(0, 127, 127);
        text.setDraggable(true);
        
        // Image
        String currentFolder = System.getProperty("user.dir");
        //System.out.println(currentFolder);
        Image image = new Image(currentFolder + "/beetle.jpg", 200, 110, 80, 80);
        image.setDraggable(true);
        image.move(50, 0);
        
        // Change z-level
        Oval oval1 = new Oval(10, 210, 80, 80);
        oval1.setFillColor( 127, 127, 0);
        Rectangle rect1 = new Rectangle( 50, 250, 80, 80);
        rect1.setFillColor(0, 127, 127);
        
        // Polygon
        ArrayList<Point> pts = new ArrayList<>();
        pts.add( new Point( 250, 220 ) );
        pts.add( new Point( 210, 260 ) );
        pts.add( new Point( 250, 300 ) );
        pts.add( new Point( 290, 260 ) );
        Polygon polygon = new Polygon( pts );
        polygon.setFillColor( 100, 200, 255 );
        polygon.setDraggable( true );
        
        // Path1
        Path path1 = new Path();
        path1.moveTo(220, 150);
        path1.curveTo(240, 130, 280, 160, 300, 140);
        path1.lineTo(300, 230);
        path1.quadTo(260, 210, 220, 230);
        path1.closePath();
        path1.setFillColor(255, 255, 200, 200);
        path1.setDraggable(true);
    }
}
