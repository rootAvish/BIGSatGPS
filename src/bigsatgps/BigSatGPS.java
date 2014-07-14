/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bigsatgps;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.swing.JFileChooser;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import java.io.File;

/**
 *
 * @author rootavish
 */


public class BigSatGPS {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException, Exception{
        RasterAnalysis raobject = new RasterAnalysis();
        JFileChooser filechooser = new JFileChooser();
        BigDataHandler bigdata = new BigDataHandler();
        
        int ret = filechooser.showOpenDialog(null);
        String filename = null;
        if (ret== JFileChooser.APPROVE_OPTION) 
        {
            File file = filechooser.getSelectedFile();
            filename= file.getAbsolutePath();
        }
        else {
            System.err.println("You never chose a file");
            System.exit(1);
        }

        String seqfilename = bigdata.ImageToSequence(filename);
        bigdata.SequenceToImage(seqfilename);
        raobject.setGeoTransform(filename);
        String shapefilename = "/home/rootavish/DRDO/ROAD/Shape/Road_62O7.shp";//TODO
        ShapeData shapedata = new ShapeData(shapefilename);
        shapedata.SetPoints();
        List<PointData[]> points = shapedata.getPoints();
        
        ImageMisc.WriteOutRoads(filename, points);
        SnowCover snowcover = new SnowCover();
        HashMap<PointData[],Boolean> saferoads = new HashMap<>();
        for (PointData[] pointdata : points) {
            saferoads.put(pointdata, snowcover.check(pointdata, filename));
        }
    }
    
}
