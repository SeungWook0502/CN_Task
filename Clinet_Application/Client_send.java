package Clinet_Application;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client_send extends Thread{
	Socket socket = null;
	String CID;
	int Num_Req = 1;
	public Client_send(Socket socket, String CID) {
		this.socket = socket;
		this.CID = CID;
	}
	
	public void run() {
		
		DataOutputStream dos;
		try {
			dos = new DataOutputStream(socket.getOutputStream());
			while(true) {
				System.out.print("Enter the command >>>");
				Scanner scanner = new Scanner(System.in);
				String command = scanner.nextLine();
				
				dos.writeUTF("Request"+"///"
						+command+"///"
						+"CID:"+CID+"///"
						+"Num_Req:"+Num_Req+++"///"
						+"END_MSG"
						); //Request message
				sleep(300); //Receive�� ��ġ�� ������ �������� Receive���Ŀ� 22Line�� ���� �� �ֵ���
			}
		}catch(Exception e) {
			
		}
	}
}
