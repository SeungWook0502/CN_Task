package Base64_Application;

import java.util.ArrayList;

public class Base64_Encoder {
	
	public String Base64_Encoding(String String_msg) {
		
		ArrayList<Integer> ASCII_Value_List = new ArrayList<Integer>();
		String binary_string = ""; //ASCII binary sum
		byte[] msg_byte= String_msg.getBytes(); //String convert to byte
		
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
			if(ASCII_Value.length()<8) { //ASCII value
				for(int j=0; ASCII_Value.length()<8;j++) { //부족한 자리 채우기
					ASCII_Value='0'+ASCII_Value;
				}
			}else if(ASCII_Value.length()>8) { //Non-ASCII value
				ASCII_Value=ASCII_Value.substring(24);
			}
			binary_string=binary_string+ASCII_Value; //sum binary values
		}
			
		///////
		
		for(int i=0; i<binary_string.length(); i=i+6) { //slice binary value to 6bit
			String Base64_Binary = "";
			null_count = 0;
			for(int j=0; j<6; j++) { //전체에서 6비트씩 받아서 Base64 value로 변경
				char binary_byte;
				try {
					binary_byte = binary_string.charAt(i+j); //binary form string에서 1개씩 받아 char형으로 convert
				}catch(StringIndexOutOfBoundsException e) { //bit가 부족한 경우
					binary_byte = '0';
					null_count++;
				}
				Base64_Binary += binary_byte;
			}
			char Base64_Value = Base64_Table[Integer.parseInt(Base64_Binary,2)]; //get Base64 value
			Base64_Value_List+=Base64_Value; //sum Base64 values
		}
		for(int i=0; i<null_count/2; i++) { //부족해서 채웠던 0값 '='로 표기
			Base64_Value_List+="=";
		}
		return Base64_Value_List;
	}
}
