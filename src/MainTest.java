import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by starSea_AB on 2017/9/14.
 */
public class MainTest {
    // websites domain names
    public final static String[] websites = {"www.163.com", "www.baidu.com", "www.souhu.com", "www.sina.com.cn", "www.taobao.com", "www.12306.cn"};
    //public final static String[] websites = {"www.163.com","www.baidu.com"};
    static int websitesNum = websites.length;
    // compareResults 保存着比较的结果——有多少次路径的变化
    public static int[] compareResults = new int[websitesNum];
    public final static int TRACERT_TIMES = 100;

    public static void main(String[] args) {
        // 创建线程数量与域名数量一致的线程池，tracert每一个域名使用一个线程
        ExecutorService executor = Executors.newFixedThreadPool(websitesNum);
        for(int i = 0; i < websitesNum; i++) {
            Runnable thread = new WorkThread(i);
            executor.execute(thread);
        }
        executor.shutdown();
        while(!executor.isTerminated()) {}
        System.out.println("All the threads have been finished!");

        BarChart chart = new BarChart(websites,compareResults,TRACERT_TIMES);
        chart.getChartPanel();
    }

    public static void mapPrint(Map<Integer,String> map) {
        Set<Map.Entry<Integer,String>> entry = map.entrySet();
        //System.out.println(map.size());
        for(Map.Entry<Integer,String> e : entry) {
            System.out.print("Key:" + e.getKey() + "-Value:" + e.getValue());
        }
        System.out.println();
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
