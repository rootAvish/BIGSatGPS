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
import java.awt.geom.*;
/**
 *
 * @author rootavish
 */
public class DiamondAnnotation extends DrawableAnnotation {

    private Point2D center; // Annotation center point.
    private double width; // Width of diamond annotation.
    private double height; // Height of diamond annotation.
    private BasicStroke stroke; // "Pen" used for drawing.
// Constructor for the class.

    /**
     *
     * @param c
     * @param w
     * @param h
     * @param pw
     */
    public DiamondAnnotation(Point2D c, double w, double h, float pw) {
        center = c;
        width = w;
        height = h;
        stroke = new BasicStroke(pw);
    }
// Concrete implementation of the paint method.

    /**
     *
     * @param g2d
     */
    public void paint(Graphics2D g2d) {
        int x = (int) center.getX();
        int y = (int) center.getY();
        int xmin = (int) (x - width / 2);
        int xmax = (int) (x + width / 2);
        int ymin = (int) (y - height / 2);
        int ymax = (int) (y + height / 2);
        g2d.setStroke(stroke);
        g2d.setColor(getColor());
        g2d.drawLine(x, ymin, xmin, y);
        g2d.drawLine(xmin, y, x, ymax);
        g2d.drawLine(x, ymax, xmax, y);
        g2d.drawLine(xmax, y, x, ymin);
    }
}


