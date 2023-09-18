package top.leftblue.publish.metaweblog.exception;

public class NoCookieException extends RuntimeException{
    public NoCookieException() {
        super("账号不存在或密码错误");
    }
}
