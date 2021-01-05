package connection;

import java.io.Serializable;

public class message implements Serializable{
		
		private static final long serialVersionUID = 1L;
			byte[] data; 
			
			message(byte[] data){
				this.data = data;
			}
			
			/*
			 *getData method
			 *				returns the byte array.
			 */
			
			byte[] getData(){
				return data;
			}
			
		}	
