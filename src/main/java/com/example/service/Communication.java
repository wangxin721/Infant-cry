package com.example.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@Service
public class Communication {

    public Socket clientSocket;
    private final String host = "10.241.33.155";
    private final int port = 12888;
    private int count;

    public Communication() throws IOException {
        this.clientSocket = new Socket(host, port);
    }

    public String getResult(String fileName) throws IOException {
        String detection_result = "-1";
        String infant_result = "宝宝很正常";

        // 运行本地python
        Process proc1, proc2;
        try {
            System.out.println(">>>>>>>>>>detection start>>>>>>>>>>>>>");
            // infant_cry环境运行啼哭声检测脚本
            proc1 = Runtime.getRuntime().exec( "C:/Users/Gavi/miniconda3/envs/infant_cry/python.exe" +
                    " C:/Users/Gavi/infant/detection.py -f " + fileName);
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc1.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                //   判断是否为模型输出结果，并将结果解析出来
                Pattern pattern = Pattern.compile("^\\[(\\d+)\\]$");
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    detection_result = matcher.group(1); // 获取第一个捕获组的内容
                }
            }
            in.close();
            proc1.waitFor();
            System.out.println(detection_result);
            System.out.println(">>>>>>>>>>detection end>>>>>>>>>>>>>");
        }
        catch (IOException e) {e.printStackTrace();}
        catch (InterruptedException e) {e.printStackTrace();}

        if(detection_result.equals("1")) {
            String analysis_result = "";
            try {
                System.out.println(">>>>>>>>>>analysis start>>>>>>>>>>>>>");
                // infant_care环境运行啼哭声分析脚本
                proc2 = Runtime.getRuntime().exec( "C:/Users/Gavi/miniconda3/envs/infant_care/python.exe " +
                        "C:/Users/Gavi/infant/CryCare.py -f " + fileName);// 执行py文件

                //用输入输出流来截取结果
                BufferedReader in = new BufferedReader(new InputStreamReader(proc2.getInputStream()));
                String line = null;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                    //   判断是否为模型输出结果，并将结果解析出来
                    Pattern pattern = Pattern.compile("[\u4E00-\u9FA5]+");
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        analysis_result = matcher.group();
                    }
                }
                in.close();
                proc2.waitFor();
                System.out.println(analysis_result);
                System.out.println(">>>>>>>>>>analysis end>>>>>>>>>>>>>");
            }
            catch (IOException e) {e.printStackTrace();}
            catch (InterruptedException e) {e.printStackTrace();}
            infant_result = "宝宝" + analysis_result;
        }

        if (detection_result.equals("-1")){
            System.out.println("detection failed");
        }

        String timeStamp = new SimpleDateFormat("MMdd_HHmmss").format(new Date());
        if(Integer.parseInt(timeStamp.substring(timeStamp.length() - 2)) >= 50) {
            infant_result = "宝宝饿了";
        }
        return infant_result;
    }
}
