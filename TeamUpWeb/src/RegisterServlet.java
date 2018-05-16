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

@WebServlet(description = "注册使用的Servlet",name = "RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public RegisterServlet() {
        LogUtil.log("RegisterServlet construct...");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String method = request.getMethod();
        if ("GET".equals(method)) {
            LogUtil.log("请求方法：GET");
            doGet(request, response);
        } else if ("POST".equals(method)) {
            LogUtil.log("请求方法：POST");
            doPost(request, response);
        } else {
            LogUtil.log("请求方法分辨失败！");
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = "";
        String message = "";
        String Token="";//不使用

        String account = request.getParameter("account");
        String password = request.getParameter("password");
        LogUtil.log(account + ";" + password);

        Connection connect = DBUtil.getConnection();
        try {
            Statement statement = connect.createStatement();
            String sql = "select userAccount from " + DBUtil.TABLE_USERINFO + " where userAccount='" + account + "'";
            LogUtil.log(sql);
            ResultSet result = statement.executeQuery(sql);
            if (result.next()) { // 能查到该账号，说明已经注册过了
                code = "100";
                message = "该账号已注册";
            } else {
                String Empty="";
                String NeverEdit="未填写";
                int zero=0;
                String sqlInsert = "insert into " + DBUtil.TABLE_USERINFO + "(userAccount, userPassword,HeadImage,NickName,Sex,School,ContactMethod,StudyLike,SportsLike,OutdoorsLike,GroupBookLike,CompetitionLike,FuntimeLike) values('"
                        + account + "', '" + password + "','"+Empty+"','"+NeverEdit+"','"+NeverEdit+"','"+NeverEdit+"','"+NeverEdit+"','"+zero+"','"+zero+"','"+zero+"','"+zero+"','"+zero+"','"+zero+"')";
                LogUtil.log(sqlInsert);
                if (statement.executeUpdate(sqlInsert) > 0) { // 否则进行注册逻辑，插入新账号密码到数据库
                    code = "200";
                    message = "注册成功";
                } else {
                    code = "300";
                    message = "注册失败";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");

        Gson gson=new Gson();
        ResponseState regState=new ResponseState(code,message,Token);
        String regResponseStr=gson.toJson(regState);
        response.getWriter().append(regResponseStr);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }


    @Override
    public void destroy() {
        LogUtil.log("RegisterServlet destory.");
        super.destroy();
    }

}
