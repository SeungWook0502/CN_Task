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
				}else {
					request_msg(command,dos);
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
			
			dos.writeUTF(Base64_Encoding(Request_msg)); //String to byte //Base64Encoding //Send Request message
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
