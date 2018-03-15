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


// Ű����� ���۹��ڿ� �Է¹޾� ������ �����ϴ� ������
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
		//Ű����κ��� �о���� ���� ��Ʈ����ü ����
		BufferedReader br=
		new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw=null;
		try{
			//������ ���ڿ� �����ϱ� ���� ��Ʈ����ü ����
			pw=new PrintWriter(socket.getOutputStream(),true);
			//ù��° �����ʹ� id �̴�. ���濡�� id�� �Բ� �� IP�� �����Ѵ�.
			if(cf.isFirst==true){
				InetAddress iaddr=socket.getLocalAddress();				
				ip = iaddr.getHostAddress();	
				id = getId();
				n_name2 = getName();
				System.out.println("ip:"+ip+"\t"+"id:"+id);
				str = "["+n_name2+"] �� �α��� ("+ip+")"; 
			}else{
				str= "["+n_name2+"] "+cf.txtF.getText();
			}
			//�Է¹��� ���ڿ� ������ ������
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
				System.out.println("while�� ����!!!!!");
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
	            	System.out.println("else ����~~~!!");
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
        
        System.out.println("insertDB����~~~");
        
        try {
        	
        	Connection conn = DriverManager.getConnection("jdbc:mysql://211.231.160.88","root", "root");

    		sql.append(" INSERT INTO test.test(id,ip,name) ");
            sql.append(" VALUES(?, ?, ?)");
            pstmt = conn.prepareStatement(sql.toString());

        	pstmt.setNString(1, id);
        	pstmt.setNString(2, ip);
        	pstmt.setNString(3, id);
        	
            int cnt = pstmt.executeUpdate();
            System.out.println("���ڵ� " + cnt + "���� �߰� �Ǿ����ϴ�.");
            //n_name2 = id;
            
            pstmt.close();
        	conn.close();

		} catch (Exception e) {
			// TODO: handle exception
		}
        
        return id;
	}
}
//������ ������ ���ڿ��� ���۹޴� ������
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
			//�����κ��� ���۵� ���ڿ� �о���� ���� ��Ʈ����ü ����
			br=new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			while(true){
				//�������κ��� ���ڿ� �о��
				String str=br.readLine();
				if(str==null){
					System.out.println("������ ������");
					break;
				}
				//���۹��� ���ڿ� ȭ�鿡 ���
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
			System.out.println("���Ἲ��!");
			cf = new ClientFrame(socket);
			new ReadThread(socket, cf).start();
		}catch(IOException ie){
			System.out.println(ie.getMessage());
		}
	}
}












