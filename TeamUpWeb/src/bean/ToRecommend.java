package bean;
//作为短文本相似度算法用于排序的活动信息待推荐类
public class ToRecommend implements Comparable {
    private TeamInfo teamInfo;
    private double similarity;

    public ToRecommend(TeamInfo teamInfo, double similarity) {
        this.teamInfo = teamInfo;
        this.similarity = similarity;
    }

    @Override
    public int compareTo(Object o) {
        ToRecommend toRecommend= (ToRecommend)o;
        if(this.similarity<toRecommend.similarity){
            return 1;
        }
        else if(this.similarity>toRecommend.similarity){
            return -1;
        }
        else return 0;
    }

    public TeamInfo getTeamInfo() {
        return teamInfo;
    }

    public void setTeamInfo(TeamInfo teamInfo) {
        this.teamInfo = teamInfo;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }


}
