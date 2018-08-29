import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gson.Gson;

public class MultiChatServer {

	// 서버 소켓 및 클라이언트 연결 소켓
	private ServerSocket serverSocket = null;
	private Socket socket = null;

	// 연결된 클라이언트 스레드를 관리하는 ArrayList
	private ArrayList<ChatTread> chatThreads = new ArrayList<ChatTread>();
	// 로그이한 회원 정보를 저장하는 ArrayList
	private ArrayList<String> userThreads;

	// 로거 객체
	private Logger logger;

	// Login, Logout할 때만 정보를 전달해주도록 체크하기 위한 변수
	private boolean check = false;

	// 멀티 채팅 메인 프로그램 부분
	// 서버의 메인 실행 메서드이다
	// ServerSocket을 생성하고 클라이언트 연결 및 스레드 생성을 처리한다
	public void start() {
		logger = Logger.getLogger(this.getClass().getName());
		userThreads = new ArrayList<String>();
		try {
			// 서버 소켓 생성
			serverSocket = new ServerSocket(4005);
			logger.info("MultiChatServer start");

			// 무한 루프를 돌면서 클라이언트 연결을 기다린다
			while (true) {
				socket = serverSocket.accept();
				// 연결된 클라이언트에 대해 스레드 클래스 생성
				ChatTread chat = new ChatTread();
				// 클라이언트 리스트 추가
				chatThreads.add(chat);
				// 스레드 시작
				chat.start();
			}
		} catch (Exception e) {// 예외 처리
			logger.info("[MultiChatServer]start() Exception 발생!");
			e.printStackTrace();
		}

	}

	// ChatThread는 각 클라이언트와 연결을 담당하는 클래스로, 실질적인 네트워크 입출력이 발생하는 부분이다.
	// 각 클라이언트와 연결을 유지하며, 메세지 송수신을 담당하는 스레드 클래스이다
	public class ChatTread extends Thread {

		private String msg;// 수신 메세지 및 파싱 메세지 처리를 위한 변수 선언
		private Message m;// 메세지 객체 선언
		private Gson gson;// JSON 파서
		private boolean status;// logout과 같은 사용자의 상태를 알려주기 위한 변수
		private BufferedReader inMsg = null;// 입력 스트림
		private PrintWriter outMsg = null;// 출력 스트림

		// 서버가 수신한 메세지를 연결된 모든 클라이언트에 전송하는 메서드이다
		// 연결된 모든 클라이언트에 메세지 중계
		public void msgSendAll(String msg) {
			for (ChatTread ct : chatThreads) {
				ct.outMsg.println(msg);
				if (check) {
					ct.outMsg.println("client");
					ct.outMsg.println(gson.toJson(userThreads));
				}
			}

		}

		// 스레드의 핵심 메서드로, 생성된 각 스레드에서 따로 동작
		// 클라이언트에서 전달하는 JSON 메시지를 읽어와 Message 객체로 매핑한 후 Message 객체를 참조하여 메시지 유형에 따라 처리하는
		// 구조이다
		public void run() {
			try {
				// 변수 생성
				m = new Message();
				gson = new Gson();
				inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				outMsg = new PrintWriter(socket.getOutputStream(), true);
				status = true;
				// 먼저 while( ) 블록에서 메시지를 행 단위로 읽어 문자열 변수로 받아 온다
				// 먼저 상태 정보가 true이면 루플르 돌면서 사용자에게서 수신된 메세지 처리
				while (status) {
					// 수신된 메세지는 msg 변수에 저장
					msg = inMsg.readLine();
					// JSON 메세지를 Message 객체로 매핑
					m = gson.fromJson(msg, Message.class);
					System.out.println(m.getId());
					System.out.println(m.getType());
					// 파싱된 문자열 배열의 두 번째 요소값에 따라 처리
					// 로그아웃 메시지일 때
					if (m.getType().equals("logout")) {// 로그아웃 메세지 일 때
						check = true;// Logout일 떄 정보를 넘겨주기
						chatThreads.remove(this);
						userThreads.remove(m.getId());// 로그아웃 했을 때는 ArrayList에서 없앤다
						msgSendAll(gson.toJson(new Message(m.getId(), "", "님이 종료했습니다", "server")));
						// msgSendAll(gson.toJson(new Message(m.getId(), "", "logout", "userInfo")));
						// 해당 클라이언트 스레드 종료로 status를 false로 설정
						status = false;

					} else if (m.getType().equals("login")) {// 로그인 메세지일 때
						check = true;// Login일 떄 정보를 넘겨주기
						userThreads.add(m.getId());// 로그인 했을 떄는 ArrayList에 추가해준다
						for (int i = 0; i < userThreads.size(); i++) {// 컨트롤러에게 넘겨준다
							System.out.println(userThreads.get(i));
						}
						msgSendAll(gson.toJson(new Message(m.getId(), "", "님이 로그인 했습니다", "server")));
					} else {// 그 밖의 경우, 즉 일반 메세지일 때
						check = false;// 정보를 넘겨 주지 않음
						msgSendAll(msg);
					}
				}
				// 루프를 벗어나면 클라이언트 연결이 종료되므로 스레드 인터럽트
				this.interrupt();
				logger.info(this.getName() + " 종료됨!!");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MultiChatServer a = new MultiChatServer();
		a.start();
	}

}
