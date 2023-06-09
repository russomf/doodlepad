/*
 * Sprite.java
 * 
 * Author: Mark F. Russo, Ph.D.
 * Copyright (c) 2012-2024 Mark F. Russo
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
 * A class that loads a sprite sheet image from a file and animates it on a Pad
 * 
 * @author Mark F. Russo, Ph.D.
 * @version 1.0
 */
public class Sprite extends Shape {
    private String path;
    private BufferedImage img = null;
    private double frameWidth;
    private int frameNum = 0;
    private int nFrames;
    private double targetWidth;
    private double targetHeight;
    
    /**
     * Constructor for objects of class Sprite
     * @param   path        Path to sprite sheet image file.
     * @param   x           The x-coordinate of the image upper left corner.
     * @param   y           The y-coordinate of the image upper left corner.
     * @param   frameWidth  The width of a single frame in the sprite sheet image.
     * @param   nFrames     The number of adjacent frames in the entire sprite sheet image.
     */
    public Sprite(String path, double x, double y, int frameWidth, int nFrames)
    {
        this(path, x, y, frameWidth, nFrames, Pad.getPad().getLayer(0));
    }
    
    /**
     * Constructor for objects of class Sprite
     * @param   path        Path to sprite sheet image file.
     * @param   x           The x-coordinate of the image upper left corner.
     * @param   y           The y-coordinate of the image upper left corner.
     * @param   frameWidth  The width of a single frame in the sprite sheet image.
     * @param   nFrames     The number of adjacent frames in the entire sprite sheet image.
     * @param   pad         The Pad to which the Sprite will be added.
     */
    public Sprite(String path, double x, double y, int frameWidth, int nFrames, Pad pad)
    {
        this(path, x, y, frameWidth, nFrames, pad.getLayer(0));
    }
    
    /**
     * Constructor for objects of class Sprite
     * @param   path        Path to sprite sheet image file.
     * @param   x           The x-coordinate of the image upper left corner.
     * @param   y           The y-coordinate of the image upper left corner.
     * @param   frameWidth  The width of a single frame in the sprite sheet image.
     * @param   nFrames     The number of adjacent frames in the entire sprite sheet image.
     * @param   layer       The Layer object to which the Image will be added, or null if not to add to a Layer.
     */
    public Sprite(String path, double x, double y, int frameWidth, int nFrames, Layer layer)
    {
        super(x, y, 0, 0, layer);
        this.path = path;
        this.frameWidth = frameWidth;
        this.nFrames = nFrames;
        this.setStroked(false);
        this.setFilled(false);
        
        try {
            this.img = ImageIO.read(new File(path));
            this.width = frameWidth;
            this.height = img.getHeight();
            this.targetWidth = frameWidth;
            this.targetHeight = this.height;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Constructor for objects of class Sprite
     * @param path          Path to image file.
     * @param x             The x-coordinate of the image upper left corner.
     * @param y             The y-coordinate of the image upper left corner.
     * @param frameWidth    The width of a single frame in the sprite sheet image.
     * @param nFrames       The number of adjacent frames in the entire sprite sheet image.
     * @param targetWidth   The width with which to draw the sprite.
     * @param targetHeight  The height with which to draw the sprite.
     */
    public Sprite(String path, double x, double y, double frameWidth, int nFrames, int targetWidth, int targetHeight) 
    {
        this(path, x, y, frameWidth, nFrames, targetWidth, targetHeight, Pad.getPad().getLayer(0));        
    }
    
//    /**
//     * Constructor for objects of class Sprite
//     * @param path          Path to image file.
//     * @param x             The x-coordinate of the image upper left corner.
//     * @param y             The y-coordinate of the image upper left corner.
//     * @param frameWidth    The width of a single frame in the sprite sheet image.
//     * @param nFrames       The number of adjacent frames in the entire sprite sheet image.
//     * @param targetWidth   The width with which to draw the sprite.
//     * @param targetHeight  The height with which to draw the sprite.
//     * @param pad           The Pad to which this Sprite should be added
//     */
//    public Sprite(String path, double x, double y, double frameWidth, int nFrames, int targetWidth, int targetHeight, Pad pad)
//    {
//        this(path, x, y, frameWidth, nFrames, targetWidth, targetHeight, pad.getLayer(0));
//    }
    
    /**
     * Constructor for objects of class Sprite
     * @param   path            Path to image file.
     * @param   x               The x-coordinate of the image upper left corner.
     * @param   y               The y-coordinate of the image upper left corner.
     * @param   frameWidth      The width of a single frame in the sprite sheet image.
     * @param   nFrames         The number of adjacent frames in the entire sprite sheet image.
     * @param   targetWidth     The width with which to draw the sprite.
     * @param   targetHeight    The height with which to draw the sprite.
     * @param   layer           The Layer object to which the Sprite will be added, or null if not to add to a Pad.
     */
    public Sprite(String path, double x, double y, double frameWidth, int nFrames, double targetWidth, double targetHeight, Layer layer)
    {
        super(x, y, 0, 0, layer);
        this.path = path;
        this.frameWidth = frameWidth;
        this.nFrames = nFrames;
        this.setStroked(false);
        this.setFilled(false);
        
        try {
            this.img = ImageIO.read(new File(path));
            this.width = frameWidth;
            this.height = img.getHeight();
            this.targetWidth = targetWidth;
            this.targetHeight = targetHeight;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * A no-op to prevent setting text for this Shape.
     */
    public void setText(String text) { }

    /**
     * Generate a representation of the Sprite object.
     * @return String representation
     */
    @Override
    public String toString() {
        return "Sprite x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", path=" + path + ", layer=" + layer;
    }
    
    /**
     * Advance the sprite frame and repaint
     */
    public void advance() {
        frameNum = (frameNum + 1) % nFrames;
        repaint();
    }
    
     /**
     * Draw the current sprite frame
     * @param g         The Graphics2D object on which to draw the sprite frame
     */
    @Override
    public void draw(Graphics2D g)
    {
        int ix = (int)Math.round(x);
        int iy = (int)Math.round(y);
        int iw = (int)Math.round(width);
        int ih = (int)Math.round(height);
        int tw = (int)Math.round(targetWidth);
        int th = (int)Math.round(targetHeight);
        int ifw = (int)Math.round(frameWidth);
        
        if (filled == true) {
            g.setColor(this.fillColor);
            g.fill( new Rectangle2D.Double(x, y, targetWidth, targetHeight));
        } else {
            g.drawImage(img, ix, iy, ix+tw, iy+th, frameNum*ifw, 0, (frameNum+1)*ifw, ih, null);
        }
        
        if (stroked == true && strokeWidth > 0.0) {
            g.setColor(this.strokeColor);
            
            float _strokeWidth = (float)strokeWidth;
            //if (selected) _strokeWidth = 2.0f*_strokeWidth;
            g.setStroke( new BasicStroke(_strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND) );
            
            g.draw(new Rectangle2D.Double(x, y, width, height));
        }
        
        if (selected) drawSelRect(g);
    }
    
    /**
     * Draw the region that will be used to detect a hit on the sprite
     * @param g The Graphics2D on which to draw the hit region
     * @param clr The unique color used to fill on the hit region
     */
    @Override
    void draw(Graphics2D g, Color clr)
    {
        g.setColor(clr);
        g.fill(new Rectangle2D.Double(x, y, frameWidth, height));
    }
}
