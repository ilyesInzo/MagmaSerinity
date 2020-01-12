/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.bibliotheque;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author inzo
 */
public class TraitementImage {
    public static InputStream resizeImageMaxHeight(InputStream imageInputStream, int maxHeight, String fileName) {
        try {
            BufferedImage originalImage = ImageIO.read(imageInputStream);
            int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
            int widthOriginale = originalImage.getWidth();
            int heightOriginale = originalImage.getHeight();
            int widthResized = (widthOriginale * maxHeight) / heightOriginale;
            BufferedImage resizedImage = new BufferedImage(widthResized, maxHeight, type);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, widthResized, maxHeight, null);
            g.dispose();
            g.setComposite(AlphaComposite.Src);

            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, fileName.substring(fileName.lastIndexOf("." )+ 1), os);
            return new ByteArrayInputStream(os.toByteArray());
        } catch (IOException ex) {
            System.out.println("IOException: " + ex.getMessage());
            return null;
        }
    }

    public static InputStream resizeImageMaxWidth(InputStream imageInputStream, int maxWidth, String fileName) {
        try {
            BufferedImage originalImage = ImageIO.read(imageInputStream);
            int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
            int widthOriginale = originalImage.getWidth();
            int heightOriginale = originalImage.getHeight();
            int heightResized = (heightOriginale * maxWidth) / widthOriginale;
            BufferedImage resizedImage = new BufferedImage(maxWidth, heightResized, type);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, maxWidth, heightResized, null);
            g.dispose();
            g.setComposite(AlphaComposite.Src);
            
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, fileName.substring(fileName.lastIndexOf("." )+ 1), os);
            return new ByteArrayInputStream(os.toByteArray());
        } catch (IOException ex) {
            return null;
        }
    }

    
}
