/*
 * Text.java
 * 
 * Author: Mark F. Russo, Ph.D.
 * Copyright (c) 2012-2020 Mark F. Russo
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

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.font.TextLayout;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.Random;
import javax.swing.UIManager;

/**
 * A class that implements a graphical object made up of text.
 * Starts filled with black and no stroke.
 * 
 * @author Mark F. Russo, Ph.D.
 * @version 1.0
 */
public class Text extends Shape
{
    private String text = "";
    private Font font;
    private double offX;    // Offset of text in TextLayout
    private double offY;
    
    /**
     * Text object constructor
     * @param text      The String drawn as the Text object
     * @param x         The x-coordinate of the Text object upper left corner.
     * @param y         The y-coordinate of the Text object upper left corner.
     * @param size      The font size used to draw the Text object.
     * @param style     The Font class constant that defines the style used to draw the Text object. Example: Font.PLAIN
     * @param fontName  The name of the font used to draw the Text object, Example: "Arial"
     * @param layer     The Layer object to which the Text should be added, or null if not to add to a Pad.
     */
    public Text(String text, double x, double y, int size, int style, String fontName, Layer layer) {
        super(x, y, 0, 0, layer);
        this.text = text;
        font = new Font(fontName, style, size);
        FontMetrics fm = layer.getPad().getFontMetrics(font);
        width = fm.stringWidth(text);
        height = fm.getHeight();
        
        // Default Text to no stroke and black fill
        this.strokeWidth = 0.0;
        fillColor = Color.black;
    }
    
    /**
     * Text object constructor
     * @param text      The String drawn as the Text object
     * @param x         The x-coordinate of the Text object upper left corner.
     * @param y         The y-coordinate of the Text object upper left corner.
     * @param size      The font size used to draw the Text object.
     * @param style     The Font class constant that defines the style used to draw the Text object. Example: Font.PLAIN
     * @param fontName  The name of the font used to draw the Text object, Example: "Arial"
     */
    public Text(String text, double x, double y, int size, int style, String fontName) {
        this(text, x, y, size, style, fontName, Pad.getPad().getLayer(0));
    }
    
    /**
     * Text object constructor
     * @param text      The String drawn as the Text object
     * @param x         The x-coordinate of the Text object upper left corner.
     * @param y         The y-coordinate of the Text object upper left corner.
     * @param size      The font size used to draw the Text object.
     * @param style     The Font class constant that defines the style used to draw the Text object. Example: Font.PLAIN
     * @param fontName  The name of the font used to draw the Text object, Example: "Arial"
     * @param pad       The Pad to which the Text object should be added.
     */
    public Text(String text, double x, double y, int size, int style, String fontName, Pad pad) {
        this(text, x, y, size, style, fontName, pad.getLayer(0));
    }
    
    /**
     * Text object constructor. Create a Text object with an Arial font.
     * @param text  The String drawn as the Text object
     * @param x     The x-coordinate of the Text object upper left corner.
     * @param y     The y-coordinate of the Text object upper left corner.
     * @param size  The font size used to draw the Text object.
     * @param style The Font class constant that defines the style used to draw the Text object. Example: Font.PLAIN
     */
    public Text(String text, double x, double y, int size, int style) {
        this(text, x, y, size, style, "Arial");
    }

    /**
     * Text object constructor. Create a Text object with an Arial font.
     * @param text      The String drawn as the Text object
     * @param x         The x-coordinate of the Text object upper left corner.
     * @param y         The y-coordinate of the Text object upper left corner.
     * @param size      The font size used to draw the Text object.
     * @param fontName  The name of the font used to draw the Text object, Example: "Arial"
     */
    public Text(String text, double x, double y, int size, String fontName) {
        this(text, x, y, size, Font.PLAIN, fontName);
    }

    /**
     * Text object constructor. Create a Text object with style Font.PLAIN and an Arial font.
     * @param text  The String drawn as the Text object
     * @param x     The x-coordinate of the Text object upper left corner.
     * @param y     The y-coordinate of the Text object upper left corner.
     * @param size  The font size used to draw the Text object.
     */
    public Text(String text, double x, double y, int size) {
        this(text, x, y, size, Font.PLAIN, "Arial", Pad.getPad().getLayer(0));
    }
    
