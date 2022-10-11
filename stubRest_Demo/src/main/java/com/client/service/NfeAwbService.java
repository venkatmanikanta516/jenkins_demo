package com.client.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.client.model.NfeAwbModel;

@Service

public class NfeAwbService {

	public List<NfeAwbModel> nfeAwbGeneratorService(int nfeCount) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		List<NfeAwbModel> sepratedList = new ArrayList<NfeAwbModel>();
		for (int i = 0; i < nfeCount; i++) {
			String nfeKey = getSaltString(44);
			map.put(nfeKey, nfeKey.substring(0, 12));

		}
		for (Map.Entry m : map.entrySet()) {
			NfeAwbModel nfeAwbModel = new NfeAwbModel();
			nfeAwbModel.setNfeKey(m.getKey());
			nfeAwbModel.setAwb(m.getValue());
			sepratedList.add(nfeAwbModel);

		}
		// saveIntoFile(sepratedList);
		poiExample(sepratedList);
		//downloadFile();
		return sepratedList;
	}

	private static String getSaltString(int valuelength) {
		String SALTCHARS = "1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < valuelength) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}

	//Save Data Into File
	private void saveIntoFile(List<NfeAwbModel> nfeAwbModelData) {
		FileOutputStream out;
		String filePath = "C:\\Users\\Mani\\Desktop\\NFEs_Stubs\\stubRest_Demo\\src\\main\\resources\\tt.txt";
		try {
			out = new FileOutputStream(filePath);
			for (NfeAwbModel nfeAws : nfeAwbModelData) {
				NfeAwbModel nfeAwbModel = new NfeAwbModel();
				nfeAwbModel.setNfeKey(nfeAws.getNfeKey());
				nfeAwbModel.setAwb(nfeAws.getAwb());
				out.write(nfeAwbModel.getNfeKey().toString().getBytes());
				out.write(nfeAwbModel.getAwb().toString().getBytes());
				// out.write("\n".getBytes());

			}

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
//Save Data Into Excel
	public void poiExample(List<NfeAwbModel> nfeAwbModelData) {

		 
		XSSFWorkbook workbook = new XSSFWorkbook();

	 
		XSSFSheet sheet = workbook.createSheet("NFE_Aws Keys");
		
		XSSFRow header = sheet.createRow(0);
		Cell headerCell = header.createCell(0);
		headerCell.setCellValue("Nfe Key");
		//headerCell.setCellStyle(headerStyle);

		headerCell = header.createCell(1);
		headerCell.setCellValue("AWS");
		//headerCell.setCellStyle(headerStyle);
	 
		Map<String, String> data = new HashMap<String, String>();

		 
		for (NfeAwbModel nfeAws : nfeAwbModelData) {
			data.put(nfeAws.getNfeKey().toString(), nfeAws.getAwb().toString());
		}

		int rows = 1;
		for (Map.Entry m : data.entrySet()) {

			Object value = null;

			XSSFRow row = sheet.createRow(rows);
			for (int cols = 0; cols < 2; cols++) {
				XSSFCell cell = row.createCell(cols);

				if (cols == 0)
					value = m.getKey();
				if (cols == 1)
					value = m.getValue();
				if (value instanceof String)
					cell.setCellValue((String) value);

			}
			rows = ++rows;
		}

		 
		try {
			String path ="C:\\Users\\Mani\\Desktop\\NFEs_Stubs\\stubRest_Demo\\src\\main\\resources\\NfeAws.xlsx";
		 
			File f = new File(path);
			String absolute = f.getAbsolutePath();
			// Writing the workbook
			FileOutputStream out = new FileOutputStream(f);
			System.out.println(f);
			workbook.write(out);

			// Closing file output connections
			out.close();

			// Console message for successful execution of
			// program
			System.out.println("NfeAws.xlsx written successfully on disk.");
		}

		// Catch block to handle exceptions
		catch (Exception e) {

			// Display exceptions along with line number
			// using printStackTrace() method
			e.printStackTrace();
		}

	}
	
	 
	public ResponseEntity<Object> downloadFile() throws IOException
	{
		String filename ="C:\\Users\\Mani\\Desktop\\NFEs_Stubs\\stubRest_Demo\\src\\main\\resources\\NfeAws.xlsx";
		 
		//String filename = "D:/work/tree.jpg";
		File file = new File(filename);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition",
				String.format("attachment; filename=\"%s\"", file.getName()));
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers)
				.contentLength(file.length())
				.contentType(MediaType.parseMediaType("application/txt")).body(resource);

		return responseEntity;
	}

}
