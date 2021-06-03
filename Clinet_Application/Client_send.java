package Clinet_Application;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Scanner;

import Base64_Application.Base64_Encoder;
import Loss_Simulator.Loss_Simulator_Object;
import Server_Application.Server_main;

public class Client_send extends Thread{
	Socket socket = null;
	String CID;
	int Num_Req = 0;
	public Client_send(Socket socket, String CID) {
		this.socket = socket;
		this.CID = CID;
	}
	
	public void run() {
		
		DataOutputStream dos;
		try {
			dos = new DataOutputStream(socket.getOutputStream());
			while(!Client_main.Quit) {
				String command = "Hi";
				sleep(100);
				/////////match command to request////////
				
				boolean Receive_Client = false;
				do {
					Client_main.Receive_ACK = false;
					Client_main.Receive_Response = false;
				
					if(command.equals("Hi")) { //Hi 메시지를 입력한 경우
						send_request_msg("Hi",dos);
					}else if(command.equals("CurrentTime")) { //CurrentTime 메시지를 입력한 경우
						send_request_msg("CurrentTime",dos);
					}else if(command.equals("ConnectionTime")) { //ConnectionTime 메시지를 입력한 경우
						send_request_msg("ConnectionTime",dos);
					}else if(command.equals("ClientList")) { //ClientList 메시지를 입력한 경우
						send_request_msg("ClientList",dos);	
					}else if(command.equals("Quit")) { //Quit 메시지를 입력한 경우
						send_request_msg("Quit",dos);
					}else {
						send_request_msg(command,dos);
					}
					try {
						sleep(100);
					} catch (InterruptedException e) {
						System.out.println("Client >>> 인터럽트 예외 발생");
					}
					
					LocalTime Send_Time = LocalTime.now(); //Request send time
					boolean Time_Receive_ACK = true;
						while(Time_Receive_ACK) {
							LocalTime Now_Time = LocalTime.now(); //Current time
							Duration Interval = Duration.between(Send_Time, Now_Time);
							if(Interval.getNano() > 500000000) { //Timeout Interval 0.5 second
								Time_Receive_ACK = false;
							}
							if(Client_main.Receive_ACK) { //Receive ACK
								Client_main.Count_ACK++;
								boolean Time_Receive_Response = true;
								Send_Time = LocalTime.now();
								while(Time_Receive_Response) {
									Now_Time = LocalTime.now();
									Interval = Duration.between(Send_Time, Now_Time);
									if(Interval.getNano() > 500000000) { //Timeout Interval 0.5 second
										Time_Receive_ACK = false;
										Time_Receive_Response = false;
									}
									if(Client_main.Receive_Response) { //Receive Response
										Client_main.Count_Response++;
										Receive_Client = true;
										Time_Receive_ACK = false;
										Time_Receive_Response = false;
										break;
									}
									sleep(2);
								}
							}
							sleep(2);
					}
				}while(!Receive_Client);
				
				Client_main.Receive_ACK = false;
				Client_main.Receive_Response = false;
				Num_Req++; //Num_Req up
				sleep(300);
			}
		}catch(IOException e) {
			System.out.println("Client >>> 출력 예외 발생");
		}catch(InterruptedException e) {
			System.out.println("Client >>> 인터럽트 예외 발생");
		}
	}
	public void send_request_msg(String command, DataOutputStream dos) {

		String Request_msg = "Request"+"///"
				+command+"///"
				+"CID:"+CID+"///"
				+"Num_Req:"+Num_Req+"///"
				+"END_MSG"
				; //Message form
		
		Base64_Encoder base64_Encoder = new Base64_Encoder();
		Loss_Simulator_Object lso = new Loss_Simulator_Object();
		lso.loss_send(dos,base64_Encoder.Base64_Encoding(Request_msg),Client_main.Loss_Ratio); //String to byte //Base64Encoding //Send Request message
		Client_main.Count_Total++;
	}
}
