import bean.ResponseState;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
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

//        BufferedReader reader=request.getReader();
//        String requestStr=reader.readLine();
//        LogUtil.log("ContentType:"+request.getContentType());
//        LogUtil.log("URl:"+request.getRequestURL());
//        LogUtil.log("CEncoding:"+request.getCharacterEncoding());
//        LogUtil.log(""+request.get)
//        LogUtil.log("requestStr:"+requestStr);//查看请求

        Connection connection=DBUtil.getConnection();
        try{
            Statement statement=connection.createStatement();
            String sqlInsert="insert into "+DBUtil.TABLE_ACTIVITY_INFO+"(activity_isValid,activity_issuer_id,activity_title," +
                    "activity_description,activity_kind,activity_issue_time,activity_time,activity_place,activity_max_number," +
                    "activity_demand)values("+isValid+","+token+",'"+title+"','"+description+"','"+category+"','"+currentTime+"','"+datetime+"','"+
                    place+"',"+number+",'"+demand+"')";
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //
    }
}
