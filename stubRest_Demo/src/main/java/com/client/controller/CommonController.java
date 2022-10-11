package com.client.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.client.model.NfeAwbModel;
import com.client.service.NfeAwbService;

@RestController
@RequestMapping("/nfe/v1")
public class CommonController {
	@Autowired
	public NfeAwbService nfeAwbService;
	@GetMapping(value="/getnfeawb/{nfeCount}")
	//public List<NfeAwbModel> nfeAwbGeneratorService(@RequestParam int nfeCount)
	public List<NfeAwbModel> nfeAwbGeneratorService(@PathVariable("nfeCount")int nfeCount) throws IOException
	{
		
	        
		return nfeAwbService.nfeAwbGeneratorService(nfeCount);
		
		
	}
	
	@RequestMapping(value = "/download", method = RequestMethod.GET)
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

 
