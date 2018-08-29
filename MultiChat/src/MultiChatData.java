import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JTextArea;

public class MultiChatData {

	protected JTextArea msgOut;
	protected JTextArea usrOut;

	// �����͸� ������ �� ������Ʈ�� UI ������Ʈ�� ����ϴ� �޼����̴�
	// ���������� ����� �������� JComponent Ÿ���� �Ķ���ͷ� �޴´�
	public void addObj(Component comp) {
		msgOut = (JTextArea) comp;
	}

	public void addUser(Component comp) {
		usrOut = (JTextArea) comp;
	}

	// �Ķ���ͷ� ���޵� �޼��� ��������, UI �����͸� ������Ʈ�Ѵ�
	// ���⼭�� ä�� �޼��� â�� �ؽ�Ʈ�� �߰��ϴ� �۾��� �Ѵ�
	public void refreshData(String msg) {
		msgOut.append(msg + "\n");
	}

	// �α����� ȸ�������� ������Ʈ���ִ� �޼ҵ�
	public void refreshClient(ArrayList<String> client) {
		usrOut.setText("");// �߰�, ���� �Ǵ� ȸ������ �����ϱ� ���� ��� ���� �����ϰ� �ٽ� ���ִ� ���
		for (int i = 0; i < client.size(); i++) {// �ٽ� ���ֱ�
			usrOut.append(client.get(i) + "\n");
		}
	}

	public void refreshClientReset() {// ��� ���� �����ִ� �κ�
		usrOut.setText("");
	}
}
