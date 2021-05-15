package Clinet_Application;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import Base64_Application.Base64_Decoder;

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
				Base64_Decoder base64_Decoder = new Base64_Decoder();
				Response_msg=base64_Decoder.Base64_Decoding(Response_msg);
				System.out.println("Client's receive message - "+Response_msg+"\n============================================================="); //message form check
				System.out.println("Server >>> "+Response_msg.split("///")[2]);
				if(Response_msg.split("///")[1].equals("250")) { ///���������� Quit�� ���
					Quit = true; //Thread while terminate
				}
			}
		} catch (IOException e) {
			System.out.println("Client >>> �Է� ���� �߻�");
		}
	}
}
