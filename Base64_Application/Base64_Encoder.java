package Base64_Application;

import java.util.ArrayList;

public class Base64_Encoder {
	
	public String Base64_Encoding(String String_msg) {
		
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
			if(ASCII_Value.length()<8) {
				for(int j=0; ASCII_Value.length()<8;j++) {
					ASCII_Value='0'+ASCII_Value;
				}
			}else if(ASCII_Value.length()>8) {
				ASCII_Value=ASCII_Value.substring(24);
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
