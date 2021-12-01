//package functionality;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import net.sourceforge.tess4j.Tesseract;
//import net.sourceforge.tess4j.TesseractException;
//
//import javax.imageio.ImageIO;
//
//public class DetectTextTess {
//    public static String detect() {
//        String s = "abc";
//        Tesseract tesseract = new Tesseract();
//        try {
//
//            tesseract.setDatapath("./");
//
//            // the path of your tess data folder
//            // inside the extracted file
//            String imageURL = "https://jeroen.github.io/images/testocr.png";
//
//            URL url = new URL(imageURL);
//            BufferedImage img = ImageIO.read(url);
//            String text
//                    = tesseract.doOCR(img);
//
//            // path of your image file
//            System.out.print(text);
//            s = text;
//        } catch (TesseractException | MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return s;
//    }
//}
//
//
//
