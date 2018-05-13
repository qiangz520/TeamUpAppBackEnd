package bean;
//成功失败状态返回信息，code，message
public class ResponseState {
    String code;
    String msg;
    String Token;//只在登录成功时使用
    public ResponseState(String code, String msg, String Token ){
        this.code=code;
        this.msg=msg;
        this.Token=Token;
    }
}
