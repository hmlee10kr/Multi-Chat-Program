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

	private JPanel loginPanel;// �α��� �г�
	protected JButton loginButton;// �α��� ��ư
	private JLabel inLabel;// ��ȭ�� ��
	protected JTextField idInput;// ��ȭ�� �Է� �ؽ�Ʈ �ʵ�

	private JPanel logoutPanel;// �α׾ƿ� �г�
	protected JLabel outLabel;// ��ȭ�� ��� ��
	protected JButton logoutButton;// �α׾ƿ� ��ư

	private JPanel msgPanel;// �޼��� �Է� �г� ����
	protected JTextField msgInput;// �޼��� �Է� �ؽ�Ʈ �ʵ�
	protected JButton exitButton;// �����ư

	// ȭ�� ���� ��ȯ�� ���� ī�� ���̾ƿ�
	protected Container tab;
	protected CardLayout cardLayout;

	protected JTextArea msgOut;// ä�� ���� ��� â

	protected String id;

	protected JTextArea userOut;// �α����� ����� ǥ�����ִ� â

	// �����ڷ�, ȭ���� �����ϴ� ������Ʈ �ʱ�ȭ �� ���̾ƿ� ��ġ ���� �������� ������ �����Ѵ�
	public MultiChatUI() {
		// ���� ������ ����
		super("::��Ƽ��::");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// �α��� �г� ȭ�� ����
		loginPanel = new JPanel();
		// �α��� �г� ���̾ƿ� ����
		loginPanel.setLayout(new BorderLayout());

		// �α��� �Է��ʵ�/��ư ����
		idInput = new JTextField(15);
		loginButton = new JButton("�α���");

		// �α��� �гο� ���� ����
		inLabel = new JLabel("��ȭ�� ");

		loginPanel.add(inLabel, BorderLayout.WEST);
		loginPanel.add(idInput, BorderLayout.CENTER);
		loginPanel.add(loginButton, BorderLayout.EAST);

		// �α׾ƿ� �г� ����
		logoutPanel = new JPanel();

		// �α׾ƿ� �г� ���̾ƿ� ����
		logoutPanel.setLayout(new BorderLayout());
		outLabel = new JLabel();
		logoutButton = new JButton("�α׾ƿ�");

		// �α׾ƿ� �гο� ���� ����
		logoutPanel.add(outLabel, BorderLayout.CENTER);
		logoutPanel.add(logoutButton, BorderLayout.EAST);

		// �޼��� �Է� �г� ����
		msgPanel = new JPanel();
		// �޼��� �Է� �г� ���̾ƿ� ����
		msgPanel.setLayout(new BorderLayout());

		// �޼��� �Է� �гο� ���� ����
		exitButton = new JButton("����");
		msgInput = new JTextField();
		msgPanel.add(msgInput, BorderLayout.CENTER);
		msgPanel.add(exitButton, BorderLayout.EAST);

		// �α���/�α׾ƿ� �г� �� �ϳ��� �����ϴ� CardLayout �г�
		tab = new JPanel();
		cardLayout = new CardLayout();
		tab.setLayout(cardLayout);
		tab.add(loginPanel, "login");
		tab.add(logoutPanel, "logout");

		// �޼��� ��� ���� �ʱ�ȭ
		msgOut = new JTextArea("", 10, 30);
		// JTextArea�� ������ �������� ���ϵ��� �Ѵ�. ��, ��� �������� ����Ѵ�
		msgOut.setEditable(false);

		// �޼��� ��� ���� ��ũ�� �ٸ� �����Ѵ�
		// ���� ��ũ�� �ٴ� �׻� ��Ÿ����, ���� ��ũ�ѹٴ� �ʿ��� �� ��Ÿ������ �Ѵ�
		JScrollPane jsp = new JScrollPane(msgOut, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// �α����� ȸ�� ���� ��� ���� �ʱ�ȭ
		userOut = new JTextArea("", 10, 15);
		userOut.setEditable(false);
		// �α����� ȸ�� ���� ��� ���� ��ũ�� �ٸ� �����Ѵ�
		JScrollPane jsp2 = new JScrollPane(userOut, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// �����ӿ� �߰�
		getContentPane().add(tab, BorderLayout.PAGE_START);
		getContentPane().add(jsp, BorderLayout.EAST);
		getContentPane().add(jsp2, BorderLayout.WEST);
		getContentPane().add(msgPanel, BorderLayout.PAGE_END);

		// �������� ���̵���
		pack();
		setVisible(true);
	}

	// �̺�Ʈ �ڵ鷯 ��� �޼����, ��� ��ư�� �̺�Ʈ �ڵ鷯�� �Ѱ����� ����Ѵ�
	public void addButtonActionListeners(ActionListener listener) {

		loginButton.addActionListener(listener);
		logoutButton.addActionListener(listener);
		exitButton.addActionListener(listener);
		msgInput.addActionListener(listener);
	}

}
