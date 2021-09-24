package chatproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

public class CSThread extends Thread{
	private Socket socket;
	private String nickname;
	private HashMap<String, Object> hash;
	
	BufferedReader br = null;
	
	public CSThread(Socket socket, HashMap<String, Object> hash) {
		this.socket = socket;
		this.hash = hash;
		
		//this.nickname = nickname;
		
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
			pw.println("닉네임을 입력하세요 : ");
			pw.flush();
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			
			this.nickname = br.readLine();
			
			//입장
			synchronized (hash) {
				hash.put(nickname, pw);
			}
			
			//입장 안내문
			Set<String> keys = hash.keySet();
			for(String key : keys) {
				//hash만들때 값 Object라서 pw 해줘야 된다.
				 PrintWriter pw2 = ((PrintWriter) hash.get(key));
				 pw2.println("< " + nickname + "님이 입장하였습니다. >");
				 pw2.flush();
			}
			
			
		} catch (UnsupportedEncodingException e) {
			
		} catch (IOException e) {
			
		}
	}

	@Override
	public void run() {
		try {
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
			
			while(true) {
				String data = br.readLine();
				if(data == null) {
					break;
				}else if("quit".equals(data)) {
					
					synchronized (hash) {
						hash.remove(nickname);
					}
					String msg = "< " + nickname + "님이 퇴장하셨습니다. >";
					
					Set<String> keys = hash.keySet();
					for(String key : keys) {
						//hash만들때 값 Object라서 pw 해줘야 된다.
						 PrintWriter pw2 = ((PrintWriter) hash.get(key));
						 pw2.println(msg);
						 pw2.flush();
						 
					}break;
				}
				
				//메세지 hash에 담아서 전체한테 보내주기 위한 hash
				Set<String> keys = hash.keySet();
				for(String key : keys) {
					//hash만들때 값 Object라서 pw 해줘야 된다.
					 PrintWriter pw2 = ((PrintWriter) hash.get(key));
					 pw2.println(data);
					 pw2.flush();
				}
				System.out.println(data);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("error : " + e);
		} finally {
			if(socket != null && socket.isClosed() == false) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
}
