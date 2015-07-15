package sesion;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.awt.Font;

import javax.swing.SwingConstants;
import javax.swing.JButton;

import Bd.conexion;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

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

@SuppressWarnings("serial")
public class login extends JFrame {

	private JPanel contentPane;
	private JTextField _usuario;
	private conexion con;
	static JPanel panel;
	private DaemonThread myThread = null;
	int count = variables.get_id();
	public VideoCapture webSource = null;
	public Claseimagen lop = new Claseimagen();
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
										lop.guardar(frame, new Point(rect.x,
												rect.y), new Point(rect.x
												+ rect.width, rect.y
												+ rect.height));
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
							ex.printStackTrace();
							System.out.println(ex);
						}
					}
					runnable2 = false;
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
					login frame = new login();
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
	public login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 477, 485);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new TitledBorder(new LineBorder(new Color(0, 128,
				128), 4, true), "INICIAR SESI\u00D3N", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(0, 128, 128)));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton button = new JButton("INICIAR");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				con = new conexion();
				con.busqueda("usuario", "usuario", "usuario,id", _usuario
						.getText().trim());
				String resultado = con.registro_busqueda;
				if (resultado.equals("")) {
					JOptionPane
							.showMessageDialog(null, "Usuario NO registrado");
				} else {
					String[] columnas = resultado.split(",");
					if (columnas[0].equals(_usuario.getText().trim())) {
						variables.set_id(Integer.parseInt(columnas[1]));
						if(variables.getBw()!=null||variables.getGray()!=null) {
							LoginTrainingBinary Training = new LoginTrainingBinary(
									variables.get_id());
							Training.principal();
							LoginTrainingScaleGray Training2 = new LoginTrainingScaleGray(
									variables.get_id());
							Training2.principal();
						}
					} else {
						JOptionPane.showMessageDialog(null,
								"Usuario y/o contraseña/nInvalido!!");
					}
				}

			}
		});
		button.setBounds(359, 374, 91, 60);
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Tahoma", Font.BOLD, 10));
		button.setBackground(new Color(0, 128, 128));
		contentPane.add(button);

		JButton btnFoto = new JButton("Foto");
		btnFoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				myThread.runnable2 = true;
			}
		});
		btnFoto.setForeground(Color.WHITE);
		btnFoto.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnFoto.setBackground(new Color(0, 128, 128));
		btnFoto.setBounds(220, 385, 84, 36);
		contentPane.add(btnFoto);

		_usuario = new JTextField();
		_usuario.setBorder(new TitledBorder(new LineBorder(new Color(0, 128,
				128), 2, true), "USUARIO", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(0, 128, 128)));
		_usuario.setHorizontalAlignment(SwingConstants.CENTER);
		_usuario.setFont(new Font("Times New Roman", Font.BOLD, 12));
		_usuario.setBounds(20, 380, 173, 41);
		contentPane.add(_usuario);
		_usuario.setColumns(10);

		panel = new JPanel();
		panel.setBounds(10, 15, 440, 350);
		contentPane.add(panel);
		webSource = new VideoCapture(0);
		myThread = new DaemonThread();
		Thread t = new Thread(myThread);
		t.setDaemon(true);
		myThread.runnable = true;
		t.start();
	}
}
