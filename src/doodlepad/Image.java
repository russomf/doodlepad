/*
 * Image.java
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

import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

/**
 * A class that loads an image from a file and draws it on a Pad.
 * 
 * @author Mark F. Russo, Ph.D.
 * @version 1.0
 */
public class Image extends Shape
{
    private String path;
    private BufferedImage img = null;
    
    /**
     * Constructor for objects of class Image
     * @param   path    Path to image file.
     * @param   x       The x-coordinate of the image upper left corner.
     * @param   y       The y-coordinate of the image upper left corner.
     */
    public Image(String path, double x, double y)
    {
        this(path, x, y, Pad.getPad().getLayer(0));
    }
    
    /**
     * Constructor for objects of class Image
     * @param   path    Path to image file.
     * @param   x       The x-coordinate of the image upper left corner.
     * @param   y       The y-coordinate of the image upper left corner.
     * @param   pad     The Pad to which the Image should be added.
     */
    public Image(String path, double x, double y, Pad pad)
    {
        this(path, x, y, pad.getLayer(0));
    }
    
    /**
     * Constructor for objects of class Image
     * @param   path    Path to image file.
     * @param   x       The x-coordinate of the image upper left corner.
     * @param   y       The y-coordinate of the image upper left corner.
     * @param   layer   The Layer object to which the Image should be added, or null if not to add to a Layer.
     */
    public Image(String path, double x, double y, Layer layer) 
    {
        super(x, y, 0, 0, layer);
        this.path = path;
        
        try {
            img = ImageIO.read(new File(path));
            width = img.getWidth();
            height = img.getHeight();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        super.setStrokeWidth(0.0);
        super.setFillColor(Color.BLACK);
    }
    
    /**
     * Constructor for objects of class Image
     * @param   path    Path to image file.
     * @param   x       The x-coordinate of the image upper left corner.
     * @param   y       The y-coordinate of the image upper left corner.
     * @param   width   The width with which to draw the image.
     * @param   height  The height with which to draw the image.
     */
    public Image(String path, double x, double y, double width, double height)
    {
        this(path, x, y, width, height, Pad.getPad());
    }
    
    /**
     * Constructor for objects of class Image
     * @param   path    Path to image file.
     * @param   x       The x-coordinate of the image upper left corner.
     * @param   y       The y-coordinate of the image upper left corner.
     * @param   width   The width with which to draw the image.
     * @param   height  The height with which to draw the image.
     * @param   pad     The Pad to which this Image should be added.
     */
    public Image(String path, double x, double y, double width, double height, Pad pad) {
        this(path, x, y, width, height, pad.getLayer(0));
    }

    /**
     * Constructor for objects of class Image
     * @param   path    Path to image file.
     * @param   x       The x-coordinate of the image upper left corner.
     * @param   y       The y-coordinate of the image upper left corner.
     * @param   width   The width with which to draw the image.
     * @param   height  The height with which to draw the image.
     * @param   layer   The Layer object to which the Image should be added, or null if not to add to a Pad.
     */
    public Image(String path, double x, double y, double width, double height, Layer layer)
    {
        super(x, y, 0, 0, layer);
        this.path = path;
        
        try {
            img = ImageIO.read(new File(path));
            this.width = width;
            this.height = height;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        super.setStrokeWidth(0.0);
        super.setFillColor(Color.BLACK);
    }

    /**
     * Constructor for creating an empty image
     * @param   x       The x-coordinate of the image upper left corner.
     * @param   y       The y-coordinate of the image upper left corner.
     * @param   width   The width with which to draw the image.
     * @param   height  The height with which to draw the image.
     */
    public Image(double x, double y, double width, double height)
    {
        super(x, y, 0, 0, Pad.getPad().getLayer(0));
        super.setStrokeWidth(0.0);
        super.setFillColor(Color.BLACK);
        this.width = width;
        this.height = height;
        
        img = new BufferedImage((int)width, (int)height, BufferedImage.TYPE_INT_ARGB);
    }
    
    /**
     * Set the color of the pixel at coordinates (x, y)
     * @param   x       The x-coordinate of the image upper left corner.
     * @param   y       The y-coordinate of the image upper left corner.
     * @param   red     The red component of the pixel color [0, 255]
     * @param   green   The green component of the pixel color [0, 255]
     * @param   blue    The blue component of the pixel color [0, 255]
     */
    public void setPixel(int x, int y, int red, int green, int blue) {
        int rgb = new Color(red, green, blue).getRGB();
        img.setRGB(x, y, rgb);
        repaint();
    }
    
    /**
     * Set the color of the pixel at coordinates (x, y)
     * @param   x       The x-coordinate of the image upper left corner.
     * @param   y       The y-coordinate of the image upper left corner.
     * @param   clr     The Color object to use to assign pixel color.
     */
    public void setPixel(int x, int y, Color clr) {
        img.setRGB(x, y, clr.getRGB());
        repaint();
    }

    /**
     * Set the color of the pixel at coordinates (x, y)
     * @param   x       The x-coordinate of the image upper left corner.
     * @param   y       The y-coordinate of the image upper left corner.
     * @param   gray    The gray level of the pixel color [0, 255]
     */
    public void setPixel(int x, int y, int gray) {
        setPixel(x, y, gray, gray, gray);
    }
    
    /**
     * Get the pixel color as a Color object.
     * @param   x   x-coordinate of the pixel.
     * @param   y   y-coordinate of the pixel.
     * @return A Color object encapsulating the pixel color.
     */
    public Color getPixel(int x, int y) {
        return new Color( img.getRGB(x, y) );
    }
    
    /**
     * Get red component of pixel color.
     * @param   x   x-coordinate of the pixel.
     * @param   y   y-coordinate of the pixel.
     * @return pixel red component as an integer in range [0, 255].
     */
    public int getRed(int x, int y) {
        //int rgba = img.getRGB(x, y);
        //return (rgba & 0x00F0) >>> 8;
        return getPixel(x, y).getRed();
    }
    
    /**
     * Get green component of pixel color.
     * @param   x   x-coordinate of the pixel.
     * @param   y   y-coordinate of the pixel.
     * @return pixel green component as an integer in range [0, 255].
     */
    public int getGreen(int x, int y) {
        //int rgba = img.getRGB(x, y);
        //return (rgba & 0x0F00) >>> 16;
        return getPixel(x, y).getGreen();
    }
    
    /**
     * Get blue component of pixel color.
     * @param   x   x-coordinate of the pixel.
     * @param   y   y-coordinate of the pixel.
     * @return pixel blue component as an integer in range [0, 255].
     */
    public int getBlue(int x, int y) {
        //int rgba = img.getRGB(x, y);
        //return (rgba & 0xF000) >>> 24;
        return getPixel(x, y).getBlue();
    }

    /**
     * Get alpha component of pixel color.
     * @param   x   x-coordinate of the pixel.
     * @param   y   y-coordinate of the pixel.
     * @return pixel alpha component as an integer in range [0, 255].
     */
    public int getAlpha(int x, int y) {
        //int rgba = img.getRGB(x, y);
        //return (rgba & 0x000F);
        return getPixel(x, y).getAlpha();
    }
    
    /**
     * Set the background color for the Image 
     * @param red   The background color red component
     * @param green The background color green component
     * @param blue  The background color blue component
     */
    public void setBackground(double red, double green, double blue)
    {
        int r = (int)Math.round(Util.constrain(red, 0, 255));
        int g = (int)Math.round(Util.constrain(green, 0, 255));
        int b = (int)Math.round(Util.constrain(blue, 0, 255));
        
        Graphics2D g2 = img.createGraphics();
        g2.setColor(new Color(r, g, b));
        g2.fillRect(0, 0, (int)width, (int)height);
        
        repaint();
    }

    /**
     * Set the background color for the Image to a gray value
     * @param gray The background gray scale
     */
    public void setBackground(double gray)
    {
        setBackground(gray, gray, gray);
    }
    
    /**
     * Draws an arc on the Image. The arc is defined by a section of an ellipse bounded by the rectangle with upper left
     * corner at (x, y) and size (width, height). The section starts at angle startAngle (degrees) and
     * extends by arcAngle (degrees).
     * 
     * @param   x           The upper x-coordinate of the arc`s related ellipse bounding box (pixels).
     * @param   y           The upper y-coordinate of the arc`s related ellipse bounding box (pixels).
     * @param   width       The width of the arc`s related ellipse (pixels).
     * @param   height      The height of the arc`s related ellipse bounding box (pixels).
     * @param   startAngle  The starting angle at which to begin drawing the arc (degrees).
     * @param   arcAngle    The angular extent of the arc, which defines its length (degrees).
     */
    public void drawArc(double x, double y, double width, double height, double startAngle, double arcAngle) 
    {
        Graphics2D g = img.createGraphics();
        
        // Set the type based on the filled setting
        int typ;
        if (filled == true) {
            typ = java.awt.geom.Arc2D.PIE;
        } else {
            typ = java.awt.geom.Arc2D.OPEN;
        }
        
        // Create the shape
        Arc2D.Double arc = new Arc2D.Double(x, y, width, height, startAngle, arcAngle, typ);
        
        if (filled == true) {
            g.setColor(this.fillColor);
            g.fill( arc );
        }
        
        if (stroked == true && strokeWidth > 0.0) {
            g.setColor(this.strokeColor);
            
            float _strokeWidth = (float)strokeWidth;
            g.setStroke( new BasicStroke(_strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND) );
            g.draw( arc );
        }
        
        repaint();
    }
    
    /**
     * Draw an oval on the Image.
     * @param   x       The x-coordinate of the upper left-hand corner of the oval.
     * @param   y       The y-coordinate of the upper left-hand corner of the oval.
     * @param   width   The width of the oval.
     * @param   height  The height of the oval.
     */
    public void drawOval(double x, double y, double width, double height) 
    {
        Graphics2D g = img.createGraphics();
        
        // Create the shape
        Ellipse2D.Double ellipse = new Ellipse2D.Double(x, y, width, height);
        
        if (filled == true) {
            g.setColor(this.fillColor);
            g.fill( ellipse );
        }
        
        if (stroked == true && strokeWidth > 0.0) {
            g.setColor(this.strokeColor);
            
            float _strokeWidth = (float)strokeWidth;
            g.setStroke( new BasicStroke(_strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND) );
            g.draw(ellipse);
        }

        repaint();
    }
    
    /**
     * Draw a rectangle on the Image.
     * @param   x       The x-coordinate of the upper left-hand corner of the rectangle.
     * @param   y       The y-coordinate of the upper left-hand corner of the rectangle.
     * @param   width   The width of the rectangle.
     * @param   height  The height of the rectangle.
     */
    public void drawRectangle(double x, double y, double width, double height) 
    {
        Graphics2D g = img.createGraphics();
        
        Rectangle2D.Double rect = new Rectangle2D.Double(x, y, width, height);
        
        if (filled == true) {
            g.setColor(fillColor);
            g.fill( rect);
        }
        
        if (stroked == true && strokeWidth > 0.0) {
            g.setColor(strokeColor);
            
            float _strokeWidth = (float)strokeWidth;
            g.setStroke( new BasicStroke(_strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND) );
            g.draw( rect );
        }

        repaint();
    }

    /**
     * Draw a line on the Image.
     * @param   x1  The x-coordinate of the line's first point.
     * @param   y1  The y-coordinate of the line's first point.
     * @param   x2  The x-coordinate of the line's second point.
     * @param   y2  The y-coordinate of the line's second point.
     */
    public void drawLine(double x1, double y1, double x2, double y2) 
    {
        Graphics2D g = img.createGraphics();
        
        if (stroked == true && strokeWidth > 0.0) {
            g.setColor(this.strokeColor);

            float _strokeWidth = (float)strokeWidth;
            //if (selected) _strokeWidth = 2.0f*_strokeWidth;
            g.setStroke( new BasicStroke(_strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND) );
            
            g.draw( new Line2D.Double(x1, y1, x2, y2));
        }
        
        repaint();
    }
    
    /**
     * Draw a rounded rectangle on the Image.
     * @param   x           The x-coordinate of the upper left corner of the rounded rectangle.
     * @param   y           The y-coordinate of the upper left corner of the rounded rectangle.
     * @param   width       The width of the rounded rectangle.
     * @param   height      The height of the rounded rectangle.
     * @param   arcWidth    The width of the arc that forms a corner of the rounded rectangle.
     * @param   arcHeight   The height of the arc that forms a corner of the rounded rectangle.
     */
    public void drawRoundRect(double x, double y, double width, double height, double arcWidth, double arcHeight) 
    {
        Graphics2D g = img.createGraphics();
        
        RoundRectangle2D.Double rect = new RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight);
        if (filled == true) {
            g.setColor(this.fillColor);
            g.fill( rect );
        }
        
        if (stroked == true && strokeWidth > 0.0) {
            g.setColor(this.strokeColor);
            
            float _strokeWidth = (float)strokeWidth;
            //if (selected) _strokeWidth = 2.0f*_strokeWidth;
            g.setStroke( new BasicStroke(_strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND) );
            
            g.draw( rect );
        }
        
        repaint();
    }

    /**
     * Draw a polygon on the Image
     * @param   points  List of Point objects that define coordinate points of polygon
     */
    public void drawPolygon( List<Point> points )
    {
        // Compute the number of points in the Polygon.
        int nPoints = points.size();

        // Create and fill arrays
        double[] xPoints = new double[nPoints];
        double[] yPoints = new double[nPoints];
        
        if (nPoints > 0) {
            for (int i=0; i<nPoints; i++) {
                Point pt = points.get(i);
                xPoints[i] = pt.getX();
                yPoints[i] = pt.getY();
            }                
        }

        this.drawPolygon(xPoints, yPoints);
    }
    
    /**
     * Draw a polygon on the Image
     * @param xPoints The array of x-coordinates for all polygon points.
     * @param yPoints The array of y-coordinates for all polygon points.
     */
    public void drawPolygon(double[] xPoints, double[] yPoints)
    {
        // Test the number of points
        if (xPoints.length == 0 || yPoints.length == 0) return;
        int nPoints = xPoints.length;
        
        // Build a path object
        Path2D.Double _path = new Path2D.Double();
        
        _path.moveTo(xPoints[0], yPoints[0]);
        for (int i=1; i<nPoints; i++) {
            _path.lineTo(xPoints[i], yPoints[i]);
        }
        _path.closePath();
        
        Graphics2D g = img.createGraphics();
        
        if (filled == true) {
            g.setColor( fillColor );
            g.fill(_path);
        }
        
        if (stroked == true && strokeWidth > 0.0) {
            g.setColor( strokeColor );
            
            float _strokeWidth = (float)strokeWidth;
            g.setStroke( new BasicStroke(_strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND) );
            
            g.draw(_path);
        }
        
        repaint();
    }

    /**
     * Draw text on the Image.
     * @param text      The String drawn as the Text object
     * @param x         The x-coordinate of the Text object upper left corner.
     * @param y         The y-coordinate of the Text object upper left corner.
     * @param size      The font size used to draw the Text object.
     * @param style     The Font class constant that defines the style used to draw the Text object. Example: Font.PLAIN
     * @param fontName  The name of the font used to draw the Text object, Example: "Arial"
     */
    public void drawText(String text, double x, double y, int size, int style, String fontName) 
    {
        if (stroked == false && filled == false) return;
        if (text.isEmpty()) return;

        Graphics2D g = img.createGraphics();
        
        Font font = new Font(fontName, style, size);
        FontRenderContext frc = g.getFontRenderContext();
        TextLayout tl = new TextLayout(text, font, frc);

        // Stash the size and offset of the rendered text
        Rectangle2D bounds = tl.getBounds();
        double height = bounds.getHeight();
        
        // Fill the text
        if (filled == true) {
            g.setColor( fillColor );                        
            tl.draw(g, (float)x, (float)(y+height));
        }
        
        // Stroke the text
        if (stroked == true && strokeWidth > 0.0) {
            g.setColor( strokeColor );
            float _strokeWidth = (float)strokeWidth;
            g.setStroke( new BasicStroke(_strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND) );

            //AffineTransform tx = new AffineTransform();
            //tx.translate(x, y+height);
            AffineTransform tx = new AffineTransform(1.0, 0.0, 0.0, 1.0, x, y+height);
            g.draw(tl.getOutline(tx));
        }
        
        repaint();
    }
 
    /**
     * Draw text on an Image with some default font attributes.
     * @param   text        The String drawn as the Text object
     * @param   x           The x-coordinate of the Text object upper left corner.
     * @param   y           The y-coordinate of the Text object upper left corner.
     * @param   size        The font size used to draw the Text object.
     * @param   fontName    The name of the font used to draw the Text object, Example: "Arial"
     */
    public void drawText(String text, double x, double y, int size, String fontName) {
        this.drawText(text, x, y, size, Font.PLAIN, fontName);
    }

    /**
     * Draw text on an Image with some default font attributes.
     * @param   text        The String drawn as the Text object
     * @param   x           The x-coordinate of the Text object upper left corner.
     * @param   y           The y-coordinate of the Text object upper left corner.
     * @param   fontName    The name of the font used to draw the Text object, Example: "Arial"
     */
    public void drawText(String text, double x, double y, String fontName) {
        this.drawText(text, x, y, 12, Font.PLAIN, fontName);
    }

    /**
     * Draw text on an Image with some default font attributes.
     * @param   text    The String drawn as the Text object
     * @param   x       The x-coordinate of the Text object upper left corner.
     * @param   y       The y-coordinate of the Text object upper left corner.
     * @param   size    The font size used to draw the Text object.
     * @param   style   The Font class constant that defines the style used to draw the Text object. Example: Font.PLAIN
     */
    public void drawText(String text, double x, double y, int size, int style) {
        this.drawText(text, x, y, size, style, "Arial");
    }
    
    /**
     * Draw text on an Image with some default font attributes.
     * @param   text    The String drawn as the Text object
     * @param   x       The x-coordinate of the Text object upper left corner.
     * @param   y       The y-coordinate of the Text object upper left corner.
     * @param   size    The font size used to draw the Text object.
     */
    public void drawText(String text, double x, double y, int size) {
        this.drawText(text, x, y, size, Font.PLAIN, "Arial");
    }
    
    /**
     * Draw text on an Image with the default font attributes.
     * @param   text    The String drawn as the Text object
     * @param   x       The x-coordinate of the Text object upper left corner.
     * @param   y       The y-coordinate of the Text object upper left corner.
     */
    public void drawText(String text, double x, double y) {
        this.drawText(text, x, y, 12, Font.PLAIN, "Arial");
    }
    
    /**
     * A no-op to prevent setting text for this Shape.
     */
    public void setText(String text) { }
    
    /**
     * Generate a representation of the Image object.
     * @return String representation
     */
    @Override
    public String toString() {
        return "Image x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", path=" + path + ", layer=" + layer;
    }
    
    /**
     * Draw the shape   
     * @param g         The Graphics2D object on which to draw the image
     */
    @Override
    public void draw(Graphics2D g)
    {
        int ix = (int)Math.round(x);
        int iy = (int)Math.round(y);
        int iw = (int)Math.round(width);
        int ih = (int)Math.round(height);
        g.drawImage(img, ix, iy, iw, ih, null);
        
        // Images do not have an stroked outline.
        // Stroke is reserved for graphics drawn on an Image.
        
//        if (stroked == true && strokeWidth > 0.0) {
//            g.setColor(this.strokeColor);
//
//            float _strokeWidth = (float)strokeWidth;
//            //if (selected) _strokeWidth = 2.0f*_strokeWidth;
//            g.setStroke( new BasicStroke(_strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND) );
//            
//            g.drawRect(ix, iy, iw, ih);
//        }
        
        if (selected) drawSelRect(g);
    }
    
    /**
     * Draw the region that will be used to detect a hit on the shape
     * @param g The Graphics2D on which to draw the hit region
     * @param clr The unique color used to fill on the hit region
     */
    @Override
    void draw(Graphics2D g, Color clr)
    {
        g.setColor(clr);
        g.fill( new Rectangle2D.Double(x, y, width, height));
    }
}
