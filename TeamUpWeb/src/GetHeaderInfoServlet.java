import bean.HeaderInfoClass;
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

@WebServlet(name = "GetHeaderInfoServlet")
public class GetHeaderInfoServlet extends HttpServlet {
    public GetHeaderInfoServlet(){
        super();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



    }
//用于处理客户端加载主界面侧滑菜单头部信息的Get请求
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        int userID=Integer.parseInt(request.getParameter("token"));
        String HeadImage = "";
        String NickName="";
        String ContactMethod="";

        Connection connection= DBUtil.getConnection();


        try {
            Statement statement=connection.createStatement();
            String sqlQuery="select HeadImage,NickName,ContactMethod from "+DBUtil.TABLE_USERINFO+" where userID="+userID;
            ResultSet resultSet=statement.executeQuery(sqlQuery);
            if(resultSet.next()){
                HeadImage=resultSet.getString(1);
                NickName=resultSet.getString(2);
                ContactMethod=resultSet.getString(3);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.setContentType("text/html;charset=utf-8");
        Gson gson=new Gson();
        HeaderInfoClass headerInfo=new HeaderInfoClass(HeadImage,NickName,ContactMethod);
        String headerInfoResponseStr=gson.toJson(headerInfo);
        LogUtil.log(headerInfoResponseStr);
        response.getWriter().append(headerInfoResponseStr);

    }
}
