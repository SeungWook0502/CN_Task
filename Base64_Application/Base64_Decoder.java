package Base64_Application;

public class Base64_Decoder {
	
	public String Base64_Decoding(String String_msg) {

		char[] c_msg = String_msg.toCharArray(); //convert String to char array
		char[] Base64_Table = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3','4','5','6','7','8','9','+','/'};
		String Base64_StringTable = new String(Base64_Table); //char array convert to String
		String Base64_value_List="";
		
		for(int i=0;i<c_msg.length;i++) { //Base64 value convert to binary form String
			String binary_String="";
			if(c_msg[i]=='=') { //Base64 '=' 변환
				binary_String="00";
			}
			else{ //'=' 아닌경우
				binary_String = Integer.toBinaryString(Base64_StringTable.indexOf(c_msg[i]));
				for(int j=0;binary_String.length()<6;j++) { //Base64의 binary에서 부족한 자리 0으로 채우기
					binary_String="0" + binary_String;
				}
			}
			Base64_value_List += binary_String; //sum binary form String
		}
		byte[] ASCII_value= new byte[Base64_value_List.length()];
		for(int i=0;i<Base64_value_List.length();i=i+8) { //convert String form binary to String message
			String Base64_Value="";
			try {
				for(int j=0; j<8;j++) { //get 8bit
					Base64_Value+=Base64_value_List.charAt(i+j);
				}
				for(int j=0; Base64_Value.length()<8; j++) { //ASCII의 binary에서 부족한 자리 0으로 채우기
					Base64_Value="0"+Base64_Value;
				}
				
				int ASCII_int = 0;
				for(int j=0; j < Base64_Value.length(); j++) { //binary form string value convert to integer
					
					if(Base64_Value.charAt(j) == '1') { //binary value가 1인 자리수를 integer형태로 변환
						ASCII_int += (int)Math.pow(2, 7-j)*1;
					}
				}
				if(ASCII_int > 127) { //Non-ASCII value
						ASCII_value[i/8]+=ASCII_int-256;
				}else { //ASCII value
						ASCII_value[(int)(i/8)]+=ASCII_int;
				}
			}catch(StringIndexOutOfBoundsException e) {}
		}
		String_msg = new String(ASCII_value); //convert byte array to String
		return String_msg;
	}
}
