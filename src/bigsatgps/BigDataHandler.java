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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.log4j.PropertyConfigurator;
import java.io.File;
/*
 *
 * @author rootavish
 */

/**
 *
 * @author rootavish
 */

public class BigDataHandler {
    
    //The universal configuration for the Hadoop module
    Configuration confHadoop;

    /**
     *
     * @param infile
     * @return
     * @throws Exception
     */
    public String ImageToSequence(String infile) throws Exception {
                String log4jConfPath = "lib/log4j.properties";
                PropertyConfigurator.configure(log4jConfPath);
                confHadoop = new Configuration();        
                confHadoop.addResource(new Path("/hadoop/projects/hadoop-1.0.4/conf/core-site.xml"));
                confHadoop.addResource(new Path("/hadoop/projects/hadoop-1.0.4/conf/hdfs-site.xml"));  
                FileSystem fs = FileSystem.get(confHadoop);
                Path inPath = new Path(infile);
                String outfile = infile.substring(0,infile.indexOf("."))+".seq";
                Path outPath = new Path(outfile);
                System.out.println();
                System.out.println("Successfully created the sequencefile "+outfile);
                FSDataInputStream in = null;
                Text key = new Text();
                BytesWritable value = new BytesWritable();
                SequenceFile.Writer writer = null;
                try{
                        in = fs.open(inPath);
                        byte buffer[] = new byte[in.available()];
                        in.read(buffer);
                        writer = SequenceFile.createWriter(fs, confHadoop, outPath, key.getClass(),value.getClass());
                        writer.append(new Text(inPath.getName()), new BytesWritable(buffer));
                        IOUtils.closeStream(writer);
                        return outfile;
                }catch (IOException e) {
                        System.err.println("Exception MESSAGES = "+e.getMessage());
                        IOUtils.closeStream(writer);
                        return null;
                }
        }
    
    /**
     *
     * @param inpath
     * @throws IOException
     */
    public void SequenceToImage(String inpath) throws IOException{
       FileSystem fs = FileSystem.get(confHadoop);
       Path inputFilePath = new Path(inpath);
       SequenceFile.Reader reader = new SequenceFile.Reader(fs, inputFilePath, confHadoop);
       Text key = (Text)
    ReflectionUtils.newInstance(reader.getKeyClass(), confHadoop);
       BytesWritable value = (BytesWritable)
    ReflectionUtils.newInstance(reader.getValueClass(), confHadoop);
       reader.next(key,value);
       byte[] imagebytearray = value.copyBytes();
       BufferedImage bufferedseq = ImageIO.read(new ByteArrayInputStream(imagebytearray));
       String outpath = (inpath.substring(0,inpath.indexOf(".")) + "copy.tif");
       System.out.println("image was successfully retrieved and written to "+outpath);
       ImageIO.write(bufferedseq,"tiff",new File(outpath));
    }
}
