package Registro;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JLabel;

import Bd.conexion;
import Clases.Imagen;


@SuppressWarnings("serial")
public class registro extends JFrame {

	private JPanel contentPane;
	private JTextField _usuario;
	private conexion con;
	private JButton btnImagen_1;
	private JTextField _password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
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
		setBounds(100, 100, 421, 265);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new TitledBorder(new LineBorder(new Color(0, 128, 128), 4, true), "INICIAR SESI\u00D3N", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 128, 128)));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnImagen = new JButton("Guardar");
		btnImagen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				con= new conexion();
				try {
					if(_usuario.getText().equals("")||_password.getText().equals(""))
					{
						JOptionPane.showMessageDialog(null, "Llene todos los campos");
					}
					else
					{
						con.agregar("usuario", "usuario,contrasena,entrenamiento", _usuario.getText().trim()+","+_password.getText().trim()+", 1 1 1 1 1 1 1");
						
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		});
		btnImagen.setBounds(61, 146, 91, 60);
		btnImagen.setForeground(Color.WHITE);
		btnImagen.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnImagen.setBackground(new Color(0, 128, 128));
		contentPane.add(btnImagen);
		
		_usuario = new JTextField();
		_usuario.setBorder(new TitledBorder(new LineBorder(new Color(0, 128, 128), 2, true), "USUARIO", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 128, 128)));
		_usuario.setHorizontalAlignment(SwingConstants.CENTER);
		_usuario.setFont(new Font("Times New Roman", Font.BOLD, 12));
		_usuario.setBounds(24, 40, 173, 36);
		contentPane.add(_usuario);
		_usuario.setColumns(10);
		
		final JLabel _foto = new JLabel("New label");
		_foto.setBorder(new LineBorder(new Color(0, 128, 128), 2, true));
		_foto.setBounds(207, 21, 173, 150);
		contentPane.add(_foto);
		
		btnImagen_1 = new JButton("Imagen");
		btnImagen_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Imagen ObjProcesamiento= new Imagen();
				ImageIcon imagen = new ImageIcon(ObjProcesamiento.abrirImagen());
	            Icon icono = new ImageIcon(imagen.getImage().getScaledInstance(_foto.getWidth(), _foto.getHeight(), Image.SCALE_DEFAULT));
	            _foto.setIcon(icono);
			}
		});
		btnImagen_1.setForeground(Color.WHITE);
		btnImagen_1.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnImagen_1.setBackground(new Color(0, 128, 128));
		btnImagen_1.setBounds(255, 177, 91, 29);
		contentPane.add(btnImagen_1);
		
		_password = new JTextField();
		_password.setHorizontalAlignment(SwingConstants.CENTER);
		_password.setFont(new Font("Times New Roman", Font.BOLD, 12));
		_password.setColumns(10);
		_password.setBorder(new TitledBorder(new LineBorder(new Color(0, 128, 128), 2, true), "CONTRASE\u00D1A", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 128, 128)));
		_password.setBounds(24, 86, 173, 36);
		contentPane.add(_password);
	}
}
