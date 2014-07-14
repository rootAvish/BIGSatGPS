/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bigsatgps;

/**
 *
 * @author rootavish
 */
import java.io.*;
import java.util.*;
import org.nocrala.tools.gis.data.esri.shapefile.*;
import org.nocrala.tools.gis.data.esri.shapefile.exception.InvalidShapeFileException;
import org.nocrala.tools.gis.data.esri.shapefile.header.ShapeFileHeader;
import org.nocrala.tools.gis.data.esri.shapefile.shape.*;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.*;

/**
 *
 * @author rootavish
 */
public class ShapeData {
    
    //Member declarations
    
    // A variable to hold the header of this shapefile
    ShapeFileHeader fileheader ; 
    // A list of all shapes in the file
    List<AbstractShape> shapes; 
    //The name of the file
    String shpfilename ;
    //Validation preferences for the current shapefile
    ValidationPreferences prefs ;
    //ESRI shape type contained in this file
    ShapeType fileShapeType ;
    //Data structure to hold all limits for this shape
    HashMap<String, Double> limits ;
    //List for all pointdata of current object
    List<PointData[]> pointsList ;
    
    /*
     * Constructor for the class
     * @shpFilename Name/path of the input shapefile
     */

    /**
     *
     * @param shpFilename
     */
    
    public ShapeData(String shpFilename)  {
        shpfilename = shpFilename ;
        prefs = new ValidationPreferences();
        shapes = null; fileShapeType = null; fileheader = null;pointsList=null;
        limits = new HashMap<>() ;
    }
    
    //A function to parse the shapefile for the currently loaded tiff and give a list of all shapes in the file.

    /**
     *
     */
        public void setShapes() {
        
        try {
            
            if ( fileShapeType == null)
                setShapeType() ;
            
            
            FileInputStream dotshp = new FileInputStream(shpfilename);
            ShapeFileReader reader = new ShapeFileReader(dotshp,prefs);
          
            shapes = new ArrayList<>();
            
            AbstractShape currentshape ;
            while  ( ( currentshape = reader.next()) != null )
            {
                shapes.add(currentshape) ;
            }
            
        }
        catch(IOException|InvalidShapeFileException ex) {
            System.err.println(ex.toString());
        }
    
    }
    
    private void setShapeType() {
        ShapeType type = fileheader.getShapeType() ;
        fileShapeType = type ;
    }
    
    /**
     *
     */
    public void setLimits() {
        limits.put("MaxX",fileheader.getBoxMaxX());
        limits.put("MinX",fileheader.getBoxMinX());
        limits.put("MaxY",fileheader.getBoxMaxY());
        limits.put("MinY",fileheader.getBoxMinY());
        limits.put("MaxZ",fileheader.getBoxMaxZ());
        limits.put("MinZ",fileheader.getBoxMinZ());
        limits.put("MaxM",fileheader.getBoxMaxM());
        limits.put("MinM",fileheader.getBoxMinM());
    }
    
    //A function to set the file header for a shapefile

    /**
     *
     */
        public void setFileHeader() {
        try {
            FileInputStream dotshp = new FileInputStream(shpfilename);
            fileheader = new ShapeFileHeader(dotshp, prefs);           
        }
        catch(IOException|InvalidShapeFileException ex) {
            System.out.println(ex.toString());
        }
    }
    
    //The ceantral function of this class, if getting points is the final objective, directly call this function.

    /**
     *
     */
        public void SetPoints() {
        
        if ( fileheader == null )
            setFileHeader();
        if ( fileShapeType == null )
            setShapeType();
        if ( shapes == null )
            setShapes();
        if ( limits.isEmpty())
            setLimits();
        
        checkShapeConsistency();
        
        switch(fileShapeType)
        {
            case POLYLINE:
                 pointsList = new ArrayList<>();
                 for ( AbstractShape shape : shapes) {
                     PolylineShape current = (PolylineShape) shape ;
                     
                     PointData[] pointarray = current.getPoints() ;
                     pointsList.add(pointarray);
                 } 
                 break;
            case POLYGON:
                pointsList = new ArrayList<>();
                 for ( AbstractShape shape : shapes) {
                     PolygonShape current = (PolygonShape)shape;
                     
                     PointData[] pointarray = current.getPoints() ;
                     pointsList.add(pointarray);
                 } 
                 break;
            default:
                System.err.println("I have not added this backend yet.");
        }
    }
    
    /*
     * Getters for this file
     */

    /**
     *
     * @return
     */
    
    public List<AbstractShape> getShapes() {
        return shapes ;
    }
    
    /**
     *
     * @return
     */
    public ShapeFileHeader getFileHeader() {
        return fileheader;
    }
    
    /**
     *
     * @return
     */
    public ShapeType getFileShapeType() {
        return fileShapeType;
    }
    
    /**
     *
     * @return
     */
    public HashMap getLimits() {
        return limits ;
    }
    
    /**
     *
     * @return
     */
    public List<PointData[]> getPoints() {
        return pointsList ;
    }
    /*
     *  Utility functions for this object 
     */
    
    //TODO : Use this iterator boilerplate code
    
    /**
     *
     */
        
    public void iterateSyntax ()
    {
        Iterator it = limits.entrySet().iterator();
        while ( it.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next() ;
            System.out.println(pair.getKey()+" = "+pair.getValue());
            it.remove();
        }
    }
    
    //TODO : this is the boilerplate iterator for the points
    
    /**
     *
     */
        
    public void iteratePoints() {
        for ( PointData[] pointset : pointsList) {
            for ( PointData currentpoint : pointset )
            {
                System.out.println(currentpoint.getX() + " "+ currentpoint.getY());
            }
        }
    }

    /**
     *
     */
    public void checkShapeConsistency() {
        for (AbstractShape shape_object : shapes) {
            ShapeType shp_type = shape_object.getShapeType();
            
            if ( shp_type != fileShapeType )
            {
                System.err.println("Shapefile "+shpfilename+" is not consistent and has more than one type of shape!");
                System.exit(2);
            }
        }
    }
}
