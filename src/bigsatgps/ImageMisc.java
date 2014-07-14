/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bigsatgps;

import com.sun.media.jai.widget.DisplayJAI;
import java.util.List;
import java.awt.image.Raster;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.TiledImage;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;

/**
 *
 * @author rootavish
 */
public class ImageMisc extends DisplayJAI{
    
    DisplayJAIWithAnnotations display;   
    DiamondAnnotation d1;
    /* Here's a constructor for the class
     * @RenderedImage: im
     */

    /**
     *
     * @param im
     */
    
    public ImageMisc(RenderedImage im) {
        super(im);
        addMouseListener(this);
    }
    
    /**
     *
     * @param path
     * @throws IOException
     */
    public void display(String path) throws IOException {

        PlanarImage image = JAI.create("fileload", path);
        
             display = new DisplayJAIWithAnnotations(image);
// Create three diamond-shaped annotations.

        DiamondAnnotation d2
                = new DiamondAnnotation(new Point2D.Double(249, 84), 20, 20, 3);
        d2.setColor(Color.BLACK);
        DiamondAnnotation d3
                = new DiamondAnnotation(new Point2D.Double(303, 33), 35, 35, 5);
        d3.setColor(Color.GRAY);
// Add the annotations to the instance of DisplayJAIWithAnnotations.
        display.addAnnotation(d1);
        display.addAnnotation(d2);
        display.addAnnotation(d3);
// Create a new Frame and set the DisplayJAIWithAnnotations.
        JFrame frame = new JFrame();
        frame.setTitle("Annotations over an image");
        frame.getContentPane().add(new JScrollPane(display));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 200); // Set the frame size.
        frame.setVisible(true);
    }
    
    public static void display(PlanarImage image) throws IOException {
        DisplayJAIWithAnnotations display
                = new DisplayJAIWithAnnotations(image);
// Create three diamond-shaped annotations.
        DiamondAnnotation d1
                = new DiamondAnnotation(new Point2D.Double(229, 55), 20, 20, 2);
        d1.setColor(Color.BLACK);
        DiamondAnnotation d2
                = new DiamondAnnotation(new Point2D.Double(249, 84), 20, 20, 3);
        d2.setColor(Color.BLACK);
        DiamondAnnotation d3
                = new DiamondAnnotation(new Point2D.Double(303, 33), 35, 35, 5);
        d3.setColor(Color.GRAY);
// Add the annotations to the instance of DisplayJAIWithAnnotations.
        
        display.addAnnotation(d2);
        display.addAnnotation(d3);
// Create a new Frame and set the DisplayJAIWithAnnotations.
        JFrame frame = new JFrame();
        frame.setTitle("Annotations over an image");
        frame.getContentPane().add(new JScrollPane(display));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 200); // Set the frame size.
        frame.setVisible(true);
    }

    /**
     *
     * @param path_original
     * @param points
     */
    public static void WriteOutRoads(String path_original, List<PointData[]> points) {

        PlanarImage pi = JAI.create("fileload", path_original);
        int width = pi.getWidth();
        int height = pi.getHeight();
        SampleModel sm = pi.getSampleModel();
        int nbands = sm.getNumBands();
        Raster inputRaster = pi.getData();
        WritableRaster outputRaster = inputRaster.createCompatibleWritableRaster();
        int[] pixels = new int[nbands * width * height];
        inputRaster.getPixels(0, 0, width, height, pixels);
        int offset;
        int h, w;
        RasterAnalysis rasterObj = new RasterAnalysis();
        rasterObj.setGeoTransform(path_original);
        for (PointData[] pointslist : points) {
            for (PointData x : pointslist) {
                double[] cartesian = rasterObj.WGS84toRaster(x.getX(), x.getY());
                h = (int) cartesian[0];
                w = (int) cartesian[1];
                offset = h * width * nbands + w * nbands;
                for (int band = 0; band < nbands; band++) {
                    pixels[offset + band] = 255;
                }
            }
        }
        outputRaster.setPixels(0, 0, width, height, pixels);
        TiledImage ti = new TiledImage(pi, 1, 1);
        ti.setData(outputRaster);
        
        PlanarImage im = ti;
        try {
            display(im);
        }catch(IOException ex){
            System.err.println(ex.toString());
        }
    }
    
    public void mousemoved(MouseEvent me) {
        int x = me.getX();
        int y = me.getY();
             d1
                = new DiamondAnnotation(new Point2D.Double(x, y), 20, 20, 2);
        d1.setColor(Color.BLACK);
        display.addAnnotation(d1);
    }
}