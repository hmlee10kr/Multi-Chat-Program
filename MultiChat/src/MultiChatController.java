import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gson.Gson;

//클라이언트 프그램의 핵심 부분으로 MultiChatController 클래스는 서버와 통신을 담당하고, 
//클라이언트에서 발생하는 이벤트에 따라 UI를 변경하거나 수신 된 메시지를 출력하는 등 프로그램의 전반적인 로직을 처리하는 부분이다
public class MultiChatController implements Runnable {

	private final MultiChatUI v;// 뷰 클래스 참조 객체
	private final MultiChatData chatData;// 데이터 클래스 참조 객체
	private Socket socket;
	private BufferedReader inMsg;
	private PrintWriter outMsg = null;
	private Message m;
	private boolean status;
	private Logger logger;
	private Thread thread;
	// private ControllerListener contL;
	private Gson gson;
	private String ip;
	private ArrayList<String> client;// 로그인한 회원 정보를 받아주는 ArrayList

	// 모델과 뷰 객체를 파라미터로 하는 생성자이다
	public MultiChatController(MultiChatData data, MultiChatUI ui) {
		// 모델과 뷰클래스 참조
		v = ui;
		chatData = data;

		// 로거 객체 초기화
		logger = Logger.getLogger(this.getClass().getName());
		ip = "127.0.0.1";
		gson = new Gson();
		client = new ArrayList<String>();// ArrayList 초기화
	}

	// 컨트롤러 클래스의 메인 로직 부분으로, UI에서 발생한 이벤트를 위임받아 처리한다
	public void appMain() {
		// 데이터 객체에서 데이터 변화를 처리할 UI 객체 추가
		chatData.addObj(v.msgOut);
		chatData.addUser(v.userOut);
		// 뷰 클래스에서 버튼들의 이벤트 핸들러를 등록하는 addButtonActionListener( ) 메서드를 호출하면서 리스너 클래스를 익명의
		// 내부 클래스로 만들어 전달한다
		v.addButtonActionListeners(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Object obj = e.getSource();

				if (obj == v.exitButton) {// 종료 버튼 처리
					if (outMsg != null)
						outMsg.println(gson.toJson(new Message(v.id, "", "", "logout")));
					System.exit(0);
				} else if (obj == v.loginButton) {// 로그인 상태 전환
					v.id = v.idInput.getText();
					v.outLabel.setText("대화명 : " + v.id);
					v.cardLayout.show(v.tab, "logout");
					ConnectServer();
				} else if (obj == v.logoutButton) {// 로그아웃 상태 전환
					outMsg.println(gson.toJson(new Message(v.id, "", "", "logout")));
					v.msgOut.setText("");
					v.cardLayout.show(v.tab, "login");
					chatData.refreshClientReset();// 로그아웃할때 로그인된 회원 정보를 아예 지워주기
					outMsg.close();
					try {
						inMsg.close();
						socket.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					status = false;
				} else if (obj == v.msgInput) {// 메세지 전송
					outMsg.println(gson.toJson(new Message(v.id, "", v.msgInput.getText(), "msg")));
					v.msgInput.setText("");
				}
			}
		});

	}

	// 로그인 버튼을 눌렀을 때 호출되는 메서드이다
	// 서버와 연결하고 입출력 스트림을 만든 후 메시지 수신에 필요한 스레드를 생성한다
	// 채팅 서버 접속을 위한 메서드이다
	public void ConnectServer() {
		try {
			// 소켓 생성
			socket = new Socket(ip, 4005);
			logger.info("[Client]Server 연결 성공");

			// 입력 스트림 생성
			inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// 출력 스트림 생성
			outMsg = new PrintWriter(socket.getOutputStream(), true);
			// 서버에 로그인 메세지 전달
			m = new Message(v.id, "", "", "login");
			outMsg.println(gson.toJson(m));

			// 메세지 수신을 위한 스레드 생성
			thread = new Thread(this);
			thread.start();

		} catch (Exception e) {// 예외 처리
			logger.warning("[MultiChatUI]connectServer() Exception 발생!");
			e.printStackTrace();
		}
	}

	// UI 실행과 독립적으로 서버와 네트워크 연결을 유지하며, 수신된 메시지를 처리하는 역할을 담당한다
	// 서버 연결 후 메세지 수신을 UI 동작과 상관없이 독립적으로 처리하는 스레드를 실행한다
	public void run() {
		// 수신 메세지를 처리하는데 필요한 변수 선언
		String msg;
		status = true;

		// while( )문을 반복하면서 서버에서 전송하는 메시지를 행 단위로 읽어와 JSON 메시지를 Message 객체로 변환한다
		while (status) {
			try {
				// 메세지 수신 및 파싱
				msg = inMsg.readLine();
				if (msg.equals("client")) {
					client = gson.fromJson(inMsg.readLine(), ArrayList.class);// ArrayList형식으로 String 받아오기
					chatData.refreshClient(client);// 정보 업데이트
				} else {
					m = gson.fromJson(msg, Message.class);
					// MultiChatData 객체로 데이터 갱신

					// 로그인된 회원 정보 업데이트
					chatData.refreshData(m.getId() + ">" + m.getMsg() + "\n");

					// 커서를 현재 대화 메세지에 표시
					v.msgOut.setCaretPosition(v.msgOut.getDocument().getLength());
					v.userOut.setCaretPosition(v.msgOut.getDocument().getLength());
				}
			} catch (Exception e) {// 예외처리

			}
		}
	}

	// 실행 메인 메서드
	// MultiChatController 클래스 생성자의 파라미터로 뷰와 모델 클래스를 전달하고, appMain( ) 메서드를 실행한다
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		MultiChatController app = new MultiChatController(new MultiChatData(), new MultiChatUI());
		app.appMain();
	}
}
