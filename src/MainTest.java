import java.util.*;

/**
 * Created by starSea_AB on 2017/9/14.
 */
public class MainTest {
    // websites domain names
    // static final String[] websites = {"www.163.com", "www.baidu.com", "www.souhu.com", "www.sina.com.cn", "www.taobao.com", "www.12306.cn"};
    static final String[] websites = {"www.163.com"};
    static int websitesNum = websites.length;
    static final int TRACERT_TIMES = 5;
    // compareResults 保存着比较的结果——有多少次路径的变化
    static int[] compareResults = new int[websitesNum];

    public static void main(String[] args) {
        for(int i = 0; i < websitesNum; i++) {
            // 创建一个元素为HashMap的List容器
            // 如果目的服务器IP出现不同，则将其作为一个新的路径，存放在List中
            ArrayList<HashMap<Integer,String>> tracertStandardList = new ArrayList<>();

            HashMap<Integer,String> tracertTmp = new HashMap<>();
            CommandTracert command = new CommandTracert();

            int count = 0;
            while(count < TRACERT_TIMES) {
                // tracertResults.add(command.command(websites[0]));
                // 每次获取一个tracert结果，然后与标准进行比较
                tracertTmp = command.command(websites[i]);
                mapPrint(tracertTmp);
                //System.out.println("Tracert Times: " + count);

                HashMap<Integer,String> tracertStandard = getIdenticalServerIP(tracertStandardList, tracertTmp);
                if(tracertStandard == null) {
                    // 说明list中所包含的目的服务器IP，与当前tracert结果的目的服务器IP都不相同，需要将该结果新添加进list中
                    tracertStandardList.add(tracertTmp);
                    System.out.println("Null and Add" + tracertStandardList.size());
                }
                else {
                    // 若包含tracert结果中的目的服务器，则需要通过逐一路由节点的比较，判断是否路径相同
                    boolean judgeResult = hasIdenticalRoutingPath(tracertStandardList,tracertStandard,tracertTmp);
                    if(judgeResult) { // 对于路径不相同的情况，进行计数
                        count++;
                    }
                    else {
                        count++;
                        compareResults[i]++;
                    }
                }
            }
        }
        System.out.println(compareResults[0]);
        BarChart chart = new BarChart(websites,compareResults);
        chart.getChartPanel();
    }

    public static void mapPrint(Map<Integer,String> map) {
        Set<Map.Entry<Integer,String>> entry = map.entrySet();
        //System.out.println(map.size());
        for(Map.Entry<Integer,String> e : entry) {
            System.out.print("Key:" + e.getKey() + "-Value:" + e.getValue());
        }
    }

    public static HashMap<Integer,String> getIdenticalServerIP(ArrayList<HashMap<Integer,String>> standardList, HashMap<Integer,String> tmpMap) {
        if(standardList.isEmpty()) return null;
        else {
            for(HashMap<Integer,String> standardMap : standardList) {
                if(standardMap.get(2).equals(tmpMap.get(2)))
                    return standardMap;
            }
            return null;
        }
    }

    public static boolean hasIdenticalRoutingPath(ArrayList<HashMap<Integer,String>> standardList,HashMap<Integer,String> standardMap, HashMap<Integer,String> tmpMap) {
        Iterator iter = tmpMap.entrySet().iterator();
        //System.out.println("hasIdenticalRoutingPath?");
        while(iter.hasNext()) {
            Map.Entry<Integer,String> entry = (Map.Entry<Integer,String>) iter.next();
            Integer key = entry.getKey();
            //System.out.println("Key: " + key);
            if(standardMap.containsKey(key)) {
                // 若standardMap有相应的key，那么就判断key对应的value是否相等
                if(standardMap.get(key).equals(entry.getValue())) {
                    //System.out.println("The Same and Continue");
                    continue;
                }
                else {
                    //System.out.println("Not The Same and False");
                    return false;
                }

            }
            else {
                // 否则，就将当前tmpMap中key-value添加到standardMap中
                // 由于standardMap没有相应的key-value（可能是中间节点防ping导致）
                // 所以，后续出现新的key-value（中间节点可能又可ping了），不认为是路径不一样
                standardList.remove(standardMap);
                System.out.println("After Remove: " + standardList.size());
                standardMap.put(key,entry.getValue());
                standardList.add(standardMap);
                System.out.println("After Add: " + standardList.size());
            }
        }
        return true;
    }
}
