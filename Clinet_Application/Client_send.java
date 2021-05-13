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
				
				if(command.equals("Hi")) { //Hi �޽����� �Է��� ���
					request_msg("Hi",dos);
				}else if(command.equals("CurrentTime")) { //CurrentTime �޽����� �Է��� ���
					request_msg("CurrentTime",dos);
				}else if(command.equals("ConnectionTime")) { //ConnectionTime �޽����� �Է��� ���
					request_msg("ConnectionTime",dos);
				}else if(command.equals("ClientList")) { //ClientList �޽����� �Է��� ���
					request_msg("ClientList",dos);	
				}else if(command.equals("?")) { //? �޽����� �Է��� ���
					help_cmd();
				}else if(command.equals("Quit")) { //Quit �޽����� �Է��� ���
					request_msg("Quit",dos);
					Quit = true; //Thread while terminate
				}
				Num_Req++; //Num_Req up
				sleep(300); //Receive�� ��ġ�� ������ �������� Receive���Ŀ� 22Line�� ���� �� �ֵ���
			}
		}catch(IOException e) {
			System.out.println("Client >>> ��� ���� �߻�");
		}catch(InterruptedException e) {
			System.out.println("Client >>> ���ͷ�Ʈ ���� �߻�");
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
			System.out.println("Client >>> ��� ���� �߻�");
		}
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
