
//클라이언트와 서버 간의 통신에 사용하는 JSON 규격의 메시지를 좀 더 쉽게 사용하려고
//자바 객체로 변환하는 데 필요한 클래스이다
public class Message {

	private String id;// 아이디
	private String password;// 비밀번호
	private String msg;// 채팅 메세지
	private String type;// 메세지 유형(로그인, 로그아웃, 메세지 전달)

	public Message() {

	}

	// 매개변수 4개 있는 생성자
	public Message(String id, String password, String msg, String type) {
		this.id = id;
		this.password = password;
		this.msg = msg;
		this.type = type;
	}

	// 매개변수 1개 있는 생성자
	public Message(Message M) {
		this.id = M.id;
		this.password = M.password;
		this.msg = M.msg;
		this.type = M.type;
	}

	// 메세지 유형 반환
	public String getType() {
		return type;
	}

	// 아이디 반환
	public String getId() {
		return id;
	}

	// 메세지 반환
	public String getMsg() {
		return msg;
	}

	// 비밀번호 반환
	public String getPassword() {
		return password;
	}
}
