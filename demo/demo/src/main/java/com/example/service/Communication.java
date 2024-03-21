package com.example.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.Socket;

//@Service
public class Communication {

    public Socket clientSocket;
    private final String host = "127.0.0.1";//43.253
    private final int port = 12888;

    public Communication() throws IOException {
        this.clientSocket = new Socket(host, port);
    }

    public String getResult(String fileName) throws IOException {
        int bufferSize = 1024;

        File file = new File(fileName);
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] fileData = new byte[bufferSize];
        int length;

        // 分多次发送文件内容
        OutputStream outputStream = clientSocket.getOutputStream();
        while ((length = fileInputStream.read(fileData)) != -1) {
            outputStream.write(fileData, 0, length);
            System.out.println("已发送" + length + "字节");
        }
        System.out.println("文件已发送");

        // 发送文件名
        outputStream.write(fileName.getBytes());
        clientSocket.shutdownOutput();

        // 接收服务器返回的字符串
//        InputStream inputStream = clientSocket.getInputStream();
//        byte[] data = new byte[bufferSize];
//        length = inputStream.read(data);
//        String strData = new String(data, 0, length);

        // 运行本地python
        Process proc1, proc2;
        try {
            proc1 = Runtime.getRuntime().exec("conda activate infant_cry");//激活conda
            proc2 = Runtime.getRuntime().exec("python C:\\test.py");// 执行py文件
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc2.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc2.waitFor();
        }
        catch (IOException e) {e.printStackTrace();}
        catch (InterruptedException e) {e.printStackTrace();}


        String strData = "宝宝饿了";
        return strData;
    }
}
