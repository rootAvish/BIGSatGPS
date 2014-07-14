/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bigsatgps;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.gdal.osr.CoordinateTransformation;
/**
 *
 * @author rootavish
 */
public class RasterAnalysis {
    
    Dataset tifdataset ;
    double xscale;
    double yscale;
    double Xorigin,Yorigin;
    double[] GeoTransformCoefficients;
    
    /**
     *
     * @param filename
     */
    public void setGeoTransform(String filename) {
        gdal.AllRegister();
        tifdataset = gdal.Open(filename,  gdalconstConstants.GA_ReadOnly);
        
        double[] adfGeoTransform = new double[6];
        tifdataset.GetGeoTransform(adfGeoTransform);
        if (adfGeoTransform[2] == 0.0 && adfGeoTransform[4] == 0.0) {
                Xorigin = adfGeoTransform[0] ;
                Yorigin = adfGeoTransform[3] ;

                xscale = adfGeoTransform[1];
                yscale = adfGeoTransform[5];
                
        } else {
            GeoTransformCoefficients = adfGeoTransform;
        }
    }
    
    /**
     *
     * @return
     */
    public double getXScale() {
        return xscale;
    }
    
    /**
     *
     * @return
     */
    public double getYscale() {
        return yscale;
    }
    
    /**
     *
     * @return
     */
    public double getStartX() {
        double[] retval= rasterToWGS84(tifdataset,0.0,0.0) ;
        return retval[0];
    }
    
    /**
     *
     * @return
     */
    public double getStartY() {
        double[] retval= rasterToWGS84(tifdataset,0.0,0.0) ;
        return retval[0];
    }
    
    static double[] rasterToWGS84(Dataset hDataset, double x,double y) {
                double dfGeoX, dfGeoY;
		String pszProjection;
                double[] adfGeoTransform = new double[6];
		CoordinateTransformation hTransform = null;
                
                hDataset.GetGeoTransform(adfGeoTransform);
		{
			pszProjection = hDataset.GetProjectionRef();

			dfGeoX = adfGeoTransform[0] + adfGeoTransform[1] * x
					+ adfGeoTransform[2] * y;
			dfGeoY = adfGeoTransform[3] + adfGeoTransform[4] * x
					+ adfGeoTransform[5] * y;
		}
                double coordinates[] = new double[2];
                coordinates[0]= dfGeoX;
                coordinates[1] = dfGeoY;
                
                return coordinates;
    }   
    
    /**
     *
     * @param x
     * @param y
     * @return
     */
    public double[] WGS84toRaster(double x,double y) {
        double[] raster = new double[2];
        raster[0] = Math.floor((x - Xorigin) / xscale);
        raster[1] = Math.floor((y - Yorigin)/yscale);
        if (raster[0] < 0) {
            raster[0] =Math.abs(raster[0]);
        }
        if (raster[1] < 0) {
            raster[1] =Math.abs(raster[1]);
        }
        return raster;
    }
}
