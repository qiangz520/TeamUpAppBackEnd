import bean.PersonInfo;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet(name = "InitPersonInfoServlet")
public class InitPersonInfoServlet extends HttpServlet {

    public InitPersonInfoServlet(){
        super();
    }

//   Post方法，用于处理个人信息编辑后提交到数据库请求
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final int UpdateHeaderImage=1;
        final int UpdateNickName=2;
        final int UpdateSex=3;
        final int UpdateSchool=4;
        final int UpdateContactMethod=5;//标识更新哪一项

        int UpdateIndex;
        String UpdateContent;
        int userID;
//        LogUtil.log("GetHeaderInfoServlet:Running");

        request.setCharacterEncoding("UTF-8");

        UpdateIndex=Integer.parseInt(request.getParameter("UpdateIndex"));
        userID=Integer.parseInt(request.getParameter("token"));
        UpdateContent=request.getParameter("UpdateContent");
        Connection connection = DBUtil.getConnection();
        switch (UpdateIndex){
            case UpdateHeaderImage:
                try {
                    Statement statement=connection.createStatement();
                    String sqlUpdate="update "+DBUtil.TABLE_USERINFO+" set HeadImage='"+UpdateContent+"' where userID='"+userID+"'";
                    if(statement.executeUpdate(sqlUpdate)>0){
                        LogUtil.log("更新成功！");
                    }
                    else{
                        LogUtil.log("更新失败！");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case UpdateNickName:
                try {
                    Statement statement=connection.createStatement();
                    String sqlUpdate="update "+DBUtil.TABLE_USERINFO+" set NickName='"+UpdateContent+"' where userID='"+userID+"'";
                    if(statement.executeUpdate(sqlUpdate)>0){
                        LogUtil.log("更新成功！");
                    }
                    else{
                        LogUtil.log("更新失败！");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case UpdateSex:
                try {
                    Statement statement=connection.createStatement();
                    String sqlUpdate="update "+DBUtil.TABLE_USERINFO+" set Sex='"+UpdateContent+"' where userID='"+userID+"'";
                    if(statement.executeUpdate(sqlUpdate)>0){
                        LogUtil.log("更新成功！");
                    }
                    else{
                        LogUtil.log("更新失败！");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case UpdateSchool:
                try {
                    Statement statement=connection.createStatement();
                    String sqlUpdate="update "+DBUtil.TABLE_USERINFO+" set School='"+UpdateContent+"' where userID='"+userID+"'";
                    if(statement.executeUpdate(sqlUpdate)>0){
                        LogUtil.log("更新成功！");
                    }
                    else{
                        LogUtil.log("更新失败！");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case UpdateContactMethod:
                try {
                    Statement statement=connection.createStatement();
                    String sqlUpdate="update "+DBUtil.TABLE_USERINFO+" set ContactMethod='"+UpdateContent+"' where userID='"+userID+"'";
                    if(statement.executeUpdate(sqlUpdate)>0){
                        LogUtil.log("更新成功！");
                    }
                    else{
                        LogUtil.log("更新失败！");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            default:LogUtil.log("出问题了");
        }



    }
//Get方法，初始化个人信息
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userID=Integer.parseInt(request.getParameter("token"));

        String headImage="";
        String nickName="";
        String sex="";
        String school="";
        String contactMethod="";

        Connection connect = DBUtil.getConnection();
        try{
            Statement statement=connect.createStatement();
            String sqlQuery="select HeadImage,NickName,Sex,School,ContactMethod from "+DBUtil.TABLE_USERINFO+" where userID="+userID;
//            LogUtil.log(sqlQuery);
            ResultSet resultSet=statement.executeQuery(sqlQuery);
            if(resultSet.next()){
                headImage=resultSet.getString(1);
                nickName=resultSet.getString(2);
                sex=resultSet.getString(3);
                school=resultSet.getString(4);
                contactMethod=resultSet.getString(5);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        Gson gson=new Gson();
        PersonInfo personInfo=new PersonInfo(headImage,nickName,sex,school,contactMethod);
        String personInfoResponseStr=gson.toJson(personInfo);
        LogUtil.log(personInfoResponseStr);
        response.getWriter().append(personInfoResponseStr);
    }
}
