package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {

	public static ArrayList<PrintWriter> m_OutputList;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		m_OutputList = new ArrayList<PrintWriter>();
		
			try {
				ServerSocket s_socket = new ServerSocket(80);
				
				while(true)
				{
					Socket c_socket = s_socket.accept();
					ClientManagerThread c_thread = new ClientManagerThread();
					c_thread.setSocket(c_socket);
					
					m_OutputList.add(new PrintWriter(c_socket.getOutputStream()));
					
					c_thread.start();
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
