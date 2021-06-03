package Loss_Simulator;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

public class Loss_Simulator_Object {

	public void loss_send(DataOutputStream dos, String send_msg, int Loss_Ratio) {
		Random random = new Random();
		if(random.nextInt(10)>=Loss_Ratio) { //Success send
			try {
				dos.writeUTF(send_msg);
			} catch (IOException e) {
				return;
			}
		}
	}
}
