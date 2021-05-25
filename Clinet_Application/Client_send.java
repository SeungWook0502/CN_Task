package Clinet_Application;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Scanner;

import Base64_Application.Base64_Encoder;
import Loss_Simulator.Loss_Simulator_Object;

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
				System.out.print("Enter the command >>> ");
				Scanner scanner = new Scanner(System.in);
				String command = scanner.nextLine(); //Enter command
				
				/////////match command to request////////
				
				boolean Receive_Client = false;
				do {
					Client_main.Receive_ACK = false;
					Client_main.Receive_Response = false;
				
					if(command.equals("Hi")) { //Hi �޽����� �Է��� ���
						send_request_msg("Hi",dos);
					}else if(command.equals("CurrentTime")) { //CurrentTime �޽����� �Է��� ���
						send_request_msg("CurrentTime",dos);
					}else if(command.equals("ConnectionTime")) { //ConnectionTime �޽����� �Է��� ���
						send_request_msg("ConnectionTime",dos);
					}else if(command.equals("ClientList")) { //ClientList �޽����� �Է��� ���
						send_request_msg("ClientList",dos);	
					}else if(command.equals("Quit")) { //Quit �޽����� �Է��� ���
						send_request_msg("Quit",dos);
					}else if(command.equals("?")) { //? �޽����� �Է��� ���
						help_cmd();
					}else {
						send_request_msg(command,dos);
					}
					try {
						sleep(100);
					} catch (InterruptedException e) {
						System.out.println("Client >>> ���ͷ�Ʈ ���� �߻�");
					}
					
					LocalTime Send_Time = LocalTime.now(); //Request send time
					boolean Time_Receive_ACK = true;
						while(Time_Receive_ACK) {
							LocalTime Now_Time = LocalTime.now(); //Current time
							Duration Interval = Duration.between(Send_Time, Now_Time);
//							System.out.println(Interval); //Interval time check
							if(Interval.getNano() > 500000000) { //Timeout Interval 0.5 second
								Time_Receive_ACK = false;
							}
							if(Client_main.Receive_ACK) { //Receive ACK
								boolean Time_Receive_Response = true;
								Send_Time = LocalTime.now();
								while(Time_Receive_Response) {
									Now_Time = LocalTime.now();
									Interval = Duration.between(Send_Time, Now_Time);
//									System.out.println(Interval); //Interval time check
									if(Interval.getNano() > 500000000) { //Timeout Interval 0.5 second
										Time_Receive_ACK = false;
										Time_Receive_Response = false;
									}
									if(Client_main.Receive_Response) { //Receive Response
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
			System.out.println("Client >>> ��� ���� �߻�");
		}catch(InterruptedException e) {
			System.out.println("Client >>> ���ͷ�Ʈ ���� �߻�");
		}
	}
	public void send_request_msg(String command, DataOutputStream dos) {

		String Request_msg = "Request"+"///"
				+command+"///"
				+"CID:"+CID+"///"
				+"Num_Req:"+Num_Req+"///"
				+"END_MSG"
				; //Message form
//		System.out.println("Client's send message - "+Request_msg); //message form check
		
		Base64_Encoder base64_Encoder = new Base64_Encoder();
//		System.out.println("Base64 Request Message - "+base64_Encoder.Base64_Encoding(Request_msg)+"\n============================================================="); //Base64 Request message check
		
		Loss_Simulator_Object lso = new Loss_Simulator_Object();
		lso.loss_send(dos,base64_Encoder.Base64_Encoding(Request_msg)); //String to byte //Base64Encoding //Send Request message
	}
	
	public void help_cmd() { //���� �� ���� �ʿ��� ���� ���
		System.out.println("\t\t��ɾ� ���� & ��ɾ� ����");
		System.out.println("Hi  =>  �������� �ڽ��� ���̵� �����ش޶�� ��û");
		System.out.println("CurrentTime  =>  �������� ����ð��� ��û");
		System.out.println("ConnectionTime  =>  �޽����� ������ �ִ� �� Ŭ���̾�Ʈ���� TCP ���� ���� �ð��� ��û");
		System.out.println("ClientList  =>  ���� ������ ����� ��� Ŭ���̾�Ʈ�� IP�ּҿ� CID�� ��û");
		System.out.println("Quit  =>  �������� ������ �����ϱ⸦ ��û");
	}
}
