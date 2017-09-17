import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by starSea_AB on 2017/9/17.
 */
public class WorkThread implements Runnable{
    private String website;
    private int ith;


    public WorkThread(int ith) {
        website = MainTest.websites[ith];
        this.ith = ith;
    }

    public void run() {
        // 创建一个元素为HashMap的List容器
        // 如果目的服务器IP出现不同，则将其作为一个新的路径，存放在List中
        ArrayList<HashMap<Integer,String>> tracertStandardList = new ArrayList<>();
        HashMap<Integer,String> tracertTmp = new HashMap<>();
        CommandTracert command = new CommandTracert();

        int count = 0;
        while(count < MainTest.TRACERT_TIMES) {
            // 每次获取一个tracert结果，然后与标准进行比较
            tracertTmp = command.command(website);
            System.out.println(website + ": ");
            MainTest.mapPrint(tracertTmp);
            //System.out.println("Tracert Times: " + count);

            HashMap<Integer,String> tracertStandard = MainTest.getIdenticalServerIP(tracertStandardList, tracertTmp);
            if(tracertStandard == null) {
                // 说明list中所包含的目的服务器IP，与当前tracert结果的目的服务器IP都不相同，需要将该结果新添加进list中
                tracertStandardList.add(tracertTmp);
                System.out.println("Null and Add" + tracertStandardList.size());
            }
            else {
                // 若包含tracert结果中的目的服务器，则需要通过逐一路由节点的比较，判断是否路径相同
                boolean judgeResult = MainTest.hasIdenticalRoutingPath(tracertStandardList,tracertStandard,tracertTmp);
                if(judgeResult) { // 对于路径不相同的情况，进行计数
                    count++;
                }
                else {
                    count++;
                    MainTest.compareResults[ith]++;
                }
            }
        }
    }
}
