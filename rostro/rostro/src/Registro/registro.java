package Registro;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JLabel;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;

import Bd.conexion;

@SuppressWarnings("serial")
public class registro extends JFrame {

	private JPanel contentPane;
	private JTextField _usuario;
	private conexion con;
	private JButton btnImagen_1;
	private JTextField _password;
	private JTextField _nombreusuario;
	static JPanel panel;
	static JLabel etiqueta;
	private DaemonThread myThread = null;
	int count = variables.get_id();
	public VideoCapture webSource = null;
	public imagenes lop = new imagenes();
	Mat frame = new Mat();
	MatOfByte mem = new MatOfByte();
	CascadeClassifier faceDetector = new CascadeClassifier(
			"C:\\opencv\\sources\\data\\lbpcascades\\lbpcascade_frontalface.xml");
	MatOfRect faceDetections = new MatOfRect();

	class DaemonThread implements Runnable {
		protected volatile boolean runnable = false;
		protected volatile boolean runnable2 = false;

		@Override
		public void run() {
			synchronized (this) {
				while (runnable) {
					if (webSource.grab()) {
						try {
							Thread.sleep(10);
							webSource.retrieve(frame);
							if (runnable2) {
								faceDetector.detectMultiScale(frame,
										faceDetections);
								for (Rect rect : faceDetections.toArray()) {
									if (faceDetections.toArray().length == 1) {
										lop.guardar(frame, count,new Point(rect.x,rect.y),new Point(rect.x + rect.width,rect.y + rect.height));
										count++;
									}
									Core.rectangle(frame, new Point(rect.x,
											rect.y),
											new Point(rect.x + rect.width,
													rect.y + rect.height),
											new Scalar(0, 255, 0));
								}
							}
							Highgui.imencode(".bmp", frame, mem);
							Image im = ImageIO.read(new ByteArrayInputStream(
									mem.toArray()));
							BufferedImage buff = (BufferedImage) im;
							Graphics g = panel.getGraphics();
							if (g.drawImage(buff, 0, 0, getWidth(),
									getHeight(), 0, 0, buff.getWidth(),
									buff.getHeight(), null))

								if (runnable == false) {
									System.out.println("Going to wait()");
									this.wait();
								}
						} catch (Exception ex) {
							System.out.println("Error");
						}
					}
					variables.set_id(count);
					if (variables.get_id() == 5) {
						runnable2 = false;
					}
				}
			}
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
					registro frame = new registro();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public registro() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 692, 416);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new TitledBorder(new LineBorder(new Color(0, 128,
				128), 4, true), "INICIAR SESI\u00D3N", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(0, 128, 128)));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		panel = new JPanel();
		panel.setBounds(10, 15, 440, 350);
		panel.setLayout(new BorderLayout());

		final JButton btnImagen = new JButton("Guardar");
		btnImagen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				con = new conexion();
				try {
					if (_usuario.getText().equals("")
							|| _password.getText().equals("")) {
						JOptionPane.showMessageDialog(null,
								"Llene todos los campos");
					} else {
						con.agregar("usuario", "usuario,contrasena,salida",
								_usuario.getText().trim() + ","
										+ _password.getText().trim() + ","
										+ "  ");
						con.busquedaClausula("usuario", "usuario", "id",
								String.valueOf(_usuario.getText().trim()));
						String id = con.registro_busqueda;
						id=id.replace(",", "");
						id.trim();
						variables.setIdusuario(Integer
								.parseInt(id));
						System.out.println(variables.getIdusuario());
						for (int i = 0; i < 5; i++) {
							String gray = variables.getGray()[i];
							String bw = variables.getBw()[i];
							con.agregar("entrada", "gris,bn,idUsuario", gray
									+ "," + bw + "," + variables.getIdusuario());
						}
						Actualizar update = new Actualizar();
						update.actualizarid();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		btnImagen.setBounds(462, 210, 204, 29);
		btnImagen.setForeground(Color.WHITE);
		btnImagen.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnImagen.setBackground(new Color(0, 128, 128));
		contentPane.add(btnImagen);

		_usuario = new JTextField();
		_usuario.setBorder(new TitledBorder(new LineBorder(new Color(0, 128,
				128), 2, true), "USUARIO", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(0, 128, 128)));
		_usuario.setHorizontalAlignment(SwingConstants.CENTER);
		_usuario.setFont(new Font("Times New Roman", Font.BOLD, 12));
		_usuario.setBounds(462, 116, 204, 36);
		contentPane.add(_usuario);
		_usuario.setColumns(10);

		btnImagen_1 = new JButton("Foto");
		btnImagen_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				myThread.runnable2 = true;
				btnImagen.setEnabled(true);
			}
		});
		btnImagen_1.setForeground(Color.WHITE);
		btnImagen_1.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnImagen_1.setBackground(new Color(0, 128, 128));
		btnImagen_1.setBounds(462, 29, 95, 29);
		contentPane.add(btnImagen_1);

		_password = new JTextField();
		_password.setHorizontalAlignment(SwingConstants.CENTER);
		_password.setFont(new Font("Times New Roman", Font.BOLD, 12));
		_password.setColumns(10);
		_password.setBorder(new TitledBorder(new LineBorder(new Color(0, 128,
				128), 2, true), "CONTRASE\u00D1A", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(0, 128, 128)));
		_password.setBounds(462, 163, 204, 36);
		contentPane.add(_password);

		_nombreusuario = new JTextField();
		_nombreusuario.setHorizontalAlignment(SwingConstants.CENTER);
		_nombreusuario.setFont(new Font("Times New Roman", Font.BOLD, 12));
		_nombreusuario.setColumns(10);
		_nombreusuario.setBorder(new TitledBorder(new LineBorder(new Color(0,
				128, 128), 2, true), "NOMBRE DE USUARIO", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(0, 128, 128)));
		_nombreusuario.setBounds(462, 69, 204, 36);
		contentPane.add(_nombreusuario);
		contentPane.add(panel);
		etiqueta = new JLabel();
		panel.add(etiqueta, BorderLayout.CENTER);
		webSource = new VideoCapture(0);
		myThread = new DaemonThread();
		Thread t = new Thread(myThread);
		t.setDaemon(true);
		myThread.runnable = true;
		t.start();
	}
}
