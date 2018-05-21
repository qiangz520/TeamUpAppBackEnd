package bean;
//将活动信息与活动信息中的搜索分词出现计数包装成一个类，以便于排序
public class SearchMatch implements Comparable{

    private TeamInfo teamInfo;
    private int containCnt;

    @Override
    public int compareTo(Object o) {
        SearchMatch searchMatch=(SearchMatch)o;
        if(this.containCnt<searchMatch.containCnt){
            return 1;
        }
        else if(this.containCnt>searchMatch.containCnt) {
            return -1;//注意是-1
        }
        else return 0;
    }

    public SearchMatch(TeamInfo teamInfo,int containCnt){
        this.teamInfo=teamInfo;
        this.containCnt=containCnt;
    }
    public TeamInfo getTeamInfo() {
        return teamInfo;
    }

    public int getContainCnt() {
        return containCnt;
    }

}
