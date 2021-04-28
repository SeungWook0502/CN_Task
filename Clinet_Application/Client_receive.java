package Clinet_Application;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

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
				String msg = din.readUTF(); //Response message
				System.out.println("Client's receive message - "+msg); //message form check
				System.out.println("Server >>> "+msg.split("///")[2]);
				if(msg.split("///")[1].equals("250")) { ///정상적으로 Quit된 경우
					Quit = true; //Thread while terminate
				}
					
					
			}
		} catch (IOException e) {
			System.out.println("Client >>> 입력 예외 발생");
		}
		
		
	}
}
