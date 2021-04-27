package Clinet_Application;

import java.net.Socket;
import java.util.Scanner;


public class Client_main {

	Socket client_socket = null;
	
	public static void main(String[] args) {

		System.out.print("Enter the CID : ");
		Scanner scanner = new Scanner(System.in);
		String CID = scanner.nextLine(); //Enter the CID to user
		
//		System.out.println(CID);
		Client_main client = new Client_main();
		try {
			client.client_socket = new Socket(/*"210.119.32.221"*/"localhost",55555); //server connect
			System.out.println("Connected Server");
			
			Client_receive client_reciever = new Client_receive(client.client_socket);
			Client_send client_sender = new Client_send(client.client_socket, CID);
			
			client_reciever.start();
			client_sender.start();
			
		} catch (Exception e) {
			System.out.println("Connection Fail");
		}

	}

}
