package Server_Application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import Base64_Application.*;

public class Client_struct extends Thread{
	Socket socket;	//Socket Object
	LocalTime cnt_time;	//connected time
	String CID;		//CID
	ArrayList<Client_struct> clients;
	int client_id;
	
	
	public Client_struct(Socket socket,ArrayList<Client_struct> clients, LocalTime cnt_time, int client_id) {
		this.client_id = client_id;
		this.socket = socket;
		this.clients = clients;
		this.cnt_time = cnt_time;
	}
	
	public void run() {
		
		try {
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			boolean Quit = false;
			while(!Quit) {
				String Request_msg = dis.readUTF(); //Data read
				String[] value_sttNum_array= {"",""};
				System.out.println("=============================================================");
				System.out.println("Base64 Request message - "+Request_msg); //Base64 Request message check
				Base64_Decoder base64_Decoder = new Base64_Decoder();
				Request_msg = base64_Decoder.Base64_Decoding(Request_msg);
				String[] slice_msg = Request_msg.split("///");
				System.out.println("Server's receive message - "+Request_msg); //message form check
				if(slice_msg[0].equals("Request") && slice_msg.length==5) { //message type & message form check
					
					LocalTime now_Server_Time = LocalTime.now(); //get server's real time
					
					if(slice_msg[1].equals("Hi")) { //command "Hi"
//						System.out.println(slice_msg);
						value_sttNum_array = Server_Hi(slice_msg[2].split(":")[1]);
						
					}else if(slice_msg[1].equals("CurrentTime")) { //command "CurrentTime"
						
						value_sttNum_array = Server_CurrentTime(now_Server_Time);
						
					}else if(slice_msg[1].equals("ConnectionTime")) { //command "ConnectionTime"
						
						value_sttNum_array = Server_ConnectionTime(now_Server_Time);
						
					}else if(slice_msg[1].equals("ClientList")) { //command "ClientList"
						
						value_sttNum_array = Server_ClientList();
						
					}else if(slice_msg[1].equals("Quit")) { //command "Quit"
						
						value_sttNum_array = Server_Quit();
						Quit=true;
					}else {
						value_sttNum_array = Server_Error();
					}
					
					String Response_msg = "Response"+"///"
							+value_sttNum_array[0]+"///"
							+value_sttNum_array[1]+"///"
							+"END_MSG"; //message form
					System.out.println("Server's send message - "+Response_msg); //message form check
					Base64_Encoder base64_Encoder = new Base64_Encoder();
					System.out.println("Base64 Response message - "+base64_Encoder.Base64_Encoding(Response_msg)); //Base64 Response message check
					dos.writeUTF(base64_Encoder.Base64_Encoding(Response_msg)); //String to byte //Base64Encoding //Send Request message
				}
			}
		} catch (IOException e) {
			System.out.println("Server >>> 입출력 예외 발생");
		}
	}
	
	public String[] Server_Hi(String CID) { //Hi
		
		String[] Response_msg = {"",""};
		this.CID = CID;
		Response_msg[0] = "100";
		Response_msg[1] = "Hi "+CID;
		return Response_msg;
	}
	
	public String[] Server_CurrentTime(LocalTime now_Server_Time) { //CurrentTime
		
		String[] Response_msg = {"",""};
		Response_msg[0] = "130";
		Response_msg[1] = ("서버 시간은 "+now_Server_Time.getHour()+"시 "+now_Server_Time.getMinute()+"분 "+now_Server_Time.getSecond()+"초 입니다.");
		return Response_msg;
	}
	
	public String[] Server_ConnectionTime(LocalTime now_Server_Time) { //ConnectionTime
		String[] Response_msg = {"",""};
		Response_msg[0] = "150";
		Duration duration = Duration.between(cnt_time, now_Server_Time); //Server real time - Client connected time
		int current_hour = (int) duration.getSeconds()/3600; //hour
		int current_minute = (int) duration.getSeconds()%3600/60; //minute
		int current_second = (int) duration.getSeconds()%3600%60; //second
		Response_msg[1] = current_hour+"시간 "+current_minute+"분 "+current_second+"초 동안 연결중입니다.";
		return Response_msg;
	}
	
	public String[] Server_ClientList() { //ClientList
		String[] Response_msg = {"",""};
		Response_msg[0] = "200";
		for (int i=0;i<clients.size();i++) {
			String clients_CID = clients.get(i).CID; //CID
			String clients_IP = clients.get(i).socket.getLocalAddress().toString(); //IP
			Response_msg[1] = Response_msg[1]+clients_CID + clients_IP+"\n"; //CID+IP
		}
		return Response_msg;
	}
	
	public String[] Server_Quit() { //Quit
		String[] Response_msg = {"",""};
		Response_msg[0] = "250";
		Response_msg[1] = "Bye "+CID;
		client_remove();
		return Response_msg;
	}
	
	public void client_remove() { //해당 클라이언트 삭제
		
		for (int i=0;i<clients.size();i++) {
		
			int clients_id = clients.get(i).client_id;
			if(clients_id > client_id) { //해당 client_id보다 높은 값들 찾기
				clients.get(i).client_id_set(); //높은경우 pop이후에 index조정이 필요하므로 값 변경
			}
		}
		clients.remove(client_id);
	}
	
	public void client_id_set() { //remove하게될 경우 해당 client_id보다 높은 값들 -1
		this.client_id = --client_id;
	}
	
	public String[] Server_Error() {
		String[] Response_msg = {"",""};
		Response_msg[0] = "300";
		Response_msg[1] = "Command Error";
		return Response_msg;
	}
}
