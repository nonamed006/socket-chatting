package chatproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class ChatServer {
	//1. 서버소켓만들기
	//2. bind 하기
	//3. accept해서 대기상태 만들기
	//4. 쓰레드 여러개 써야되는 이유 : 
	
	static HashMap<String, Object> hash;
	
	public static void main(String[] args) {
		ServerSocket serversocket = null;
		
		try {
			serversocket = new ServerSocket();
			
			serversocket.bind(new InetSocketAddress("0.0.0.0", 7000));
			
			//대기하다가 소켓 생기면 스레드로 넘기고 다시 대기
			
			hash = new HashMap<String, Object>();
			while(true) {
				Socket socket = serversocket.accept();
				
				System.out.println("접속");
				new CSThread(socket, hash).start();
			}
			
			
//			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
//			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
//			
			
			
			//System.out.println("test");
		} catch (IOException e) {
			e.printStackTrace();
			
		}finally {
			if(serversocket != null && serversocket.isClosed() == false) {
				try {
					serversocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}

