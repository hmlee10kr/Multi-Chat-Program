import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JTextArea;

public class MultiChatData {

	protected JTextArea msgOut;
	protected JTextArea usrOut;

	// 데이터를 변경할 때 업데이트할 UI 컴포넌트를 등록하는 메서드이다
	// 범용적으로 사용할 목적으로 JComponent 타입을 파라미터로 받는다
	public void addObj(Component comp) {
		msgOut = (JTextArea) comp;
	}

	public void addUser(Component comp) {
		usrOut = (JTextArea) comp;
	}

	// 파라미터로 전달된 메세지 내용으로, UI 데이터를 업데이트한다
	// 여기서는 채팅 메세지 창의 텍스트를 추가하는 작업만 한다
	public void refreshData(String msg) {
		msgOut.append(msg + "\n");
	}

	// 로그인한 회원정보는 업데이트해주는 메소드
	public void refreshClient(ArrayList<String> client) {
		usrOut.setText("");// 추가, 삭제 되는 회원들을 관리하기 위해 모든 것을 삭제하고 다시 써주는 방식
		for (int i = 0; i < client.size(); i++) {// 다시 써주기
			usrOut.append(client.get(i) + "\n");
		}
	}

	public void refreshClientReset() {// 모든 것을 지워주는 부분
		usrOut.setText("");
	}
}
