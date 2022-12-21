package com.h3.restful;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import java.awt.*;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.util.Base64;
import java.io.*;
import java.util.Locale;
import java.util.Scanner;


@RestController
@RequestMapping(path = "/tools")
public class RemoteAccessTool {

    @GetMapping(path = "/reboot")
    public @ResponseBody boolean reboot(){
        try{
            Runtime runtime = Runtime.getRuntime();
            String osName = System.getProperty("os.name").toLowerCase();
            if(osName.contains("windows")){
                runtime.exec("shutdown -r -t 0");
            }else if(osName.contains("linux") || osName.contains("mac os x")){
                runtime.exec("shutdown -r now"); 
            }
            return true;
        } catch(Exception e){
            return false;
        }
    }

    @GetMapping(path = "/screenshot")
    public @ResponseBody String getScreenShot(){
        try{
            System.setProperty("java.awt.headless", "false");

            Rectangle screenRect = new Rectangle(java.awt.Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage capture = new Robot().createScreenCapture(screenRect);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(capture, "jpg", baos );

            byte[] bytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(bytes);
        }catch(Exception e){
            return null;
        }
    }

    @GetMapping(path = "/process")
    public  String[] get_processes() throws Exception {

        int max_n_porcesses= 10000;
    
        String[] processes = new String[max_n_porcesses];
        String os = System.getProperty("os.name", "unknown").toLowerCase(Locale.ROOT);
    
        if(os.contains("win")){
            os = "windows";
        }else if (os.contains("mac")){
            os = "MacOS";
        }else if (os.contains("nux")){
            os = "Linux";
        }else{
            os = "other";
        }
        if(os.equals("windows")){
            try{
                Process process = Runtime.getRuntime().exec("tasklist.exe");
                Scanner scanner = new Scanner(new InputStreamReader(process.getInputStream()));
                int i = 0;
                while (scanner.hasNext()) {
                    processes[i++]=scanner.nextLine();
                }
                scanner.close();
            }catch( Exception err){
                err.printStackTrace();
            }
        }else if(os.equals("Linux")){
    
            try {
                String line;
                Process p = Runtime.getRuntime().exec("ps -e");
                BufferedReader input =new BufferedReader(new InputStreamReader(p.getInputStream()));
                int i = 0;
                while ((line = input.readLine()) != null) {
                    processes[i++]=line;
                }
                input.close();
            } catch (Exception err) {
                err.printStackTrace();
            }
        }else if(os.equals("MacOS")){
    
            try {
                String line;
                Process p = Runtime.getRuntime().exec("ps -e -o command");
                BufferedReader input =new BufferedReader(new InputStreamReader(p.getInputStream()));
                int i = 0;
                while ((line = input.readLine()) != null) {
                    processes[i++]=line;
                }
                input.close();
            } catch (Exception err) {
                err.printStackTrace();
            }
    
        }else{
            processes= null;
        }
        int i =0;
        while(true){
            if( processes[i]== null){
                break;
            }else{
                i++;
            }
        }
    
        String[] output= new String[i];
        for(int k=0;k!=i;k++){
            output[k]=processes[k]+"\n";
        }
        return output;
    
    }

    
}
