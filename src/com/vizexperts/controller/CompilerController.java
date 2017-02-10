/**
 * 
 */
package com.vizexperts.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Raji
 *
 */
@Controller
public class CompilerController {
	
	@RequestMapping(value="/",method=RequestMethod.GET)
	public String mainPage(){
		
		
		return "sample";
	}

	@RequestMapping(value="/runCode",method=RequestMethod.POST)
	@ResponseBody
	public String runCode(@RequestBody String data){
		JSONObject json=null;
		String snipTitle=null;
		String code=null;
		String codeType=null;
		BufferedWriter bfw=null;
		StringBuffer finaloutput=null;
		if(null != data){
			try {
				json=(JSONObject) new JSONParser().parse(data);
				if(json.containsKey("snipTitle")){
					snipTitle=(String) json.get("snipTitle");
				}
				if(json.containsKey("code")){
					code=(String) json.get("code");
				}
				if(json.containsKey("codeType")){
					codeType=(String) json.get("codeType");
				}
				if(snipTitle.equals("")){
					snipTitle="temp";
				}
				if(codeType.contains("python")){
					bfw=new BufferedWriter(new FileWriter(new File(snipTitle+".py")));
					bfw.write(code);
					bfw.close();
					String command = "python " + snipTitle+".py";
					finaloutput=compileAndRun(command, false );
				}
				if(codeType.contains("c++")){
					bfw=new BufferedWriter(new FileWriter(new File(snipTitle+".cpp")));
					bfw.write(code);
					bfw.close();
					String command = "g++ " + snipTitle+".cpp"; 
					finaloutput=compileAndRun(command , true );	
				}
				return finaloutput.toString();
			} catch (ParseException e) {
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
			}
		}
		return null;
		
	}
	
	private StringBuffer compileAndRun(String command , boolean compile) {
		
		
		                        Runtime rt = Runtime.getRuntime();
		                        Process proc, proc1;
		                        BufferedReader reader;
								try {
										proc = rt.exec(command);
										proc.waitFor();
				                        StringBuffer output = new StringBuffer();
				                        BufferedReader error = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
				                        String line = "";  
				                        String cmd ="";
				                        while ((line = error.readLine())!= null) {
				                                output.append(line + "\n");
				                        }
				                        if (compile) {
				                        	File f = new File("a.exe");
				                        	File f1 = new File("a.out");
				                        	if (f.exists()) {
				                        		cmd = "./a.exe";
				                        	} else if (f1.exists()) {
				                        		cmd = "./a.out";			
				                        	}
				                        	if (cmd.equals("")) {
				                        		return output;
				                        	} else {
				                        		proc1 = rt.exec(cmd);
				                        		proc1.waitFor();
				                        		reader = new BufferedReader(new InputStreamReader(proc1.getInputStream()));
				                        		StringBuffer output1 = new StringBuffer();
				                        		while ((line = reader.readLine())!= null) {
				                        			output1.append(line + "\n");
				                        		}
				                        		if (f.exists()) {
				                        			f.delete();
				                        		} else if (f1.exists()) {
				                        			f1.delete();
				                        		}
				                        		return output1;
				                        	}
				                        } else {
				                        	reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				                        	while ((line = reader.readLine())!= null) {
			                        			output.append(line + "\n");
				                        	}
				                        	return output;
				                        }
				                        
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								return null;
		                       
	}
	
}
