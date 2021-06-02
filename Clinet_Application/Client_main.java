package Clinet_Application;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Client_main {

	Socket client_socket = null;
//	static LocalTime Timer;
	static boolean Quit = false; //Client loop
	static boolean Receive_ACK = false; //Receive ACK
	static boolean Receive_Response = false; //Receive response
	
	public static void main(String[] args) {

		System.out.print("Enter the CID : ");
		Scanner scanner = new Scanner(System.in);
		String CID = scanner.nextLine(); //Enter the CID to user
		
//		System.out.println(CID);
		Client_main client = new Client_main();
		
		try {
			client.client_socket = new Socket(/*"210.119.32.221"*/"localhost",55555);
			System.out.println("Connected Server");
			
			System.out.println("Command >>> <Hi> <CurrentTime> <ConnectionTime> <ClientList> <Quit> <?>");
		
			Client_receive client_receiver = new Client_receive(client.client_socket);
			Client_send client_sender = new Client_send(client.client_socket, CID);
			client_receiver.start(); //Receiver Thread
			client_sender.start(); //Sender Thread
			
			client_receiver.join();
			client_sender.join();
			
			client.client_socket.close();
			System.out.println("Client Close");
		} catch (UnknownHostException e) {
			System.out.println("Client >>> 연결 관련 예외 발생, 클라이언트 종료");
		} catch (IOException e) {
			System.out.println("Client >>> 입출력 예외 발생");
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
