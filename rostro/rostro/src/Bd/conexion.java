package Bd;

import java.sql.*;
import Configuracion.variablesGenerales;


public class conexion {

	Connection con = null;
	variablesGenerales vg = new variablesGenerales();
	String bd, user, pass;
	public String registro_busqueda = "";
	public int registro_busquedaInt = 0;

	// Constructor e inicializacion de variables.
	public conexion() {
		bd = vg.baseDeDatos;
		user = vg.usuarioMysql;
		pass = vg.passMysql;
	}

	// Metodo que realiza la conexion con la BD.
	public Connection conectar() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"
					+ bd, user, pass);
			if (con != null) {
				System.out.println("**Conectados");
			}
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Error en la conexion:\n" + e);
		}
		return con;
	}

	// Desconecto con el gestor de la BD.
	public void desconectar() {
		con = null;
		System.out.println("**Desconectado");
	}

	// Recibe el nombre de la tabla, los campos a tratar, y los valores a
	// agregar
	public boolean agregar(String tabla, String campos, String valores) {
		System.out.println("Entramos a agregar");
		String query = "INSERT INTO " + tabla;
		String[] camp = campos.split(",");
		int max = camp.length;

		query += campos(max, camp);
		System.out.println("Despues de query +  campos");
		query += valores(max);
		System.out.println(query);
		if (prepararEstados(query, valores)) {
			// Devuelve verdadero cuando ha surgido algun error
			System.out.println("agregar falso");
			return false;
		} else {
			// Devuelve falso cuando todo ha salido bien
			System.out.println("agregar verdadero");
			return true;
		}
	}

	// Recibe el nombre de la tabla,los campos a tratar,los valores a actualiza
	// y la clausula a cumplir
	public boolean actualizar(String tabla, String campos, String valores,
			String clausula) {
		String[] camp;
		int max;
		camp = campos.split(",");
		max = camp.length;
		String c;
		String query = "UPDATE " + tabla + " SET ", aux = "";
		if (clausula == null) {
			c = "1";
		} else {
			c = clausula;
		}
		for (int i = 0; i < max; i++) {
			aux += camp[i] + "=?";
			if (i != max - 1) {
				aux += ",";
			}
		}
		query += aux + " WHERE " + c;

		if (prepararEstados(query, valores)) {
			// Devuelve verdadero cuando ha surgido algun error
			System.out.println("actualizar falso");
			return false;
		} else {
			// Devuelve falso cuando todo ha salido bien
			System.out.println("actualizar verdadero");
			return true;
		}
	}

	// Recibe el nombre de la tabla y la clausula que cumple que row eliminar
	public boolean eliminar(String tabla, String clausula) {
		String query = "DELETE FROM " + tabla + " WHERE " + clausula;
		if (prepararEstados(query, null)) {
			// Devuelve verdadero cuando ha surgido algun error
			System.out.println("eliminar falso");
			return false;
		} else {
			// Devuelve falso cuando todo ha salido bien
			System.out.println("eliminar verdadero");
			return true;
		}
	}

	// Recoje el query predesarrollado y los valores a manejar en una query de
	// mysql
	// y posteriormente los ejecuta.
	// Si values viene como null, esta echo para ELIMINAR
	private boolean prepararEstados(String query, String values) {
		PreparedStatement pstm;
		boolean r = true;
		String[] val = null;
		int max;
		if (values != null) {
			val = values.split(",");
			max = val.length;
			try {
				pstm = (PreparedStatement) conectar().prepareStatement(query);
				for (int i = 0; i < max; i++) {
					pstm.setString(i + 1, val[i]);
				}
				r = pstm.execute();
				pstm.close();
			} catch (Exception e) {
				System.out
						.println("Problemas de sincronizacion con la base de datos:\n"
								+ e);
			}
		} else {
			try {
				pstm = (PreparedStatement) conectar().prepareStatement(query);
				r = pstm.execute();
				pstm.close();
			} catch (Exception e) {
				System.out.println("Problemas al preparar estados:\n" + e);
			}
		}
		desconectar();
		return r;
	}

	private ResultSet prepararEstados(String query) {
		ResultSet res = null;
		try {
			PreparedStatement pstm = (PreparedStatement) conectar()
					.prepareStatement(query);
			res = pstm.executeQuery();
		} catch (SQLException e) {
			System.out.println("Problemas al preparar estados en leerDatos:\n"
					+ e);
		}
		desconectar();
		return res;
	}

	private String campos(int max, String[] camp) {
		String str = "(";
		for (int i = 0; i < max; i++) {
			str += camp[i];
			if (i != max - 1) {
				str += ",";
			}
		}
		return str;
	}

	private String valores(int max) {
		String str = ") VALUES (";
		for (int i = 0; i < max; i++) {
			str += "?";
			if (i != max - 1) {
				str += ",";
			}
		}
		str += ")";
		return str;
	}

	// Realiza busquedas en la BD con la informacion proporcionada y clausula.
	public void busqueda(String tabla, String campoBusqueda, String campos,
			String clausula) {
		try {
			String[] columnas = campos.split(",");
			int max = columnas.length;
			registro_busqueda = "";

			PreparedStatement pstm = (PreparedStatement) conectar()
					.prepareStatement(
							"SELECT " + campos + " FROM " + tabla + " WHERE "
									+ campoBusqueda + "= '" + clausula + "';");
			ResultSet res = pstm.executeQuery();
			res.next();
			for (int i = 0; i < max; i++) {
				if (i < max - 1) {
					registro_busqueda += res.getString(columnas[i]) + ",";
				} else if (i == max - 1) {
					registro_busqueda += res.getString(columnas[i]);
				}
			}
			res.close();
		} catch (Exception e) {
			// System.out.println("Problemas al obtener la informacion en la Busqueda:\n"
			// + e);
		}
	}
	
	public void conteo(String tabla, String campos) {
		try {
			registro_busqueda = "";
			PreparedStatement pstm = (PreparedStatement) conectar()
					.prepareStatement(
							"SELECT " + campos + " FROM " + tabla + ";");
			ResultSet res = pstm.executeQuery();
			while (res.next()) {
				registro_busqueda += res.getString(campos);
			}
			res.close();
		} catch (Exception e) {
			// System.out.println("Problemas al obtener la informacion en la Busqueda:\n"
			// + e);
		}
	}

	// Realiza varias busquedas en la BD con la informacion proporcionada y
	// clausula.
	public void busquedaClausula(String tabla, String campoBusqueda, String campos,
			String clausula) {
		try {
			registro_busqueda = "";
			PreparedStatement pstm = (PreparedStatement) conectar()
					.prepareStatement(
							"SELECT " + campos + " FROM " + tabla + " WHERE "
									+ campoBusqueda + "= '" + clausula + "';");
			ResultSet res = pstm.executeQuery();
			while (res.next()) {
				registro_busqueda += res.getString(campos) + ",";
			}
			res.close();
		} catch (Exception e) {
			System.out
					.println("Problemas al obtener la informacion en la Busqueda1:\n"
							+ e);
		}
	}

	// Realiza busquedas generales en la BD con la informacion proporcionada no
	// tiene una clausula.
	public void busquedaG(String tabla, String campos) {
		try {
			registro_busqueda = "";
			PreparedStatement pstm = (PreparedStatement) conectar()
					.prepareStatement(
							"SELECT " + campos + " AS A FROM " + tabla + ";");
			ResultSet res = pstm.executeQuery();
			while (res.next()) {
				registro_busqueda += res.getString("A") + ",";
			}
			res.close();
		} catch (Exception e) {
			System.out
					.println("Problemas al obtener la informacion en la BusquedaG:\n"
							+ e);
		}
	}

}
