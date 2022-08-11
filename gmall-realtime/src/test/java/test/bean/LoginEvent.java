package test.bean;

/**
 * description:
 * Created by 铁盾 on 2021/12/21
 */
public class LoginEvent {
    public String userId;
    public String type;
    public Long ts;

    @Override
    public String toString() {
        return "LoginEvent{" +
                "userId='" + userId + '\'' +
                ", type='" + type + '\'' +
                ", ts=" + ts +
                '}';
    }

    public LoginEvent() {
    }

    public LoginEvent(String userId, String type, Long ts) {
        this.userId = userId;
        this.type = type;
        this.ts = ts;
    }
}
