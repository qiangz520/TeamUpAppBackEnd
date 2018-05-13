import bean.ResponseState;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@javax.servlet.annotation.WebServlet(name = "LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = "";
        String message = "";
        String Token="";//userID唯一可作为Token，维持登录状态

        String account = request.getParameter("account");
        String password = request.getParameter("password");
        LogUtil.log(account + ";" + password);

        Connection connect = DBUtil.getConnection();
        try {
            Statement statement = connect.createStatement();
            String sql = "select userID,userAccount from " + DBUtil.TABLE_USERINFO + " where userAccount='" + account
                    + "' and userPassword='" + password + "'";
            LogUtil.log(sql);
            ResultSet result = statement.executeQuery(sql);
            if (result.next()) { // 能查到该账号，说明已经注册过了

                code = "200";
                message = "登录成功";
                Token = result.getInt(1)+"";//获得查询结果的第一列
            } else {

                code = "100";
                message = "登录失败，密码不匹配或账号未注册";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.setContentType("text/html;charset=utf-8");
        Gson gson=new Gson();
        ResponseState loginState=new ResponseState(code,message,Token);
        String loginResponseStr=gson.toJson(loginState);
        response.getWriter().append(loginResponseStr);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LogUtil.log("不支持POST方法");
    }

}
