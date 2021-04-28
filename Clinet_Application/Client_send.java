package Clinet_Application;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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
				
				dos.writeUTF("Request"+"///"
						+command+"///"
						+"CID:"+CID+"///"
						+"Num_Req:"+Num_Req+"///"
						+"END_MSG"
						); //Request message
				
				System.out.println("Client's send message - "+"Request"+"///"
						+command+"///"
						+"CID:"+CID+"///"
						+"Num_Req:"+Num_Req+"///"
						+"END_MSG"
						); //message form check

				Num_Req++; //Num_Req up
				
				if(command.equals("Quit")) { //Quit메시지를 보낸경우
					Quit = true; //Thread while terminate
				}
				sleep(300); //Receive와 겹치는 현상을 막기위해 Receive이후에 22Line이 나올 수 있도록
			}
		}catch(IOException e) {
			System.out.println("Client >>> 출력 예외 발생");
		}catch(InterruptedException e) {
			System.out.println("Client >>> 인터럽트 예외 발생");
		}
	}
}
