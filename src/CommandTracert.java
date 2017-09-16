import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by starSea_AB on 2017/9/14.
 */
public class CommandTracert {
    // 将返回一次tracert查询的结果
    // 返回形式为Map：Key为返回结果的行数，Value为每一行的最后一个IP（如果没有匹配到IP，则返回空串）
    public HashMap<Integer,String> command(String website) {
        System.out.println("The website/IP you want to tracert is: " + website);
        //CommandTracert command = new CommandTracert();
        String tracert = "tracert" + " " + website;
        HashMap<Integer,String> responseMap = new HashMap<Integer,String>();
        try {
            System.out.println("Tracert is working......");
            Process process = Runtime.getRuntime().exec(tracert);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String response = null;
            String regex = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})";
            Pattern pattern = Pattern.compile(regex);
            int nums = 0;
            while((response = bufferedReader.readLine()) != null) {
                Matcher matcher = pattern.matcher(response);
                while(matcher.find()) {
                    responseMap.put(nums, matcher.group(1));
                    // 有可能这一行不包含IP，所以对应的nums没有添加为map的Key
                }
                nums++;
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return responseMap;
    }
}
