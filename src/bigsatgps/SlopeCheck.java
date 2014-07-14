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

import java.awt.image.SampleModel;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;
import org.apache.log4j.lf5.util.LogFileParser;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;

/**
 *
 * @author rootavish
 */
public class SlopeCheck {
    
    /**
     *
     * @param sequencepath
     * @param bdhandle
     * @param points
     * @return
     * @throws IOException
     */
    public static String[] slopeCheck(String sequencepath,BigDataHandler bdhandle,List<PointData[]> points) throws IOException 
    {
        RasterAnalysis rasterObj = new RasterAnalysis();
        int numbands;
        RandomIter iterator;
        
        
        bdhandle.SequenceToImage(sequencepath);
        String imagepath = sequencepath.substring(0, sequencepath.indexOf(".")-1) + "copy.tif";
        
        rasterObj.setGeoTransform(imagepath);
        
        PlanarImage image = JAI.create("fileload",imagepath);
        SampleModel sm = image.getSampleModel();
        iterator = RandomIterFactory.create(image, null);
        numbands = sm.getNumBands();
        int roadnum=0;
        String[] slope = new String[points.size()];
        Arrays.fill(slope,"flat");
        for (PointData[] pointslist:points) {
            for (PointData x: pointslist) {
              double[] cartesian = rasterObj.WGS84toRaster(x.getX(),x.getY());
              int[] pixel = new int[numbands];
              iterator.getPixel((int)cartesian[0],(int)cartesian[1], pixel);
            
              if ((pixel[0]== 85 && pixel[1]== 255 && pixel[2] == 0) &&
                  (pixel[0] == 197 && pixel[1] == 0 && pixel[2] == 255) &&
                  (pixel[0] == 255 & pixel[1] == 255 & pixel[2] == 0))   {
                  
                  if (priority(slope[roadnum]) < priority("safe") )
                    slope[roadnum] = "safe";
              }
              else if (pixel[0] == 0 & pixel[1] == 112 & pixel[2] == 0) {
                  
                   if (priority(slope[roadnum]) < priority("critical") )
                        slope[roadnum] = "critical";
              }
              else if ((pixel[0] == 255 & pixel[1] == 0 & pixel[2] == 197) &&
                       (pixel[0] == 255 & pixel[1] == 170 & pixel[2] == 0)) {
                  
                   if (priority(slope[roadnum]) < priority("human traversable") )
                         slope[roadnum] = "human traversable";
              }
              else {
                  slope[roadnum] = "np";
              }
            }
            roadnum++;
        }
        return slope;
    }
    
    private static int priority(String type) {
        switch(type) {
            case "flat": return 0;
            case "safe" : return 1;
            case "critical": return 2;
            case "human traversable": return 3;
            case "np": return 4;
            default: return 0;
        }
    }
}