package main;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Main {

	public static void main(String args[]) {
		try {
            Socket sender = new Socket("localhost", 8081);
            if ( sender != null && sender.isConnected() ) {
            	DataOutputStream dos = 
                        new DataOutputStream( 
                            new BufferedOutputStream( sender.getOutputStream() ));
                 
                String msg = "Hello!";
//                Message msg = new Message();
//                msg.active = true;
//                msg.userid = messages;
//                msg.username = "User_" + messages;
//                msg.data = this.toString();
//                msg.type = Message.MESSAGE_TYPE_USER;
                     
                dos.writeUTF(msg);
                dos.flush();
//                oos.writeObject(msg);
//                oos.flush();
            }
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
	}
}
