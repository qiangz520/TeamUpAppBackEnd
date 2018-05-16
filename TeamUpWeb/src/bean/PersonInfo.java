package bean;

import java.io.Serializable;

public class PersonInfo implements Serializable {

    String headImage,nickName,sex,school,contactMethod;
    //    用于个人资料界面加载全部个人信息时，解析Json用的构造函数
    public PersonInfo(String headImage,String nickName,String sex,String school,String contactMethod){
        this.headImage=headImage;
        this.nickName=nickName;
        this.sex=sex;
        this.school=school;
        this.contactMethod=contactMethod;
    }

}
