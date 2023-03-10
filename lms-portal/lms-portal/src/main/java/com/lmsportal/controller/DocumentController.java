package com.lmsportal.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lmsportal.model.Document;
import com.lmsportal.repository.DocumentRepo;

import org.springframework.util.StringUtils;

@Controller
public class DocumentController {

	@Autowired
	private DocumentRepo documentRepo;
	
	@GetMapping("/add")
	public String getDoc(Model model)
	{
		List<Document> listDocs=documentRepo.findAll();
		model.addAttribute("doc",listDocs);
		return "document/index";
	}
	
	
	@GetMapping("/create")
	public String createDoc(Model model)	{
		
		model.addAttribute("doc",new Document());
		return "document/create";
	}
	
	@PostMapping("/upload")
	public String uploadDoc(@RequestParam("document")MultipartFile multipartFile,
			RedirectAttributes ra) throws IOException
	{
		String fileName=StringUtils.cleanPath(multipartFile.getOriginalFilename());
		Document document=new Document();
		document.setName(fileName);
		document.setContent(multipartFile.getBytes());
		document.setSize(multipartFile.getSize());
		document.setUploadTime(new Date());
		
		documentRepo.save(document);
		
		ra.addFlashAttribute("message","The file had been uploaded successfully");
		
		return "redirect:/add";
	}
	
	@GetMapping("/download")
	public void downloadFile(@Param("id")Long id,HttpServletResponse response) throws Exception
    {
       Optional<Document>result=documentRepo.findById(id);
	   if(!result.isPresent())
	   {
		   throw new Exception("Could not find document with ID:"+id);
	   }
	   Document document=result.get();
	   response.setContentType("application/octet-stream");
	   String headerKey="Content-Disposition";
	   String headerValue="attachment;filename="+document.getName();
    
	   response.setHeader(headerKey, headerValue);
	   ServletOutputStream outputStream= response.getOutputStream();
        
	   outputStream.write(document.getContent());
	   outputStream.close();
    }
	
	
}
