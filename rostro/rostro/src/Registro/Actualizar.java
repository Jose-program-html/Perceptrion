package Registro;

import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opencv.core.Core;

import Bd.conexion;

public class Actualizar {

	public static void main(String[] args) {
		try {
			conexion con = new conexion();
			con.conteo("usuario", "COUNT(*)");
			int id = Integer.parseInt(con.registro_busqueda);
			int numeroIdMax = id;
			int longitudIdMaxBin = 0;
			String numeroIdMaxBin = Integer.toBinaryString(numeroIdMax);
			longitudIdMaxBin = numeroIdMaxBin.length();

			System.out.print("numero id mayor " + numeroIdMaxBin);
			for (int i = 1; i <= numeroIdMax; i++) {
				String idBinario = Integer.toBinaryString(i);
				if (idBinario.length() < longitudIdMaxBin) {
					int numeroCeros = longitudIdMaxBin - idBinario.length();
					StringBuffer sb = new StringBuffer(numeroCeros);
					for (int j = 0; j < numeroCeros; j++) {
						sb.append("0");
					}
					String idBinarioAct = sb.toString().concat(idBinario);
					System.out.print("\n" + idBinarioAct);
				}
			/*String ids[] = new String[id];
			String idbinary;
			int numero, exp, digito;
			double binario;
			exp = 0;
			binario = 0;
			numero = id;
			while (numero != 0) {
				digito = numero % 2;
				binario = binario + digito * Math.pow(10, exp);
				exp++;
				numero = numero / 2;
			}
			idbinary = "" + Math.round(binario);
			ids[0] = idbinary;
			for (int i = 1; i < id; i++) {
				exp = 0;
				binario = 0;
				numero=id-i;
				while (numero != 0) {
					digito = numero % 2;
					binario = binario + digito * Math.pow(10, exp);
					exp++;
					numero = numero / 2;
				}
				idbinary = "" + Math.round(binario);
				while(idbinary.length()+ids[i].length()!=ids[0].length()) {
					ids[i]+="0";
				}
				ids[i]+=idbinary;*/
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actualizarid() {

	}

}
