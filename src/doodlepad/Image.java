/*
 * Image.java
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

import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.geom.Rectangle2D;

/**
 * A class that loads an image from a file and draws it on a Pad.
 * 
 * @author Mark F. Russo, Ph.D.
 * @version 1.0
 */
public class Image extends Shape
{
    String path;
    BufferedImage img = null;
    
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
        super.setStroked(false);
        super.setFilled(false);
        
        try {
            img = ImageIO.read(new File(path));
            width = img.getWidth();
            height = img.getHeight();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
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
        super.setStroked(false);
        super.setFilled(false);
        
        try {
            img = ImageIO.read(new File(path));
            this.width = width;
            this.height = height;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
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
        super.setStroked(false);
        super.setFilled(false);
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
        int rgba = img.getRGB(x, y);
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
        int rgba = img.getRGB(x, y);
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
        int rgba = img.getRGB(x, y);
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
        int rgba = img.getRGB(x, y);
        //return (rgba & 0x000F);
        return getPixel(x, y).getAlpha();
    }
    
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
        
        if (stroked == true && strokeWidth > 0.0) {
            g.setColor(this.strokeColor);

            float _strokeWidth = (float)strokeWidth;
            //if (selected) _strokeWidth = 2.0f*_strokeWidth;
            g.setStroke( new BasicStroke(_strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND) );
            
            g.drawRect(ix, iy, iw, ih);
        }
        
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
