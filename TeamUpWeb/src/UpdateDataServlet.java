import bean.ResponseState;
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
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(name = "UpdateDataServlet")
public class UpdateDataServlet extends HttpServlet {
    public UpdateDataServlet(){
        super();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String AddJoinNumber="2";//增加参与人数
        final String MinusJoinNumber="3";//减少参与
        final String DeleteActivity="4";//删除活动

        String Update=request.getParameter("update");
        int activityID=Integer.parseInt(request.getParameter("activityID"));
        LogUtil.log("activityID:"+activityID);

        String msg="";

        Connection connection=DBUtil.getConnection();
        if(Update.equals(AddJoinNumber)){
            try{
                Statement statement1=connection.createStatement();
                Statement statement2=connection.createStatement();
                String sqlQuery="select activity_joined_number,activity_max_number from "+DBUtil.TABLE_ACTIVITY_INFO+" where activityID='"+activityID+"'";
                ResultSet resultSet=statement1.executeQuery(sqlQuery);
                if(resultSet.next()){
                    if(resultSet.getInt(1)<resultSet.getInt(2)){
                        String sqlUpdate="update "+DBUtil.TABLE_ACTIVITY_INFO+" set activity_joined_number=activity_joined_number+1 where activityID="+activityID;
                        if(statement2.executeUpdate(sqlUpdate)>0){
                            msg="参与成功！";
                        }
                        else{
                            msg="参与失败！";
                        }
                    }
                    else{
                        msg="此活动人数已满！";
                    }
                }
            } catch (SQLException e) {
                    e.printStackTrace();
                }
        }else if(Update.equals(MinusJoinNumber)){
            try {
                Statement statement1 = connection.createStatement();
                Statement statement2 = connection.createStatement();
                String sqlQuery = "select activity_joined_number,activity_max_number from " + DBUtil.TABLE_ACTIVITY_INFO + " where activityID='" + activityID + "'";
                ResultSet resultSet = statement1.executeQuery(sqlQuery);
                if(resultSet.next()){
                    if(resultSet.getInt(1)>0){
                        String sqlUpdate="update "+DBUtil.TABLE_ACTIVITY_INFO+" set activity_joined_number=activity_joined_number-1 where activityID='"+activityID+"'";
                        if(statement2.executeUpdate(sqlUpdate)>0){
                            msg="你已经取消参与了！";
                        }
                        else{
                            msg="取消失败！";
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(Update.equals(DeleteActivity)){
            try {
                Statement delete=connection.createStatement();
                String sqlDelete="delete from "+DBUtil.TABLE_ACTIVITY_INFO+" where activityID="+activityID;
                LogUtil.log(sqlDelete);
                if(delete.executeUpdate(sqlDelete)>0){
                    msg="删除成功！";
                }
                else{
                    msg="删除失败！";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        response.setContentType("text/html;charset=utf-8");

        Gson gson=new Gson();
        ResponseState updateState=new ResponseState("",msg,"");
        String updateResponseStr=gson.toJson(updateState);
        LogUtil.log(updateResponseStr);
        response.getWriter().append(updateResponseStr);

    }

//    GET方法
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String SetTimeOutInvalid="1";//处理Client的下拉刷新请求


        String currentTime;
        String Update=request.getParameter("update");
        String msg="此次无更新！";//反馈信息
        Date cDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentTime = sdf.format(cDate);
        Connection connection=DBUtil.getConnection();
        if(Update.equals(SetTimeOutInvalid)) {
            try {
                Statement statement = connection.createStatement();
                String sqlUpdate = "update " + DBUtil.TABLE_ACTIVITY_INFO + " set activity_isValid='0' where activity_isValid='1' and activity_time<'" + currentTime + "'";
                int updatedEntryNumber=statement.executeUpdate(sqlUpdate);
                if (updatedEntryNumber>0) {
                    msg = "共更新了" + updatedEntryNumber + "条活动信息";
                } else {
                    msg = "此次无更新！";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        response.setContentType("text/html;charset=utf-8");
        Gson gson=new Gson();
        ResponseState updateState=new ResponseState("",msg,"");
        String updateResponseStr=gson.toJson(updateState);
        LogUtil.log("msg:"+msg);
        response.getWriter().append(updateResponseStr);
    }
}
