import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;


// 키보드로 전송문자열 입력받아 서버로 전송하는 스레드
class WriteThread{
	Socket socket;
	ClientFrame cf;
	String str;
	String id;
	String ip;
	String n_name;
	String n_name2;
	String n_name3;
	
	public WriteThread(ClientFrame cf) {
		this.cf  = cf;
		this.socket= cf.socket;
	}
	public void sendMsg() {
		//키보드로부터 읽어오기 위한 스트림객체 생성
		BufferedReader br=
		new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw=null;
		try{
			//서버로 문자열 전송하기 위한 스트림객체 생성
			pw=new PrintWriter(socket.getOutputStream(),true);
			//첫번째 데이터는 id 이다. 상대방에게 id와 함께 내 IP를 전송한다.
			if(cf.isFirst==true){
				InetAddress iaddr=socket.getLocalAddress();				
				ip = iaddr.getHostAddress();	
				id = getId();
				n_name2 = getName();
				System.out.println("ip:"+ip+"\t"+"id:"+id);
				str = "["+n_name2+"] 님 로그인 ("+ip+")"; 
			}else{
				str= "["+n_name2+"] "+cf.txtF.getText();
			}
			//입력받은 문자열 서버로 보내기
			pw.println(str);
		
		}catch(IOException ie){
			System.out.println(ie.getMessage());
		}finally{
			try{
				if(br!=null) br.close();
				//if(pw!=null) pw.close();
				//if(socket!=null) socket.close();
			}catch(IOException ie){
				System.out.println(ie.getMessage());
			}
		}
	}	
	
	public String getName() {

		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://211.231.160.88","root", "root");
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("Select * From test.test");
			
			while (rs.next()) {			
				System.out.println("while문 진입!!!!!");
				n_name = rs.getString("name");
				String id2 = rs.getString("id");
				
	            if(id.equals(id2)){
	            	System.out.println("~~~id : "+ id + "id2 : "+id2);
	            	n_name3 = n_name;
	            	st.close();
	            	conn.close();
	            	break;
	            } 
	            else {
	            	System.out.println("else 진입~~~!!");
	            	n_name3 = insertDB();
	                st.close();
	            	conn.close();
	            	break;
	            }
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return n_name3;
	}
	
	public String getId(){		
		
		return id = Id.getId();
		//return id;
		
	}
	
	public String insertDB() {
		
        StringBuffer sql = new StringBuffer();
        PreparedStatement  pstmt=null;
        
        System.out.println("insertDB실행~~~");
        
        try {
        	
        	Connection conn = DriverManager.getConnection("jdbc:mysql://211.231.160.88","root", "root");

    		sql.append(" INSERT INTO test.test(id,ip,name) ");
            sql.append(" VALUES(?, ?, ?)");
            pstmt = conn.prepareStatement(sql.toString());

        	pstmt.setNString(1, id);
        	pstmt.setNString(2, ip);
        	pstmt.setNString(3, id);
        	
            int cnt = pstmt.executeUpdate();
            System.out.println("레코드 " + cnt + "개가 추가 되었습니다.");
            //n_name2 = id;
            
            pstmt.close();
        	conn.close();

		} catch (Exception e) {
			// TODO: handle exception
		}
        
        return id;
	}
}
//서버가 보내온 문자열을 전송받는 스레드
class ReadThread extends Thread{
	Socket socket;
	ClientFrame cf;
	public ReadThread(Socket socket, ClientFrame cf) {
		this.cf = cf;
		this.socket=socket;
	}
	public void run() {
		BufferedReader br=null;
		try{
			//서버로부터 전송된 문자열 읽어오기 위한 스트림객체 생성
			br=new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			while(true){
				//소켓으로부터 문자열 읽어옴
				String str=br.readLine();
				if(str==null){
					System.out.println("접속이 끊겼음");
					break;
				}
				//전송받은 문자열 화면에 출력
				//System.out.println("[server] " + str);
				cf.txtA.append(str+"\n");
			}
		}catch(IOException ie){
			System.out.println(ie.getMessage());
		}finally{
			try{
				if(br!=null) br.close();
				if(socket!=null) socket.close();
			}catch(IOException ie){}
		}
	}
}
public class MultiChatClient {
	public static void main(String[] args) {
		Socket socket=null;
		ClientFrame cf;
		try{
			socket=new Socket("211.231.160.88", 80);
			System.out.println("연결성공!");
			cf = new ClientFrame(socket);
			new ReadThread(socket, cf).start();
		}catch(IOException ie){
			System.out.println(ie.getMessage());
		}
	}
}












