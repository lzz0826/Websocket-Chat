package tw.tony.com.model;

//返回前端的驗證結果集
public class ResultSet {

	private String code; // 返回碼

	private String msg; // 返回信息

	private Object data= null; // 返回數據，默認設為null，需要返回數據時，使用setData()方法

	private String token;
	
	private String uid;
	
	
	
    public String getUid() {
		return uid;
	}



	public void setUid(String uid) {
		this.uid = uid;
	}



	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public String getMsg() {
		return msg;
	}



	public void setMsg(String msg) {
		this.msg = msg;
	}



	public Object getData() {
		return data;
	}
	
	
	



	public String getToken() {
		return token;
	}



	public void setToken(String token) {
		this.token = token;
	}



	public void setData(Object data) {
		this.data = data;
	}
	
	public void success(String msg) {
		this.code = "1";
		this.msg = msg;	
	}
	
	public void fail(String msg) {
		this.code = "0";
		this.msg = msg;
	}
	
	public void error() {
		this.code = "error";
		this.msg = "異常錯誤";
	}



	@Override
    public String toString() {
        return "ResultSet{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data + '\'' +
                ", token=" + token + '\'' +
                ", uid=" + uid +
                '}';
    }

}
