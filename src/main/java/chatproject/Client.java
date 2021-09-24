package chatproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	// 1. 소켓만들기
	private static final String SERVER_IP = "127.0.0.1";
	private static final int SERVER_PORT = 7000;

	public static void main(String[] args) {

		String nickname = "";
		Socket socket = new Socket();
		Scanner scan = new Scanner(System.in);

		try {
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));

			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));

			String str = br.readLine();
			System.out.println(str);

			Scanner sc = new Scanner(System.in);

			nickname = sc.nextLine();
			// 여기 소켓이 서버랑 연결된 거니까 여기서 pw하면 서버로 간다. => 그래서 서버에서 br로 받을수있다.
			pw.println(nickname);
			pw.flush();

			// 상대방한테 채팅 받아오는 스레드
			new ClientThread(socket, nickname).start();

			// 내가 상대방한테 보내는 반복문
			while (true) {
				String line = scan.nextLine();

				if ("quit".equals(line)) {
					// line = "< " + nickname + "님이 퇴장하였습니다. >";
					pw.println("quit");
					pw.flush();
					break;
				}
				// 서버로 보낸 채팅
				pw.println(nickname + ":" + line);
				// 플러시를 해야 버퍼로 들어온 pw가 다시 나감
				pw.flush();
			}

		} catch (IOException e) {
			System.out.println("error: " + e);
			System.out.println("client");
		} finally {
			try {
				System.out.println(socket.isClosed());
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();

			}
		}

	}
}
