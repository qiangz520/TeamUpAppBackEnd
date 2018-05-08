import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@javax.servlet.annotation.WebServlet(name = "LoginServlet")
public class LoginServlet extends javax.servlet.http.HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public LoginServlet() {

    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String account = request.getParameter("account"); // 从 request 中获取名为 account 的参数的值
        String password = request.getParameter("password"); // 从 request 中获取名为 password 的参数的值
        System.out.println("account:" + account + "\npassword:" + password); // 打印出来看一看

        String result = "";
        if("abc".equals(account)
                && "123".equals(password)){ // 模拟登陆账号、密码验证
            result = "Login Success!";
        }else {
            result = "Sorry! Account or password error.";
        }
        /* 这里我们只是模拟了一个最简单的业务逻辑，当然，你的实际业务可以相当复杂 */

        PrintWriter pw = response.getWriter(); // 获取 response 的输出流
        pw.println(result); // 通过输出流把业务逻辑的结果输出
        pw.flush();

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // 默认的代码，意思就是说做doPost和doGet一样
        // ***但实质上可行不可行呢？我们接下来或者下一篇就会说到
    }
}
