/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bigsatgps;

import java.awt.image.SampleModel;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
/**
 *
 * @author rootavish
 */
public class SnowCover {
    
    int numbands;
    RandomIter iterator;
    
    //The base function of all other functions, call this only.

    /**
     *
     * @param points
     * @param filename
     * @return
     */
        public Boolean check(PointData[] points,String filename) {
        SetIterator(filename);
        RasterAnalysis rasterObj = new RasterAnalysis();
        rasterObj.setGeoTransform(filename);

      for (PointData x: points) {
          
          double[] cartesian = rasterObj.WGS84toRaster(x.getX(),x.getY());
          int[] pixel = new int[numbands];
          iterator.getPixel((int)cartesian[0],(int)cartesian[1], pixel);
          
          // If the intensity of red, yellow and Blue, all three is greater than 200, we have snow on this road.
          if ( pixel[0] > 240 && pixel[1] > 240 && pixel[2] > 240) return Boolean.FALSE;
      }
      
      return Boolean.TRUE ;
    }
    
    private void SetIterator(String path) {
        PlanarImage image = JAI.create("fileload",path);
        SampleModel sm = image.getSampleModel();
        iterator = RandomIterFactory.create(image, null);
        numbands = sm.getNumBands();
    }
}
