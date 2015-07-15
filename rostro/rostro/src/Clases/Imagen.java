 package Clases;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Imagen {

	private String nombreImagen = "";
	public BufferedImage imageActual;

	// Método que devuelve una imagen abierta desde archivo
	// Retorna un objeto BufferedImagen
	public BufferedImage abrirImagen() {
		// Creamos la variable que será devuelta (la creamos como null)
		BufferedImage bmp = null;
		// Creamos un nuevo cuadro de diálogo para seleccionar imagen
		JFileChooser selector = new JFileChooser();
		// Le damos un título
		selector.setDialogTitle("Seleccione una imagen");
		// Filtramos los tipos de archivos
		FileNameExtensionFilter filtroImagen = new FileNameExtensionFilter(
				"JPG & GIF & BMP & PNG", "jpg", "gif", "bmp", "png");
		selector.setFileFilter(filtroImagen);
		// Abrimos el cuadro de diálog
		int flag = selector.showOpenDialog(null);
		// Comprobamos que pulse en aceptar
		if (flag == JFileChooser.APPROVE_OPTION) {
			try {
				// Devuelve el fichero seleccionado
				File imagenSeleccionada = selector.getSelectedFile();
				nombreImagen = selector.getSelectedFile().getName();
				System.out.println(nombreImagen);
				// Asignamos a la variable bmp la imagen leida
				bmp = ImageIO.read(imagenSeleccionada);
			} catch (Exception e) {
			}

		}
		// Asignamos la imagen cargada a la propiedad imageActual
		imageActual = bmp;
		// Retornamos el valor
		return bmp;
	}

	public void guardarImagen() {
		// Escribimos la imagen en el archivo.
		try {
			ImageIO.write(imageActual, "jpg", new File(
					nombreImagen));
			//UNA VEZ GUARDADA CONVERTILA A BINARIO Y ENVIAR A BASE DE DATOS
			
		} catch (IOException e) {
			System.out.println("Error de escritura");
		}
	}

}
