
//Ŭ���̾�Ʈ�� ���� ���� ��ſ� ����ϴ� JSON �԰��� �޽����� �� �� ���� ����Ϸ���
//�ڹ� ��ü�� ��ȯ�ϴ� �� �ʿ��� Ŭ�����̴�
public class Message {

	private String id;// ���̵�
	private String password;// ��й�ȣ
	private String msg;// ä�� �޼���
	private String type;// �޼��� ����(�α���, �α׾ƿ�, �޼��� ����)

	public Message() {

	}

	// �Ű����� 4�� �ִ� ������
	public Message(String id, String password, String msg, String type) {
		this.id = id;
		this.password = password;
		this.msg = msg;
		this.type = type;
	}

	// �Ű����� 1�� �ִ� ������
	public Message(Message M) {
		this.id = M.id;
		this.password = M.password;
		this.msg = M.msg;
		this.type = M.type;
	}

	// �޼��� ���� ��ȯ
	public String getType() {
		return type;
	}

	// ���̵� ��ȯ
	public String getId() {
		return id;
	}

	// �޼��� ��ȯ
	public String getMsg() {
		return msg;
	}

	// ��й�ȣ ��ȯ
	public String getPassword() {
		return password;
	}
}
