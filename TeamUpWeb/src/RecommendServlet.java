import bean.TeamInfo;
import bean.ToRecommend;
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
import java.util.HashMap;

import static NLP.Constant.*;

@WebServlet(name = "RecommendServlet")
public class RecommendServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
//根据用户发布过的活动进行推荐类似活动，用短文本相似度算法
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        AipNlp client=new AipNlp(APP_ID, API_KEY, SECRET_KEY);
//        String text1 = "图书馆学习";
//        String text2 = "操场自习";

        // 传入可选参数调用接口
        HashMap<String, Object> options = new HashMap<>();
        options.put("model", "BOW");

        // 短文本相似度
//        JSONObject res = client.simnet(text1, text2, options);
//        System.out.println(res.toString(2));

        int useID=Integer.parseInt(request.getParameter("token"));

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

        String recommendResponseStr;
        ArrayList<ToRecommend> toRecommendArrayList=new ArrayList<>();//待推荐列表

        int cnt=0;//统计用户发布的活动数量 并只对用户最近发布的几个活动进行推荐
        Connection connection = DBUtil.getConnection();
        try {
            Statement statement1=connection.createStatement();
            Statement statement2=connection.createStatement();
            Statement statement3=connection.createStatement();
            String sqlQueryUser="select activity_title,activity_description from "+DBUtil.TABLE_ACTIVITY_INFO+" where activity_issuer_id="+useID+" order by activity_issue_time desc";
            ResultSet resultSet1=statement1.executeQuery(sqlQueryUser);//
            LogUtil.log(sqlQueryUser);
            while (resultSet1.next()){
                cnt++;
                String recommendReason1=resultSet1.getString(1)+resultSet1.getString(2);
//              将用户发布活动的标题和描述拼接为一个字符串
                String sqlQueryAll="select * from "+DBUtil.TABLE_ACTIVITY_INFO+" where activity_issuer_id <>"+useID;
                LogUtil.log(sqlQueryAll);
                ResultSet resultSet2=statement2.executeQuery(sqlQueryAll);
                while(resultSet2.next()){
                    String recommendReason2=resultSet2.getString(4)+resultSet2.getString(6);
                    //将活动信息表的标题和描述拼接为一个字符串
                    JSONObject similarityRes  = client.simnet(recommendReason1, recommendReason2, options);
                    LogUtil.log(similarityRes.toString(2));
                    double similarity=similarityRes.getDouble("score");
                    LogUtil.log("Reason1:"+recommendReason1+";Reason2:"+recommendReason2+";similarity:"+similarity);

                    activity_id=resultSet2.getInt(1);
                    int issuerID=resultSet2.getInt(3);
                    title=resultSet2.getString(4);
                    description=resultSet2.getString(6);
                    issueTime=resultSet2.getString(7);
                    startTime=resultSet2.getString(8);
                    place=resultSet2.getString(9);
                    maxNumber=resultSet2.getInt(10);
                    demand=resultSet2.getString(11);
                    joinedNumber=resultSet2.getInt(12);

                    String sqlGetUserInfo="select NickName,ContactMethod from "+DBUtil.TABLE_USERINFO+" where userID='"+issuerID+"'";
                    ResultSet resultSet3=statement3.executeQuery(sqlGetUserInfo);
                    if(resultSet3.next()){
                        issuerNickName=resultSet3.getString(1);
                        issuerContactMethod=resultSet3.getString(2);
                    }

                    TeamInfo teamInfo=new TeamInfo(activity_id, issuerNickName,title,description,issueTime,startTime,place,demand,issuerContactMethod, maxNumber,joinedNumber);
                    ToRecommend toRecommend=new ToRecommend(teamInfo,similarity);
                    toRecommendArrayList.add(toRecommend);
                }
                if(cnt==3)break;//定为只为用户最近发布的三个活动进行推荐
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        Collections.sort(toRecommendArrayList);//按相似度降序排序
        StringBuilder stringBuilder=new StringBuilder("[");
        if(cnt>0){
            int k=0;//只推荐有限个相关活动

            for(ToRecommend toRecommend:toRecommendArrayList){
                stringBuilder.append(toRecommend.getTeamInfo().toString());
                stringBuilder.append(",");
                LogUtil.log("Similarity:"+toRecommend.getSimilarity());
                k++;
                if(k==3){
                    break;//定为推荐三个
                }
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        stringBuilder.append("]");
        recommendResponseStr=stringBuilder.toString();
        LogUtil.log(recommendResponseStr);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().append(recommendResponseStr);
    }

}
