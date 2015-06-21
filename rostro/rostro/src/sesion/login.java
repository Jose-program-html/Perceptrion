package Sesion;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
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

import javax.swing.JPasswordField;

public class login extends JFrame {

	private JPanel contentPane;
	private JTextField _usuario;
	private conexion con;
	private JPasswordField _password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
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
		setBounds(100, 100, 363, 180);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new TitledBorder(new LineBorder(new Color(0, 128, 128), 4, true), "INICIAR SESI\u00D3N", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 128, 128)));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton button = new JButton("INICIAR");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				con= new conexion();
				con.busqueda("usuario", "usuario", "usuario,contrasena", _usuario.getText().trim());
				String resultado=con.registro_busqueda;
				if(resultado.equals(""))
				{
					JOptionPane.showMessageDialog(null, "Usuario NO registrado");
				}
				else
				{
					String[] columnas = resultado.split(",");
					if(columnas[0].equals(_usuario.getText().trim())&&columnas[1].equals(_password.getText().trim()))
					{
						//ABRIR VENTANA CAMARA
						//Ventana v= new Ventana();
						JOptionPane.showMessageDialog(null, resultado);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Usuario y/o contraseña/nInvalido!!");
					}
				}
				
			}
		});
		button.setBounds(225, 40, 91, 60);
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Tahoma", Font.BOLD, 10));
		button.setBackground(new Color(0, 128, 128));
		contentPane.add(button);
		
		_usuario = new JTextField();
		_usuario.setBorder(new TitledBorder(new LineBorder(new Color(0, 128, 128), 2, true), "USUARIO", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 128, 128)));
		_usuario.setHorizontalAlignment(SwingConstants.CENTER);
		_usuario.setFont(new Font("Times New Roman", Font.BOLD, 12));
		_usuario.setBounds(24, 21, 173, 36);
		contentPane.add(_usuario);
		_usuario.setColumns(10);
		
		_password = new JPasswordField();
		_password.setHorizontalAlignment(SwingConstants.CENTER);
		_password.setFont(new Font("Times New Roman", Font.BOLD, 12));
		_password.setBorder(new TitledBorder(new LineBorder(new Color(0, 128, 128), 2, true), "CONTRASE\u00D1A", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 128, 128)));
		_password.setBounds(24, 68, 173, 36);
		contentPane.add(_password);
	}
}
