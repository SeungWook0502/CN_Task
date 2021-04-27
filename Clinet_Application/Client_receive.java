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
			
			while(true) {
				String msg = din.readUTF();
//				System.out.println("Client's receive message - "+msg);
				System.out.println(msg.split("///")[2]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}
