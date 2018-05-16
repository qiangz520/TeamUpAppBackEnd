package bean;

/**
 * Created by qiang on 2018/5/16.
 */

public class HeaderInfoClass {
    private String headImage, nickName, contactMethod;

    public HeaderInfoClass(String headImage, String nickName, String contactMethod) {
        this.headImage = headImage;
        this.nickName = nickName;
        this.contactMethod = contactMethod;
    }

    public String getNickName() {
        return nickName;
    }

    public String getHeadImage() {

        return headImage;
    }

    public String getContactMethod() {

        return contactMethod;
    }
}
