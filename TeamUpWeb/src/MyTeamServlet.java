import bean.TeamInfo;

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

@WebServlet(name = "MyTeamServlet")
public class MyTeamServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userID=Integer.parseInt(request.getParameter("token"));

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
            String sqlQuery="select * from "+DBUtil.TABLE_ACTIVITY_INFO+" where activity_issuer_id="+userID;
            ResultSet resultMyTeamSet=statement1.executeQuery(sqlQuery);
            while (resultMyTeamSet.next()){
                activity_id=resultMyTeamSet.getInt(1);
                title=resultMyTeamSet.getString(4);
                description=resultMyTeamSet.getString(6);
                issueTime=resultMyTeamSet.getString(7);
                startTime=resultMyTeamSet.getString(8);
                place=resultMyTeamSet.getString(9);
                maxNumber=resultMyTeamSet.getInt(10);
                demand=resultMyTeamSet.getString(11);
                joinedNumber=resultMyTeamSet.getInt(12);
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
