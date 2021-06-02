package Server_Application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import Base64_Application.Base64_Decoder;
import Base64_Application.Base64_Encoder;
import Loss_Simulator.Loss_Simulator_Object;

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
			while(true) {
				String Request_msg="";
				try {
					Request_msg = dis.readUTF(); //Data read
				}catch (EOFException eof) { //Quit Client
					client_remove();
					break;
				}
//				System.out.println("=============================================================");
//				System.out.println("Base64 Request message - "+Request_msg); //Base64 Request message check
				Base64_Encoder base64_Encoder = new Base64_Encoder();
				Base64_Decoder base64_Decoder = new Base64_Decoder();
				Request_msg = base64_Decoder.Base64_Decoding(Request_msg);
				
				String[] slice_msg = Request_msg.split("///");
//				System.out.println("Server's receive message - "+Request_msg); //message form check
				if(slice_msg[0].equals("Request") && slice_msg.length==5) { //message type & message form check
					
					Loss_Simulator_Object lso = new Loss_Simulator_Object();
					Server_ACK(dos,base64_Encoder, lso, slice_msg[3].split("Num_Req")[1]); //send ACK
					
					LocalTime now_Server_Time = LocalTime.now(); //get server's real time
					
					if(slice_msg[1].equals("Hi")) { //command "Hi"
						
						send_response_msg(dos, base64_Encoder, lso, Server_Hi(slice_msg[2].split(":")[1]));
					}else if(slice_msg[1].equals("CurrentTime")) { //command "CurrentTime"
						
						send_response_msg(dos, base64_Encoder, lso, Server_CurrentTime(now_Server_Time));
					}else if(slice_msg[1].equals("ConnectionTime")) { //command "ConnectionTime"
						
						send_response_msg(dos, base64_Encoder, lso, Server_ConnectionTime(now_Server_Time));
					}else if(slice_msg[1].equals("ClientList")) { //command "ClientList"
						
						send_response_msg(dos, base64_Encoder, lso, Server_ClientList());
					}else if(slice_msg[1].equals("Quit")) { //command "Quit"
						send_response_msg(dos, base64_Encoder, lso, Server_Quit());
					}else {
						send_response_msg(dos, base64_Encoder, lso, Server_Error());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Client Quit >>> "+socket.getInetAddress());
		}
	}
	
	public void send_response_msg(DataOutputStream dos, Base64_Encoder base64_Encoder,Loss_Simulator_Object lso, String[] value_sttNum_array) {

		String Response_msg = "Response"+"///"
				+value_sttNum_array[0]+"///"
				+value_sttNum_array[1]+"///"
				+"END_MSG"; //message form
		
		lso.loss_send(dos,base64_Encoder.Base64_Encoding(Response_msg)); //String to byte //Base64Encoding //Send Request message
	}
	
	public void Server_ACK(DataOutputStream dos, Base64_Encoder base64_Encoder, Loss_Simulator_Object lso, String Num_Req) { //ACK
		
		String ACK_msg = "ACK"+"///"
				+"Num_ACK"+Num_Req+"///"
				+"END_MSG";
		
//		System.out.println("Server's ACK Message - "+ACK_msg); //Server ACK message check
		lso.loss_send(dos, base64_Encoder.Base64_Encoding(ACK_msg));
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
		return Response_msg;
	}
	
	public void client_remove() { //해당 클라이언트 삭제
		
		for (int i=0;i<clients.size();i++) {
			System.out.println("this.client_id "+this.client_id);
			System.out.println("clients.get(i).client_id "+clients.get(i).client_id);
			if(this.client_id < clients.get(i).client_id) { //해당 client_id보다 높은 값들 찾기
				clients.get(i).client_id_set(); //높은경우 pop이후에 index조정이 필요하므로 값 변경
			}
		}
		clients.remove(this.client_id);
		System.out.println("clients.size() "+clients.size());
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
