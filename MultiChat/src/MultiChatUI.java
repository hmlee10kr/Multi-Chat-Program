import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MultiChatUI extends JFrame {

	private JPanel loginPanel;// 로그인 패널
	protected JButton loginButton;// 로그인 버튼
	private JLabel inLabel;// 대화명 라벨
	protected JTextField idInput;// 대화명 입력 텍스트 필드

	private JPanel logoutPanel;// 로그아웃 패널
	protected JLabel outLabel;// 대화명 출력 라벨
	protected JButton logoutButton;// 로그아웃 버튼

	private JPanel msgPanel;// 메세지 입력 패널 구성
	protected JTextField msgInput;// 메세지 입력 텍스트 필드
	protected JButton exitButton;// 종료버튼

	// 화면 구성 전환을 위한 카드 레이아웃
	protected Container tab;
	protected CardLayout cardLayout;

	protected JTextArea msgOut;// 채팅 내용 출력 창

	protected String id;

	protected JTextArea userOut;// 로그인한 사람을 표시해주는 창

	// 생성자로, 화면을 구성하는 컴포넌트 초기화 및 레이아웃 배치 등의 전반적인 내용을 구현한다
	public MultiChatUI() {
		// 메인 프레임 구성
		super("::멀티쳇::");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// 로그인 패널 화면 구성
		loginPanel = new JPanel();
		// 로그인 패널 레이아웃 설정
		loginPanel.setLayout(new BorderLayout());

		// 로그인 입력필드/버튼 생성
		idInput = new JTextField(15);
		loginButton = new JButton("로그인");

		// 로그인 패널에 위젯 구성
		inLabel = new JLabel("대화명 ");

		loginPanel.add(inLabel, BorderLayout.WEST);
		loginPanel.add(idInput, BorderLayout.CENTER);
		loginPanel.add(loginButton, BorderLayout.EAST);

		// 로그아웃 패널 구성
		logoutPanel = new JPanel();

		// 로그아웃 패널 레이아웃 설정
		logoutPanel.setLayout(new BorderLayout());
		outLabel = new JLabel();
		logoutButton = new JButton("로그아웃");

		// 로그아웃 패널에 위젯 구성
		logoutPanel.add(outLabel, BorderLayout.CENTER);
		logoutPanel.add(logoutButton, BorderLayout.EAST);

		// 메세지 입력 패널 구성
		msgPanel = new JPanel();
		// 메세지 입력 패널 레이아웃 설정
		msgPanel.setLayout(new BorderLayout());

		// 메세지 입력 패널에 위젯 구성
		exitButton = new JButton("종료");
		msgInput = new JTextField();
		msgPanel.add(msgInput, BorderLayout.CENTER);
		msgPanel.add(exitButton, BorderLayout.EAST);

		// 로그인/로그아웃 패널 중 하나를 선택하는 CardLayout 패널
		tab = new JPanel();
		cardLayout = new CardLayout();
		tab.setLayout(cardLayout);
		tab.add(loginPanel, "login");
		tab.add(logoutPanel, "logout");

		// 메세지 출력 영역 초기화
		msgOut = new JTextArea("", 10, 30);
		// JTextArea의 내용을 수정하지 못하도록 한다. 즉, 출력 전용으로 사용한다
		msgOut.setEditable(false);

		// 메세지 출력 영역 스크롤 바를 구성한다
		// 수직 스크롤 바는 항상 나타내고, 수평 스크롤바는 필요할 때 나타나도록 한다
		JScrollPane jsp = new JScrollPane(msgOut, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// 로그인한 회원 정보 출력 영역 초기화
		userOut = new JTextArea("", 10, 15);
		userOut.setEditable(false);
		// 로그인한 회원 정보 출력 영역 스크롤 바를 구성한다
		JScrollPane jsp2 = new JScrollPane(userOut, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// 프레임에 추가
		getContentPane().add(tab, BorderLayout.PAGE_START);
		getContentPane().add(jsp, BorderLayout.EAST);
		getContentPane().add(jsp2, BorderLayout.WEST);
		getContentPane().add(msgPanel, BorderLayout.PAGE_END);

		// 프레임이 보이도록
		pack();
		setVisible(true);
	}

	// 이벤트 핸들러 등록 메서드로, 모든 버튼의 이벤트 핸들러를 한곳에서 등록한다
	public void addButtonActionListeners(ActionListener listener) {

		loginButton.addActionListener(listener);
		logoutButton.addActionListener(listener);
		exitButton.addActionListener(listener);
		msgInput.addActionListener(listener);
	}

}
