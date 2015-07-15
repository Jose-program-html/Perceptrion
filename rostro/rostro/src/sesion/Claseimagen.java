package sesion;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Claseimagen {
	int _id=0;

	public double[] entrenamientos;
	public double[] entrenamientos1;
	public String entrenamientosBW;
	public String entrenamientosGRAY;

	public void guardar(Mat imagen, Point p1, Point p2) {
		Rect rectCrop = new Rect(p1, p2);
		Mat result = imagen.submat(rectCrop);
		Mat tamaño = new Mat(result.width(), result.height(), result.type());
		Size sz = new Size(100, 100);
		Imgproc.resize(result, tamaño, sz);
		Mat gris = new Mat(tamaño.width(), tamaño.height(), tamaño.type());
		Imgproc.cvtColor(tamaño, gris, Imgproc.COLOR_RGB2GRAY);
		Mat imgH = new Mat(gris.rows(),gris.cols(),gris.type());
		gris.convertTo(imgH, -1, 2.5, 0);
		MatOfByte matOfByte = new MatOfByte();
		try {
			Highgui.imencode(".bmp", imgH, matOfByte);
			Image im = ImageIO.read(new ByteArrayInputStream(
					matOfByte.toArray()));
			BufferedImage dimg = new BufferedImage(100, 100,
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = dimg.createGraphics();
			g2d.drawImage(im, 0, 0, null);
			g2d.dispose();
			entrenamientos = new double[dimg.getHeight()
						* dimg.getWidth()];
			entrenamientos1 = new double[dimg.getHeight()
						* dimg.getWidth()];
			double[] pixelDataGrayBn = new double[dimg.getHeight()
					* dimg.getWidth()];
			double[] pixelDatabinario = new double[dimg.getHeight()
					* dimg.getWidth()];
			int[] rgb;
			int counter = 0;
			for (int i = 0; i < dimg.getHeight(); i++) {
				for (int j = 0; j < dimg.getWidth(); j++) {
					rgb = getPixelData(dimg, j, i);
					if (rgb[0] == rgb[1] && rgb[1] == rgb[2]) {
						pixelDataGrayBn[counter] = (double) Math
								.abs(rgb[0] / 16);
						if (rgb[0] > 127) {
							pixelDatabinario[counter] = 1;
						} else {
							pixelDatabinario[counter] = 0;
						}
						counter++;
					}
				}
			}
			int count = 0;
			for (int i = 0; i < dimg.getHeight(); i++) {
				for (int j = 0; j < dimg.getWidth(); j++) {
					if (pixelDatabinario[count] == 0.0) {
						System.out.print("0");
					} else {
						System.out.print("1");
					}
					count++;
				}
				System.out.println();
			}
			entrenamientosBW = variables.getBw();
			entrenamientosGRAY = variables.getGray();
			for (int e = 0; e < pixelDataGrayBn.length; e++) {
				entrenamientos1[e] = pixelDataGrayBn[e];
				entrenamientosGRAY += entrenamientos1[e] + " ";
				entrenamientos[e] = pixelDatabinario[e];
				entrenamientosBW+= entrenamientos[e] + " ";
			}
			variables.setBw(entrenamientosBW);
			variables.setGray(entrenamientosGRAY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static int[] getPixelData(BufferedImage img, int x, int y) {
		int argb = img.getRGB(x, y);

		int rgb[] = new int[] { (argb >> 16) & 0xff, // red
				(argb >> 8) & 0xff, // green
				(argb) & 0xff // blue
		};
		return rgb;
	}
}