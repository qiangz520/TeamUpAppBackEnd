import bean.SearchMatch;
import bean.TeamInfo;
import com.baidu.aip.nlp.AipNlp;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static NLP.Constant.*;

@WebServlet(name = "SearchServlet")
public class SearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String text=request.getParameter("text");

        AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 调用接口
        JSONObject res = client.lexer(text, null);

        ArrayList<String> itemsList=getRe(res.toString(),"\"item\":\"(.*?)\",");

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

        int c=0;//用于给分词编号

        Connection connection = DBUtil.getConnection();
        ArrayList<String> sqlQueryList=new ArrayList<>();//多个查询语句列表
        for(int i = 0; i < itemsList.size(); i++){
            c++;
            System.out.println("items"+c+":"+itemsList.get(i));
            String sqlQuery="select * from "+DBUtil.TABLE_ACTIVITY_INFO+" where activity_isValid=1 and(activity_title like'%"+itemsList.get(i)+"%' or " + " activity_description like'%"+itemsList.get(i)+"%')";
            sqlQueryList.add(sqlQuery);
            try {
                Statement s=connection.createStatement();
                s.executeQuery(sqlQuery);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        String sqlSearch=sqlQueryList.get(0);
        for(int i=1;i<sqlQueryList.size();i++){
            sqlSearch=sqlSearch+ " union "+sqlQueryList.get(i);
        }
        sqlSearch=sqlSearch+" order by activity_issue_time desc,activity_weight desc";
        LogUtil.log(sqlSearch);

        ArrayList<SearchMatch> searchMatchList=new ArrayList<>();//匹配结果列表

        try {
            Statement statement1=connection.createStatement();
            Statement statement2=connection.createStatement();
            ResultSet SearchResultSet=statement1.executeQuery(sqlSearch);
            while(SearchResultSet.next()){
                int count=0;//所有分词在当前这条数据的出现次数
                activity_id=SearchResultSet.getInt(1);
                int issuerID=SearchResultSet.getInt(3);
                title=SearchResultSet.getString(4);
                count+=ContainCount(itemsList,title);//对title进行词频统计

                description=SearchResultSet.getString(6);
                count+=ContainCount(itemsList,description);//对description进行词频统计

                issueTime=SearchResultSet.getString(7);
                startTime=SearchResultSet.getString(8);
                place=SearchResultSet.getString(9);
                count+=ContainCount(itemsList,place);//对place进行词频统计

                maxNumber=SearchResultSet.getInt(10);
                demand=SearchResultSet.getString(11);
                count+=ContainCount(itemsList,demand);//对demand进行词频统计

                joinedNumber=SearchResultSet.getInt(12);
                String sqlGetUserInfo="select NickName,ContactMethod from "+DBUtil.TABLE_USERINFO+" where userID='"+issuerID+"'";
                ResultSet resultSet1=statement2.executeQuery(sqlGetUserInfo);
                if(resultSet1.next()){
                    issuerNickName=resultSet1.getString(1);
                    issuerContactMethod=resultSet1.getString(2);
                }
                TeamInfo teamInfo=new TeamInfo(activity_id, issuerNickName,title,description,issueTime,startTime,place,demand,issuerContactMethod, maxNumber,joinedNumber);
                SearchMatch searchMatch=new SearchMatch(teamInfo,count);
                searchMatchList.add(searchMatch);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Collections.sort(searchMatchList);//对匹配结果按分词出现计数进行降序排序

        StringBuilder stringBuilder=new StringBuilder("[");
        for(SearchMatch searchMatch : searchMatchList){
            stringBuilder.append(searchMatch.getTeamInfo().toString());
            stringBuilder.append(",");
            LogUtil.log("CNT:"+searchMatch.getContainCnt());
        }


//        stringBuilder.append(bestMatch.toString());
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        stringBuilder.append("]");
        String searchInfoResponseStr=stringBuilder.toString();//形成最终要返回的JSON字符串
        LogUtil.log(searchInfoResponseStr);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().append(searchInfoResponseStr);


    }

    //根据正则表达式语法匹配(仅仅输出匹配结果)--两者区别见主函数
    private static ArrayList<String> getRe(String text, String patterString){
        ArrayList<String> l= new ArrayList<>();
        Pattern pattern = Pattern.compile(patterString);
        Matcher m = pattern.matcher(text);
        while (m.find()) {
            for(int i=1;i<m.groupCount()+1;i++)
            {
                l.add(m.group(i));
            }
        }
        return l;
    }
    //统计分词列表每个分词出现在某一个字符串中的次数
    private static int ContainCount(ArrayList<String> itemsList,String targetStr){
        int count=0;
        for(int i=0;i<itemsList.size();i++){
            if(targetStr.contains(itemsList.get(i))){
                count+=1;
            }
        }
        return count;
    }
}
