/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package speeddialutility;

/**
 *
 * @author http://searchdomino.techtarget.com/tip/Converting-Web-pages-to-images-using-Java
 */

////Call the Web page and convert to Image
//BufferedImage ire;
//ire = WebImage.create("<web page URL>", 800, 600);
////You can convert the BufferedImage to any format that you wish, jpg I thought was the best format
//ImageIO.write(ire, "jpg", new File ("c:\\Temp\\tt.jpg"));

//Class that Converts the web page to Image
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.*;


import javax.swing.text.html.*;

public abstract class WebImage {

    static class Kit extends HTMLEditorKit {

        public Document createDefaultDocument() {
            HTMLDocument doc
                    = (HTMLDocument) super.createDefaultDocument();
            doc.setTokenThreshold(Integer.MAX_VALUE);
            doc.setAsynchronousLoadPriority(-1);
            return doc;
        }
    }

    public static BufferedImage create(String src, int width, int height) {
        BufferedImage image = null;
        JEditorPane pane = new JEditorPane();
        Kit kit = new Kit();
        pane.setEditorKit(kit);
        pane.setEditable(false);
        pane.setMargin(new Insets(0, 0, 0, 0));
        try {
            pane.setPage(src);
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            Container c = new Container();
            SwingUtilities.paintComponent(g, pane, c, 0, 0, width, height);
            g.dispose();
        } catch (Exception e) {
            System.out.println(e);
        }
        return image;
    }
    
    public static void main (String[] args){
        //Call the Web page and convert to Image
        BufferedImage ire;
        ire = WebImage.create("http://www.agenziaentrate.it", 800, 600);
        try {
            //You can convert the BufferedImage to any format that you wish, jpg I thought was the best format
            ImageIO.write(ire, "jpg", new File("d:\\temp\\tt.jpg"));
        } catch (IOException ex) {
            Logger.getLogger(WebImage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
