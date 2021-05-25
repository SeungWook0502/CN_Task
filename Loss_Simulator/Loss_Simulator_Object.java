package Loss_Simulator;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

public class Loss_Simulator_Object {

	public void loss_send(DataOutputStream dos, String send_msg) {
		Random random = new Random();
		if(random.nextInt(100)+1<71) { //70% Success send
			try {
				dos.writeUTF(send_msg);
				System.out.println("Success - "+send_msg); //message form check
			} catch (IOException e) {
				System.out.println("Error >>> 출력 예외 발생");
			}
		}else { //30% loss
			System.out.println("Fail - "+send_msg);
		}
	}
}
