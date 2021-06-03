package Clinet_Application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import Base64_Application.Base64_Decoder;
import Base64_Application.Base64_Encoder;
import Loss_Simulator.Loss_Simulator_Object;

public class Client_receive extends Thread{
	
	Socket socket = null;
	
	public Client_receive(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		
		DataInputStream dis;
		DataOutputStream dos;
		try {
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			Loss_Simulator_Object lso = new Loss_Simulator_Object();
			
//			while(!Client_main.Quit) {
			for(int Client_Receive_Count=0; Client_Receive_Count<10; Client_Receive_Count++) {
				String Response_msg = dis.readUTF(); //Receive message
				Base64_Decoder base64_Decoder = new Base64_Decoder();
				Base64_Encoder base64_Encoder = new Base64_Encoder();
				Response_msg=base64_Decoder.Base64_Decoding(Response_msg);
				
				if(Response_msg.split("///")[0].equals("ACK")) { //Receive ACK message
					
					Client_main.Receive_ACK = true;
				}
				
				else if(Response_msg.split("///")[0].equals("Response") && Client_main.Receive_ACK) { //Receive Response message
					
//					System.out.println("Response OK"); //message form check
					Client_main.Receive_Response = true;
					if(Response_msg.split("///")[1].equals("200")) { ///Receive Quit message
						for(int Client_Count = 0;Client_Count<Response_msg.split("///")[2].split("\n").length;Client_Count++) {
//							System.out.println("Server >>> "+Response_msg.split("///")[2].split("\n")[Client_Count]);
						}
					}
					else if(Response_msg.split("///")[1].equals("250")) { ///Receive Quit message
//						System.out.println("Server >>> "+Response_msg.split("///")[2]);
						Client_main.Quit = true; //Thread while terminate
					}
					else {
//						System.out.println("Server >>> "+Response_msg.split("///")[2]);
					}
				}
			}
			Client_main.Quit = true; //Thread while terminate
		} catch (IOException e) {
			System.out.println("Client >>> 입력 예외 발생");
			return;
		}
	}
}
