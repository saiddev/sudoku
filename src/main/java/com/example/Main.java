package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

import org.apache.commons.io.IOUtils;

import com.example.AAAAA.BBBBBB;

public class Main {

	public static void main(String[] args) throws Exception {
		try {
			AAAAA aa = new AAAAA();
			BBBBBB bb = new BBBBBB();
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 10);
			cal.set(Calendar.MINUTE, 22);
			System.out.println(cal.getTimeInMillis());
//			String sets="set MY_BAT=\"C:/webs/carrefour-france/crf-fra/crf-fra-bo-batch/src/main/resources/script/IntegStock.bat \"";
//			sets+="\nset INPUT=\"C:\\webs\\carrefour-france\\crf-fra\\crf-fra-bo-batch\\src\\test\\resources\\tmp\\ST101\\20170306-153646-1/stock.flat.text \"";
//			sets+="\nset FILEDS=\"PRODUCT_EXT_REF,STORE_EXT_REF,STOCK_CALCULATION_DATE,QTY,DATE_DEBUT,DATE_FIN,TRANCHE\"";
//			String command =sets+"\n%MY_BAT% "
//					+ "-f "
//					+ "%INPUT% "
//					+ "-t TMP_FLOW_STOCK  "
//					+ "-c %FILEDS% "
//					+ "-n IntegStock ";
//			
////			String command ="C:/webs/carrefour-france/crf-fra/crf-fra-bo-batch/src/main/resources/script/IntegStock.bat "
////					+ "-f "
////					+ "C:\\webs\\carrefour-france\\crf-fra\\crf-fra-bo-batch\\src\\test\\resources\\tmp\\ST101\\20170306-153646-1/stock.flat.text "
////					+ "-t TMP_FLOW_STOCK  "
////					+ "-c \"PRODUCT_EXT_REF,STORE_EXT_REF,STOCK_CALCULATION_DATE,QTY,DATE_DEBUT,DATE_FIN,TRANCHE\" "
////					+ "-n IntegStock ";
//					//+ "integration des stocks pour C:\webs\carrefour-france\crf-fra\crf-fra-bo-batch\src\test\resources\tmp\ST101\20170306-153646-1";
//			Process p = execShell("c:/test.bat" );
//			int exitValue = p.waitFor();
//			String error = IOUtils.toString(p.getErrorStream(), "UTF-8");
//			p.destroy();
//
//			
			
			float f = 0.0f;
			Long ll = 2083922l;
			System.out.println(""+(ll == 2083922)+"####"+(ll.equals(2083922)));
		} catch (Exception e) {
			throw e;
		}
	}
	
	private static Process execShell(String command) throws IOException {
		Runtime r = Runtime.getRuntime();
		System.out.println("Appel au shell " + command + " integration des stocks pour ");
		Process p = r.exec(command);
		BufferedReader reader = 
			    new BufferedReader(new InputStreamReader(p.getInputStream()));
		String l;
		while((l = reader.readLine()) != null){
			System.out.println(l);
		}
		return p;
	}

}

class AAAAA{
	static class BBBBBB{
		
	}
}
