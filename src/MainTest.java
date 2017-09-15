import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by starSea_AB on 2017/9/14.
 */
public class MainTest {
    // websites domain names
    // static final String[] websites = {"www.163.com", "www.baidu.com", "www.souhu.com", "www.sina.com.cn", "www.taobao.com", "www.12306.cn"};
    static final String[] websites = {"www.163.com"};
    static int websitesNum = websites.length;
    // compareResults 保存着比较的结果——有多少次路径的变化
    static int[] compareResults = new int[websitesNum];
    // ArrayList作为HashMap的容器，每一个元素都是HashMap
    // ArrayList<HashMap<Integer,String>> tracertResults = new ArrayList<>();
    static final int TRACERT_TIMES = 5;

    public static void main(String[] args) {
        for(int i = 0; i < websitesNum; i++) {
            // 创建一个元素为HashMap的List容器
            // 如果目的服务器IP出现不同，则将其作为一个新的路径，存放在List中
            //ArrayList<HashMap<Integer,String>> tracertStandardList = new ArrayList<>();
            Map<Integer,String> tracertStandard = new HashMap<>();
            Map<Integer,String> tracertTmp = new HashMap<>();
            CommandTracert command = new CommandTracert();
            // tracertStandard 作为第一次获取的路由路径，用于后续比较的标准
            tracertStandard = command.command(websites[i]);
            mapPrint(tracertStandard);

            int count = 0;
            // int changePoint = 0;
            while(count < TRACERT_TIMES) {
                // tracertResults.add(command.command(websites[0]));
                // 每次获取一个tracert结果，然后与标准进行比较
                tracertTmp = command.command(websites[i]);
                mapPrint(tracertTmp);

                // 只比较目的服务器IP相同情况下，中间路径变化的情况，所以需要先判断目的服务器IP是否相同
                // 根据tracert命令的输出格式，目的服务器IP会第一次出现在HashMap Key = 2 的 Value 中
                if(!tracertStandard.get(2).equals(tracertTmp.get(2)))
                    continue;

                for(int k = 0; k < tracertTmp.size(); k++) {
                    if(tracertTmp.containsKey(k)) {
                        if(tracertStandard.containsKey(k)) {
                            if(!tracertStandard.get(k).equals(tracertTmp.get(k)))
                                compareResults[i]++;
                        }
                        else {
                            tracertStandard.put(k,tracertTmp.get(k));
                        }
                    }
                }
                count++;
            }
        }
        System.out.println(compareResults[0]);
        BarChart chart = new BarChart(websites,compareResults);
        chart.getChartPanel();
    }

    public static void mapPrint(Map<Integer,String> map) {
        Set<Map.Entry<Integer,String>> entry = map.entrySet();
        System.out.println(map.size());
        for(Map.Entry<Integer,String> e : entry) {
            System.out.print("Key:" + e.getKey() + "-Value:" + e.getValue());
        }
    }
}
