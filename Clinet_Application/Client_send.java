package Clinet_Application;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
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
				}else {
					request_msg(command,dos);
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
			
			dos.writeUTF(Base64_Encoding(Request_msg)); //String to byte //Base64Encoding //Send Request message
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
	public String Base64_Encoding(String String_msg) {
		//String to ASCII variable
		ArrayList<Integer> ASCII_Value_List = new ArrayList<Integer>();
		String binary_string = ""; //ASCII binary sum
		byte[] msg_byte= String_msg.getBytes();
		//ASCII to Base64 variable
		char[] Base64_Table = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3','4','5','6','7','8','9','+','/'};
		String Base64_Value_List = "";
		int null_count = 0;
		
		for(int i=0; i<msg_byte.length; i++) { //get ASCII Value
			int ASCII_Value = msg_byte[i];
			ASCII_Value_List.add(ASCII_Value);
		}
		
		for(int i=0; i<ASCII_Value_List.size();i++) { //get ASCII table value
			String ASCII_Value = Integer.toBinaryString(ASCII_Value_List.get(i));
			for(int j=0; ASCII_Value.length()<8;j++) {
				ASCII_Value='0'+ASCII_Value;
			}
			binary_string=binary_string+ASCII_Value;
		}
			
		///////
		
		for(int i=0; i<binary_string.length(); i=i+6) {
			String Base64_Binary = "";
			null_count = 0;
			for(int j=0; j<6; j++) {
				char binary_byte;
				try {
				binary_byte = binary_string.charAt(i+j);
				}catch(StringIndexOutOfBoundsException e) {
					binary_byte = '0';
					null_count++;
				}
				
				Base64_Binary += binary_byte;
			}
			char Base64_Value = Base64_Table[Integer.parseInt(Base64_Binary,2)];
			Base64_Value_List+=Base64_Value;
		}
		for(int i=0; i<null_count/2; i++) {
			Base64_Value_List+="=";
		}
		
		return Base64_Value_List;
	}
}
