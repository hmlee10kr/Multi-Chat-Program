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

//Ŭ���̾�Ʈ ���׷��� �ٽ� �κ����� MultiChatController Ŭ������ ������ ����� ����ϰ�, 
//Ŭ���̾�Ʈ���� �߻��ϴ� �̺�Ʈ�� ���� UI�� �����ϰų� ���� �� �޽����� ����ϴ� �� ���α׷��� �������� ������ ó���ϴ� �κ��̴�
public class MultiChatController implements Runnable {

	private final MultiChatUI v;// �� Ŭ���� ���� ��ü
	private final MultiChatData chatData;// ������ Ŭ���� ���� ��ü
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
	private ArrayList<String> client;// �α����� ȸ�� ������ �޾��ִ� ArrayList

	// �𵨰� �� ��ü�� �Ķ���ͷ� �ϴ� �������̴�
	public MultiChatController(MultiChatData data, MultiChatUI ui) {
		// �𵨰� ��Ŭ���� ����
		v = ui;
		chatData = data;

		// �ΰ� ��ü �ʱ�ȭ
		logger = Logger.getLogger(this.getClass().getName());
		ip = "127.0.0.1";
		gson = new Gson();
		client = new ArrayList<String>();// ArrayList �ʱ�ȭ
	}

	// ��Ʈ�ѷ� Ŭ������ ���� ���� �κ�����, UI���� �߻��� �̺�Ʈ�� ���ӹ޾� ó���Ѵ�
	public void appMain() {
		// ������ ��ü���� ������ ��ȭ�� ó���� UI ��ü �߰�
		chatData.addObj(v.msgOut);
		chatData.addUser(v.userOut);
		// �� Ŭ�������� ��ư���� �̺�Ʈ �ڵ鷯�� ����ϴ� addButtonActionListener( ) �޼��带 ȣ���ϸ鼭 ������ Ŭ������ �͸���
		// ���� Ŭ������ ����� �����Ѵ�
		v.addButtonActionListeners(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Object obj = e.getSource();

				if (obj == v.exitButton) {// ���� ��ư ó��
					if (outMsg != null)
						outMsg.println(gson.toJson(new Message(v.id, "", "", "logout")));
					System.exit(0);
				} else if (obj == v.loginButton) {// �α��� ���� ��ȯ
					v.id = v.idInput.getText();
					v.outLabel.setText("��ȭ�� : " + v.id);
					v.cardLayout.show(v.tab, "logout");
					ConnectServer();
				} else if (obj == v.logoutButton) {// �α׾ƿ� ���� ��ȯ
					outMsg.println(gson.toJson(new Message(v.id, "", "", "logout")));
					v.msgOut.setText("");
					v.cardLayout.show(v.tab, "login");
					chatData.refreshClientReset();// �α׾ƿ��Ҷ� �α��ε� ȸ�� ������ �ƿ� �����ֱ�
					outMsg.close();
					try {
						inMsg.close();
						socket.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					status = false;
				} else if (obj == v.msgInput) {// �޼��� ����
					outMsg.println(gson.toJson(new Message(v.id, "", v.msgInput.getText(), "msg")));
					v.msgInput.setText("");
				}
			}
		});

	}

	// �α��� ��ư�� ������ �� ȣ��Ǵ� �޼����̴�
	// ������ �����ϰ� ����� ��Ʈ���� ���� �� �޽��� ���ſ� �ʿ��� �����带 �����Ѵ�
	// ä�� ���� ������ ���� �޼����̴�
	public void ConnectServer() {
		try {
			// ���� ����
			socket = new Socket(ip, 4005);
			logger.info("[Client]Server ���� ����");

			// �Է� ��Ʈ�� ����
			inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// ��� ��Ʈ�� ����
			outMsg = new PrintWriter(socket.getOutputStream(), true);
			// ������ �α��� �޼��� ����
			m = new Message(v.id, "", "", "login");
			outMsg.println(gson.toJson(m));

			// �޼��� ������ ���� ������ ����
			thread = new Thread(this);
			thread.start();

		} catch (Exception e) {// ���� ó��
			logger.warning("[MultiChatUI]connectServer() Exception �߻�!");
			e.printStackTrace();
		}
	}

	// UI ����� ���������� ������ ��Ʈ��ũ ������ �����ϸ�, ���ŵ� �޽����� ó���ϴ� ������ ����Ѵ�
	// ���� ���� �� �޼��� ������ UI ���۰� ������� ���������� ó���ϴ� �����带 �����Ѵ�
	public void run() {
		// ���� �޼����� ó���ϴµ� �ʿ��� ���� ����
		String msg;
		status = true;

		// while( )���� �ݺ��ϸ鼭 �������� �����ϴ� �޽����� �� ������ �о�� JSON �޽����� Message ��ü�� ��ȯ�Ѵ�
		while (status) {
			try {
				// �޼��� ���� �� �Ľ�
				msg = inMsg.readLine();
				if (msg.equals("client")) {
					client = gson.fromJson(inMsg.readLine(), ArrayList.class);// ArrayList�������� String �޾ƿ���
					chatData.refreshClient(client);// ���� ������Ʈ
				} else {
					m = gson.fromJson(msg, Message.class);
					// MultiChatData ��ü�� ������ ����

					// �α��ε� ȸ�� ���� ������Ʈ
					chatData.refreshData(m.getId() + ">" + m.getMsg() + "\n");

					// Ŀ���� ���� ��ȭ �޼����� ǥ��
					v.msgOut.setCaretPosition(v.msgOut.getDocument().getLength());
					v.userOut.setCaretPosition(v.msgOut.getDocument().getLength());
				}
			} catch (Exception e) {// ����ó��

			}
		}
	}

	// ���� ���� �޼���
	// MultiChatController Ŭ���� �������� �Ķ���ͷ� ��� �� Ŭ������ �����ϰ�, appMain( ) �޼��带 �����Ѵ�
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		MultiChatController app = new MultiChatController(new MultiChatData(), new MultiChatUI());
		app.appMain();
	}
}
