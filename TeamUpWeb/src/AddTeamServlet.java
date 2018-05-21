import bean.ResponseState;
import bean.TeamInfo;
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

@WebServlet(name = "AddTeamServlet")
public class AddTeamServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AddTeamServlet(){
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isValid=true;
        int token;
        String title,description,category,datetime,currentTime,place;
        int number;
        String demand;

        String code="";
        String message="";//返回信息
        String never_use="";

        request.setCharacterEncoding("UTF-8");

        token=Integer.parseInt(request.getParameter("token"));
        title= request.getParameter("title");
        description=request.getParameter("description");
        category=request.getParameter("category");
        datetime=request.getParameter("datetime");
        currentTime=request.getParameter("currentTime");
        place=request.getParameter("place");
        number= Integer.parseInt(request.getParameter("number"));
        demand=request.getParameter("demand");

        Connection connection=DBUtil.getConnection();
        try{
            Statement statement=connection.createStatement();
            int zero=0;
            String sqlInsert="insert into "+DBUtil.TABLE_ACTIVITY_INFO+"(activity_isValid,activity_issuer_id,activity_title," +
                    "activity_description,activity_kind,activity_issue_time,activity_time,activity_place,activity_max_number," +
                    "activity_demand,activity_joined_number,activity_weight)values("+isValid+","+token+",'"+title+"','"+description+"','"+category+"','"+currentTime+"','"+datetime+"','"+
                    place+"',"+number+",'"+demand+"','"+zero+"','"+zero+"')";
            LogUtil.log("sqlInsert:"+sqlInsert);
            if(statement.executeUpdate(sqlInsert)>0){
                code="200";
                message="发布成功";
            }
            else{
                code="300";
                message="发布失败";

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        response.setContentType("text/html;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        Gson gson=new Gson();
        ResponseState issueState=new ResponseState(code,message,never_use);
        String issueResponseStr=gson.toJson(issueState);
        LogUtil.log(issueResponseStr);
        response.getWriter().append(issueResponseStr);
    }
//GET方法用于处理Client的获取对应类下的活动信息
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String kindName=request.getParameter("kindName");
        int activity_id;
        String issuerNickName="";
        String title;
        String description;
        String issueTime;
        String startTime;
        String place;
        String demand;
        String issuerContactMethod="";
        int maxNumber;
        int joinedNumber;

        StringBuilder stringBuilder=new StringBuilder("[");
        Connection connection=DBUtil.getConnection();
        try{
            Statement statement1=connection.createStatement();
            Statement statement2=connection.createStatement();//*用两个Statement对象避免java.sql.SQLException: Operation not allowed after ResultSet closed
            String sqlQuery="select * from "+DBUtil.TABLE_ACTIVITY_INFO+" where activity_kind='"+kindName+"'and activity_isValid='1' order  by  activity_issue_time desc,activity_weight desc";
            ResultSet resultSet=statement1.executeQuery(sqlQuery);
            while (resultSet.next()){
                activity_id=resultSet.getInt(1);
                int issuerID=resultSet.getInt(3);
                title=resultSet.getString(4);
                description=resultSet.getString(6);
                issueTime=resultSet.getString(7);
                startTime=resultSet.getString(8);
                place=resultSet.getString(9);
                maxNumber=resultSet.getInt(10);
                demand=resultSet.getString(11);
                joinedNumber=resultSet.getInt(12);
                String sqlGetUserInfo="select NickName,ContactMethod from "+DBUtil.TABLE_USERINFO+" where userID='"+issuerID+"'";
                ResultSet resultSet1=statement2.executeQuery(sqlGetUserInfo);
                if(resultSet1.next()){
                    issuerNickName=resultSet1.getString(1);
                    issuerContactMethod=resultSet1.getString(2);
                }
                TeamInfo teamInfo=new TeamInfo(activity_id, issuerNickName,title,description,issueTime,startTime,place,demand,issuerContactMethod, maxNumber,joinedNumber);
                stringBuilder.append(teamInfo.toString());
                stringBuilder.append(",");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        stringBuilder.append("]");
        String ActivityInfoResponseStr=stringBuilder.toString();
        LogUtil.log(ActivityInfoResponseStr);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().append(ActivityInfoResponseStr);
    }
}
