package Server_Application;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Server_main {
	
	ServerSocket ss = null;
	ArrayList<Client_struct> clients = new ArrayList<Client_struct>();
	public static int Loss_Ratio = 1; //loss percent

	public static void main(String[] args) {
		
		Server_main server = new Server_main();
		
		try {
			server.ss = new ServerSocket(55555);
			
			while(true) {
				
				Socket socket = server.ss.accept(); //client connect
				LocalTime cnt_time = LocalTime.now(); //check connected time
				System.out.println("Client Connected >>> "+socket.getInetAddress()); //Connected client IP
				Client_struct client = new Client_struct(socket, server.clients, cnt_time, server.clients.size()); //create client thread
				server.clients.add(client); //add client object in client list
				client.start();
				
			}
		
		}catch(SocketException e) {
			System.out.println("Server >>> 소켓 관련 예외 발생, 서버종료");
		}catch(IOException e) {
			System.out.println("Server main >>> 입출력 예외 발생");
		}
	}

}
