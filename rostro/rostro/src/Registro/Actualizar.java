package Registro;

import Bd.conexion;

public class Actualizar {

	public void actualizarid() {
		try {
			conexion con = new conexion();
			con.conteo("usuario", "COUNT(*)");
			int id = Integer.parseInt(con.registro_busqueda);
			String ids[] = new String[id];
			String idbinary= Integer.toBinaryString(id);
			ids[0] = idbinary;
			for (int i = 1; i < id; i++) {
				idbinary = Integer.toBinaryString(id-i);
				ids[i]="";
				while(idbinary.length()+ids[i].length()!=ids[0].length()) {
					ids[i]+="0";
				}
				ids[i]+=idbinary;
			}
			for(int i = 0;i<id;i++){
				con.actualizar("usuario", "salida", ids[i], "id="+(id-i));
			}
			Entrenamiento training = new Entrenamiento();
			training.principal();
			Entrenamiento2 training2 = new Entrenamiento2();
			training2.principal();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
