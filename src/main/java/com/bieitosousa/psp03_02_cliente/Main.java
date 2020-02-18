/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bieitosousa.psp03_02_cliente;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bieito
 */
public class Main {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {

        }

    }

    public void startIN() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void startOUT() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String sendMessage(String msg) {
        try {

            out.println(msg);

            String a = in.readLine();
            System.out.println("send mensaje responde :[CLI] lee {" + a + "}");
            return a;
        } catch (Exception e) {
            return null;
        }
    }

    public String sendMessage(int msg) {
        try {

            out.println(msg);

            String a = in.readLine();
            System.out.println("send mensaje responde :[CLI] lee {" + a + "}");
            return a;
        } catch (Exception e) {
            return null;
        }
    }

    public String readMessage() {
        try {
            String a = in.readLine();
            System.out.println("[CLI] lee {" + a + "}");
            return a;
        } catch (Exception e) {
            return null;
        }
    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {

        }

    }

    public static void main(String[] arg) {
     Main m = new Main();
     m.programFile();

    }

    

    public boolean programFile() { // prueva con strings
        try{
        final String cabecera = "[CLI] mensaje:";
        final String errorSend = "send_error";
        final String errorReader = "reader_error";
        final String errorSearch = "search_error";
        String filename = "prueba.txt";
        final String ok = "ok";
        final String error = "error";
        startConnection("localhost", 1500);
        String msn = cabecera + filename;
        String l;
        if ((l = sendMessage(msn)).contains(error)) {
            System.out.println("[CLI_RECIBE]:"+l);
            if (l.contains(errorSend)) {
                System.out.println("ERROR: enviando el archivo");
            } else if (l.contains(errorReader)) {
                System.out.println("ERROR: leyendo el mensaje");
            } else if (l.contains(errorSearch)) {
                System.out.println("No se a encontrado el archivo " + filename + " en el servidor");
            } else {
                System.out.println("ERROR: inesperado");
            }
        } else if (l.contains(ok)) {
            System.out.println("INtercambiando archivos");
            takeFile();
            readFile(new File(".\\" + filename));
        } else {
             System.out.println("ERROR: inesperado");
        }
        System.out.println("[CLI]fin");
        
    } catch (Exception e) {
            System.err.println(e);
            return false;

        }finally{
        stopConnection();
        }
        return true;
    }

    public boolean takeFile() {
        DataOutputStream output;
        BufferedInputStream bis;
        BufferedOutputStream bos;

        byte[] receivedData;
        int in;
        String file;

        try {

            while (true) {
                System.out.println("CLI INTENTA RECOGER ARCHIVO");
                //Aceptar conexiones
                //Buffer de 1024 bytes
                receivedData = new byte[1024];
                bis = new BufferedInputStream(clientSocket.getInputStream());
                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                //Recibimos el nombre del fichero
                file = dis.readUTF();
                file = file.substring(file.indexOf('\\') + 1, file.length());
                //Para guardar fichero recibido
                bos = new BufferedOutputStream(new FileOutputStream(file));
                while ((in = bis.read(receivedData)) != -1) {
                    bos.write(receivedData, 0, in);
                }
                bos.close();
                dis.close();

            }
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    private boolean readFile(File file) {
        if (file.exists()) {
            try {
                Scanner input = new Scanner(file);
                while (input.hasNextLine()) {
                    String line = input.nextLine();
                    System.out.println(line);
                }
                input.close();
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }

        } else {
            System.out.println("EL ARCHIVO " + file + " NO EXISTE");
            return false;
        }

    }

}
