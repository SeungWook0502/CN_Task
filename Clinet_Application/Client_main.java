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
	public static int Count_ACK = 0; //ACK receive count
	public static int Count_Response = 0; //Response receive count
	public static int Count_Total = 0; //total receive count
	public static int Loss_Ratio; //loss percent
	
	public static void main(String[] args) {

		System.out.println("START");
		
		for(Loss_Ratio=1; Loss_Ratio<10; Loss_Ratio++) {
			Count_ACK = 0;
			Count_Response = 0;
			Count_Total = 0;
			Quit = false;
			
			String CID = (char)(Loss_Ratio+64)+Integer.toString(Loss_Ratio);
			Client_main client = new Client_main();
			
			try {
				client.client_socket = new Socket(/*"210.119.32.221"*/"localhost",55555);
				Client_receive client_receiver = new Client_receive(client.client_socket);
				Client_send client_sender = new Client_send(client.client_socket, CID);
				client_receiver.start(); //Receiver Thread
				client_sender.start(); //Sender Thread
				
				
				client.client_socket.getOutputStream().flush();
				client_receiver.join();
				client.client_socket.getOutputStream().flush();
				client.client_socket.close();
				
				String ACK_Loss_Ratio = String.format("%.0f",(100-((float)Count_ACK/(float)Count_Total)*100)); //ACK loss ratio
				String Response_Loss_Ratio = String.format("%.0f",(100-((float)Count_Response/(float)Count_Total)*100)); //ACK loss ratio
				
				System.out.println("============================");
				System.out.println("Setting Loss Ratio - "+Loss_Ratio+"0%");
				System.out.println("Total Receive message - "+Count_Total+"번");
				System.out.println("Retransmission - "+ (Count_Total-Count_Response) +"번");
				System.out.println("ACK Loss Ratio - "+ACK_Loss_Ratio+"%");
				System.out.println("Response Loss Ratio - "+Response_Loss_Ratio+"%");
				System.out.println("============================");

			} catch (UnknownHostException e) {
				System.out.println("Client >>> 연결 관련 예외 발생, 클라이언트 종료");
			} catch (IOException e) {
				System.out.println("Client >>> 입출력 예외 발생");
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("END");
	}

}