    /**
     * Text object constructor. Create a Text object with style Font.PLAIN and an Arial font.
     * @param text  The String drawn as the Text object
     * @param x     The x-coordinate of the Text object upper left corner.
     * @param y     The y-coordinate of the Text object upper left corner.
     * @param size  The font size used to draw the Text object.
     * @param pad   The Pad to which the Text object should be added.
     */
    public Text(String text, double x, double y, int size, Pad pad) {
        this(text, x, y, size, Font.PLAIN, "Arial", pad.getLayer(0));
    }
    
    /**
     * Text object constructor. Create a Text object with style Font.PLAIN and an Arial font.
     * @param text  The String drawn as the Text object
     * @param x     The x-coordinate of the Text object upper left corner.
     * @param y     The y-coordinate of the Text object upper left corner.
     * @param size  The font size used to draw the Text object.
     * @param layer The Layer object to which the Text should be added, or null if not to add to a Pad.
     */
    public Text(String text, double x, double y, int size, Layer layer) {
        this(text, x, y, size, Font.PLAIN, "Arial", layer);
    }
    
    /**
     * Text object constructor. Create a Text object with the default system font attributes.
     * @param   text    The String drawn as the Text object
     * @param   x       The x-coordinate of the Text object upper left corner.
     * @param   y       The y-coordinate of the Text object upper left corner.
     * @param   pad     The Pad to which the Text object should be added.
     */
    public Text(String text, double x, double y, Pad pad) {
        this(text, x, y, pad.getLayer(0));
    }
    
    /**
     * Text object constructor. Create a Text object with the default font attributes.
     * @param   text    The String drawn as the Text object
     * @param   x       The x-coordinate of the Text object upper left corner.
     * @param   y       The y-coordinate of the Text object upper left corner.
     */
    public Text(String text, double x, double y) {
        this(text, x, y, Pad.getPad().getLayer(0));
    }
    
    /**
     * Text object constructor. Create a Text object with the default system font attributes.
     * @param   text    The String drawn as the Text object
     * @param   x       The x-coordinate of the Text object upper left corner.
     * @param   y       The y-coordinate of the Text object upper left corner.
     * @param   layer   The Layer object to which the Text should be added, or null if not to add to a Pad.
     */
    public Text(String text, double x, double y, Layer layer) {
        super(x, y, 0, 0, layer);
        this.text = text;
        this.font = UIManager.getDefaults().getFont("TabbedPane.font");
        FontMetrics fm = layer.getPad().getFontMetrics(this.font);
        width = fm.stringWidth(text);
        height = fm.getHeight();
        
        // Default Text to no stroke and black fill
        this.strokeWidth = 0.0;
        fillColor = Color.black;
    }
    
    /**
     * Text object constructor. 
     * Creates a randomly positioned Text object.
     */
    public Text() {
        this("Hello", 0, 0);
        Pad pad = Pad.getPad();
        Random rnd = new Random();
        double x = rnd.nextDouble()*(pad.getWidth()-100.0);
        double y = rnd.nextDouble()*(pad.getHeight() - 100.0);
        this.setLocation(x, y);
        this.setDraggable(true);
    }
    
    /**
     * Generate a representation of the Text object.
     * @return String representation
     */
    @Override
    public String toString() {
        return "Text x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "text=" + text + ", layer=" + layer;
    }
    
    /**
     * Set the internal text
     * @param text The internal text as a String
     */
    public void setText(String text) {
        this.text = text;
        this.repaint();
    }
    
    /**
     * Return the Text object String
     * @return The text of the object as a String
     */
    public String getText() {
        return this.text;
    }
    
    /**
     * Draw the Text object
     * @param g         The Graphics2D object on which to draw the text object
     */
    @Override
    public void draw(Graphics2D g)
    {
        if (stroked == false && filled == false) return;
        if (this.text.isEmpty()) return;
        
        FontRenderContext frc = g.getFontRenderContext();
        TextLayout tl = new TextLayout(this.text, font, frc);

        // Stash the size and offset of the rendered text
        Rectangle2D bounds = tl.getBounds();
        width  = bounds.getWidth();
        height = bounds.getHeight();
        offX   = bounds.getX();
        offY   = bounds.getY();
        
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
        
        // Draw select rect
        if (selected) drawSelRect(g);
    }
    
    /**
     * Draw the region that will be used to detect a hit on the shape as the bounding box.
     * Take into account the offset of the text in the TextLayout.
     * @param g The Graphics2D on which to draw the hit region
     * @param clr The unique color used to fill on the hit region
     */
    @Override
    void draw(Graphics2D g, Color clr)
    {
        if (stroked == true) {
            g.setColor(clr);
            g.fill(new Rectangle2D.Double(x+offX, y+offY+height, width, height));
        }
    }
}
