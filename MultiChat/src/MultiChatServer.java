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

	// ���� ���� �� Ŭ���̾�Ʈ ���� ����
	private ServerSocket serverSocket = null;
	private Socket socket = null;

	// ����� Ŭ���̾�Ʈ �����带 �����ϴ� ArrayList
	private ArrayList<ChatTread> chatThreads = new ArrayList<ChatTread>();
	// �α����� ȸ�� ������ �����ϴ� ArrayList
	private ArrayList<String> userThreads;

	// �ΰ� ��ü
	private Logger logger;

	// Login, Logout�� ���� ������ �������ֵ��� üũ�ϱ� ���� ����
	private boolean check = false;

	// ��Ƽ ä�� ���� ���α׷� �κ�
	// ������ ���� ���� �޼����̴�
	// ServerSocket�� �����ϰ� Ŭ���̾�Ʈ ���� �� ������ ������ ó���Ѵ�
	public void start() {
		logger = Logger.getLogger(this.getClass().getName());
		userThreads = new ArrayList<String>();
		try {
			// ���� ���� ����
			serverSocket = new ServerSocket(4005);
			logger.info("MultiChatServer start");

			// ���� ������ ���鼭 Ŭ���̾�Ʈ ������ ��ٸ���
			while (true) {
				socket = serverSocket.accept();
				// ����� Ŭ���̾�Ʈ�� ���� ������ Ŭ���� ����
				ChatTread chat = new ChatTread();
				// Ŭ���̾�Ʈ ����Ʈ �߰�
				chatThreads.add(chat);
				// ������ ����
				chat.start();
			}
		} catch (Exception e) {// ���� ó��
			logger.info("[MultiChatServer]start() Exception �߻�!");
			e.printStackTrace();
		}

	}

	// ChatThread�� �� Ŭ���̾�Ʈ�� ������ ����ϴ� Ŭ������, �������� ��Ʈ��ũ ������� �߻��ϴ� �κ��̴�.
	// �� Ŭ���̾�Ʈ�� ������ �����ϸ�, �޼��� �ۼ����� ����ϴ� ������ Ŭ�����̴�
	public class ChatTread extends Thread {

		private String msg;// ���� �޼��� �� �Ľ� �޼��� ó���� ���� ���� ����
		private Message m;// �޼��� ��ü ����
		private Gson gson;// JSON �ļ�
		private boolean status;// logout�� ���� ������� ���¸� �˷��ֱ� ���� ����
		private BufferedReader inMsg = null;// �Է� ��Ʈ��
		private PrintWriter outMsg = null;// ��� ��Ʈ��

		// ������ ������ �޼����� ����� ��� Ŭ���̾�Ʈ�� �����ϴ� �޼����̴�
		// ����� ��� Ŭ���̾�Ʈ�� �޼��� �߰�
		public void msgSendAll(String msg) {
			for (ChatTread ct : chatThreads) {
				ct.outMsg.println(msg);
				if (check) {
					ct.outMsg.println("client");
					ct.outMsg.println(gson.toJson(userThreads));
				}
			}

		}

		// �������� �ٽ� �޼����, ������ �� �����忡�� ���� ����
		// Ŭ���̾�Ʈ���� �����ϴ� JSON �޽����� �о�� Message ��ü�� ������ �� Message ��ü�� �����Ͽ� �޽��� ������ ���� ó���ϴ�
		// �����̴�
		public void run() {
			try {
				// ���� ����
				m = new Message();
				gson = new Gson();
				inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				outMsg = new PrintWriter(socket.getOutputStream(), true);
				status = true;
				// ���� while( ) ��Ͽ��� �޽����� �� ������ �о� ���ڿ� ������ �޾� �´�
				// ���� ���� ������ true�̸� ���ø� ���鼭 ����ڿ��Լ� ���ŵ� �޼��� ó��
				while (status) {
					// ���ŵ� �޼����� msg ������ ����
					msg = inMsg.readLine();
					// JSON �޼����� Message ��ü�� ����
					m = gson.fromJson(msg, Message.class);
					System.out.println(m.getId());
					System.out.println(m.getType());
					// �Ľ̵� ���ڿ� �迭�� �� ��° ��Ұ��� ���� ó��
					// �α׾ƿ� �޽����� ��
					if (m.getType().equals("logout")) {// �α׾ƿ� �޼��� �� ��
						check = true;// Logout�� �� ������ �Ѱ��ֱ�
						chatThreads.remove(this);
						userThreads.remove(m.getId());// �α׾ƿ� ���� ���� ArrayList���� ���ش�
						msgSendAll(gson.toJson(new Message(m.getId(), "", "���� �����߽��ϴ�", "server")));
						// msgSendAll(gson.toJson(new Message(m.getId(), "", "logout", "userInfo")));
						// �ش� Ŭ���̾�Ʈ ������ ����� status�� false�� ����
						status = false;

					} else if (m.getType().equals("login")) {// �α��� �޼����� ��
						check = true;// Login�� �� ������ �Ѱ��ֱ�
						userThreads.add(m.getId());// �α��� ���� ���� ArrayList�� �߰����ش�
						for (int i = 0; i < userThreads.size(); i++) {// ��Ʈ�ѷ����� �Ѱ��ش�
							System.out.println(userThreads.get(i));
						}
						msgSendAll(gson.toJson(new Message(m.getId(), "", "���� �α��� �߽��ϴ�", "server")));
					} else {// �� ���� ���, �� �Ϲ� �޼����� ��
						check = false;// ������ �Ѱ� ���� ����
						msgSendAll(msg);
					}
				}
				// ������ ����� Ŭ���̾�Ʈ ������ ����ǹǷ� ������ ���ͷ�Ʈ
				this.interrupt();
				logger.info(this.getName() + " �����!!");

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
