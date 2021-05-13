package Clinet_Application;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Base64;

public class Client_receive extends Thread{
	
	Socket socket = null;
	
	public Client_receive(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		
		DataInputStream din;
		try {
			din = new DataInputStream(socket.getInputStream());
			boolean Quit = false;
			while(!Quit) {
				String Response_msg = din.readUTF(); //Response message
				System.out.println("Base64 Response Message - "+Response_msg); //Base64 Response message check
				Response_msg=Base64_Decoding(Response_msg);
				System.out.println("Client's receive message - "+Response_msg+"\n============================================================="); //message form check
				System.out.println("Server >>> "+Response_msg.split("///")[2]);
				if(Response_msg.split("///")[1].equals("250")) { ///정상적으로 Quit된 경우
					Quit = true; //Thread while terminate
				}
			}
		} catch (IOException e) {
			System.out.println("Client >>> 입력 예외 발생");
		}
	}
	public String Base64_Decoding(String String_msg) {
		char[] c_msg = String_msg.toCharArray();
		char[] Base64_Table = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3','4','5','6','7','8','9','+','/'};
		String Base64_StringTable = new String(Base64_Table);
		String Base64_Value_List="";
		String ASCII_Value="";
		
		for(int i=0;i<c_msg.length;i++) {
			String binary_string="";
			if(c_msg[i]=='=') { //Base64 '=' 변환
				binary_string="00";
			}
			else{ //'=' 아닌경우
				binary_string = Integer.toBinaryString(Base64_StringTable.indexOf(c_msg[i]));
			
				for(int j=0;binary_string.length()<6;j++) { //부족한 5자리 0으로 채우기
					binary_string="0" + binary_string;
				}
			}
			Base64_Value_List += binary_string;
		}
		for(int i=0;i<Base64_Value_List.length();i=i+8) {
			String Base64_Value="";
			try {
			for(int j=0; j<8;j++) {
				Base64_Value+=Base64_Value_List.charAt(i+j);
			}
			int ASCII_int=Integer.parseInt(Base64_Value,2);
			ASCII_Value+=String.valueOf((char)ASCII_int);
			}catch(StringIndexOutOfBoundsException e) {}
		}
		return ASCII_Value;
	}
}
