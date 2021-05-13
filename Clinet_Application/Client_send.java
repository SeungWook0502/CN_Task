package Clinet_Application;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Base64;
import java.util.Scanner;

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
			boolean Quit = false;
			while(!Quit) {
				System.out.print("Enter the command >>> ");
				Scanner scanner = new Scanner(System.in);
				String command = scanner.nextLine(); //Enter command
				
				/////////match command to request////////
				
				if(command.equals("Hi")) { //Hi 메시지를 입력한 경우
					request_msg("Hi",dos);
				}else if(command.equals("CurrentTime")) { //CurrentTime 메시지를 입력한 경우
					request_msg("CurrentTime",dos);
				}else if(command.equals("ConnectionTime")) { //ConnectionTime 메시지를 입력한 경우
					request_msg("ConnectionTime",dos);
				}else if(command.equals("ClientList")) { //ClientList 메시지를 입력한 경우
					request_msg("ClientList",dos);	
				}else if(command.equals("?")) { //? 메시지를 입력한 경우
					help_cmd();
				}else if(command.equals("Quit")) { //Quit 메시지를 입력한 경우
					request_msg("Quit",dos);
					Quit = true; //Thread while terminate
				}
				Num_Req++; //Num_Req up
				sleep(300); //Receive와 겹치는 현상을 막기위해 Receive이후에 22Line이 나올 수 있도록
			}
		}catch(IOException e) {
			System.out.println("Client >>> 출력 예외 발생");
		}catch(InterruptedException e) {
			System.out.println("Client >>> 인터럽트 예외 발생");
		}
	}
	public void request_msg(String command, DataOutputStream dos) {
		try {
			String Request_msg = "Request"+"///"
					+command+"///"
					+"CID:"+CID+"///"
					+"Num_Req:"+Num_Req+"///"
					+"END_MSG"
					; //Message form
			System.out.println("Client's send message - "+Request_msg); //message form check
			
			dos.writeUTF(Base64.getEncoder().encodeToString(Request_msg.getBytes())); //String to byte //Base64Encoding //Send Request message
			System.out.println("Base64 Request Message - "+Base64.getEncoder().encodeToString(Request_msg.getBytes())+"\n============================================================="); //Base64 Request message check
		}catch(IOException e) {
			System.out.println("Client >>> 출력 예외 발생");
		}
	}
	public void help_cmd() { //종류 및 설명에 필요한 설명서 출력
		System.out.println("\t\t명령어 종류 & 명령어 설명");
		System.out.println("Hi  =>  서버에게 자신의 아이디를 저장해달라는 요청");
		System.out.println("CurrentTime  =>  서버에게 현재시간을 요청");
		System.out.println("ConnectionTime  =>  메시지를 보내고 있는 이 클라이언트와의 TCP 연결 유지 시간을 요청");
		System.out.println("ClientList  =>  현재 서버에 연결된 모든 클라이언트의 IP주소와 CID를 요청");
		System.out.println("Quit  =>  서버와의 연결을 종료하기를 요청");
	}
}
