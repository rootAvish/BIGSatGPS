/*
 * The MIT License
 *
 * Copyright 2014 rootavish.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package bigsatgps;

import java.awt.*;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import com.sun.media.jai.widget.DisplayJAI;

/**
 *
 * @author rootavish
 */
public class DisplayJAIWithAnnotations extends DisplayJAI {

    /**
     *
     */
    protected ArrayList annotations; // List of annotations that will be
// (non-interactively) drawn.
// Constructor for the class.

    /**
     *
     * @param image
     */
    public DisplayJAIWithAnnotations(RenderedImage image) {
        super(image); // calls the constructor for DisplayJAI
        annotations = new ArrayList(); // List that will held the drawings.
    }
// This method paints the component and all its annotations.

    /**
     *
     * @param g
     */
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        for (int a = 0; a < annotations.size(); a++) // For each annotation.
        {
            DrawableAnnotation element = (DrawableAnnotation) annotations.get(a);
            element.paint(g2d);
        }
    }
// Add an annotation (instance of any class that inherits from
// DrawableAnnotation to the list of annotations which will be drawn.

    /**
     *
     * @param a
     */
    public void addAnnotation(DrawableAnnotation a) {
        annotations.add(a);
    }
}
