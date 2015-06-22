package OpencvNeuroph;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;






import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import Bd.conexion;

class DetectFaceDemo {
	CascadeClassifier faceDetector = new CascadeClassifier(
			"C:\\opencv\\sources\\data\\lbpcascades\\lbpcascade_frontalface.xml");
	MatOfRect faceDetections = new MatOfRect();
	VideoCapture cap = new VideoCapture(0);
	Mat imagen = new Mat();
	public int cont = 0;
	public double[][] entrenamientos;
	public double[][] entrenamientos1;
	public  String entrenamientosBW="";
	public  String entrenamientosGRAY="";
	
	public void run() {
		System.out.println("Deteccion de rostros con OpenCV y Webcam en java");
		Ventana ventana = new Ventana();
		if (cap.isOpened()) {
			String nombre = JOptionPane.showInputDialog("Ingrese su nombre");
			JOptionPane
					.showMessageDialog(
							null,
							"A continuacion se le tomaran 1 fotos, en intervalos de segundo.\n"
									+ "Procure tener buena iluminacion y por cada toma mover su rostro en diversos angulos para una mayor presicion");
			entrenamientos = new double[1][imagen.width() * imagen.height()];
			entrenamientos1 = new double[1][imagen.width() * imagen.height()];
			while (cont != 1) {
				try {
					Thread.sleep(5);
					cap.read(imagen);
					if (!imagen.empty()) {
						faceDetector.detectMultiScale(imagen, faceDetections);
						if (faceDetections.toArray().length != 0) {
							guardar(imagen, cont, nombre);
						}
						for (Rect rect : faceDetections.toArray()) {
							Core.rectangle(imagen, new Point(rect.x, rect.y),
									new Point(rect.x + rect.width, rect.y
											+ rect.height), new Scalar(0, 255,
											0));
						}
						ventana.setImage(convertir(imagen));
					}
				} catch (Exception ex) {
					Logger.getLogger(DetectFaceDemo.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			}
//			Entrenamiento i = new Entrenamiento();
//			i.convert(variables.getNombre(), entrenamientos);
		}
	}

	private Image convertir(Mat imagen) {
		Mat gris = new Mat(imagen.width(), imagen.height(), imagen.type());
		Imgproc.cvtColor(imagen, gris, Imgproc.COLOR_RGB2GRAY);
		MatOfByte matOfByte = new MatOfByte();
		Highgui.imencode(".jpg", gris, matOfByte);
		byte[] byteArray = matOfByte.toArray();
		BufferedImage bufImage = null;
		try {
			InputStream in = new ByteArrayInputStream(byteArray);
			bufImage = ImageIO.read(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Image) bufImage;
		
	}

	private void guardar(Mat imagen, int num, String nombre) {
		Mat tamaño = new Mat(imagen.width(), imagen.height(), imagen.type());
		Size sz=new Size(100,100);
		Imgproc.resize(imagen, tamaño, sz);
		Mat gris = new Mat(tamaño.width(), tamaño.height(), imagen.type());
		Imgproc.cvtColor(tamaño, gris, Imgproc.COLOR_RGB2GRAY);
		MatOfByte matOfByte = new MatOfByte();
		Highgui.imencode(".jpg", gris, matOfByte);
		Highgui.imwrite(nombre + num + ".jpg", gris);
		variables.setNombre(nombre);
		byte[] byteArray = matOfByte.toArray();
		BufferedImage bufImage = null;
		try {
			InputStream in = new ByteArrayInputStream(byteArray);
			bufImage = ImageIO.read(in);
			Image tmp = bufImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		    BufferedImage dimg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		    Graphics2D g2d = dimg.createGraphics();
		    g2d.drawImage(tmp, 0, 0, null);
		    g2d.dispose();
			if (cont == 0) {
				entrenamientos = new double [1][dimg.getHeight()
						* dimg.getWidth()];
				entrenamientos1 = new double [1][dimg.getHeight()
				        						* dimg.getWidth()];
			}
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
						pixelDataGrayBn[counter] = (double) Math.abs(rgb[0] / 16);
						if(rgb[0]>127){
							pixelDatabinario[counter]=1;
						}else{
							pixelDatabinario[counter]=0;
						}
						
						counter++;
					}
				}
			}
			for (int e = 0; e < pixelDataGrayBn.length; e++) {
				entrenamientos1[cont][e] = pixelDataGrayBn[e];
				entrenamientosGRAY+=entrenamientos1[cont][e]+" ";
//				System.out.println(entrenamientos1[cont][e]);
				
				entrenamientos[cont][e] = pixelDatabinario[e];
				entrenamientosBW+=entrenamientos[cont][e]+" ";
//				System.out.println(entrenamientos[cont][e]);
			}
			
			variables.setBw(entrenamientosBW);
			variables.setGray(entrenamientosGRAY);
			cont++;
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

/*public class Claseimagen {
	public Claseimagen() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		try {
			new DetectFaceDemo().run();
		} catch (Throwable ex) {
			Logger.getLogger(Claseimagen.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}*/


public class Claseimagen {
	int _id=0;
	
	public Claseimagen(String id) {
		_id=Integer.parseInt(id);
		variables.set_id(_id);
		
	}
	public static void main(String[] args) {
		conexion con;
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		try {
			new DetectFaceDemo().run();
			System.out.println(variables.get_id());
			System.out.println(variables.getBw());
			System.out.println(variables.getGray());
			con= new conexion();
			con.agregar("entrada", "gris,bn,idUsuario", variables.getGray()+","+
					variables.getBw()+","+variables.get_id());
			
		} catch (Throwable ex) {
			Logger.getLogger(Claseimagen.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}