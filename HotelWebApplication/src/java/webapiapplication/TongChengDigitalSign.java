/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webapiapplication;

/**
 *
 * @author bianshujun
 */
public class TongChengDigitalSign {

    public static String getContent(String reqTime,String serviceName) {
        String[] originalArray = {
            "Version=" + "20111128102912",
            "AccountID=" + "4f4faac5-8fd8-4fe5-9df7-445a083afad3",
            "ServiceName=" + serviceName,
            "ReqTime=" + reqTime
        };
        String[] sortedArray = TongChengDigitalSign.BubbleSort(originalArray);
        String checkvalue = TongChengDigitalSign.GetMD5ByArray(sortedArray, "9bea39df317efb0d", "utf-8");
        System.out.println(checkvalue);
        return checkvalue;
    }

    public static String GetMD5ByArray(String[] sortedArray, String key, String inputCharset) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < sortedArray.length; i++) {
            if (i == sortedArray.length - 1) {
                builder.append(sortedArray[i]);
            } else {
                builder.append(sortedArray[i] + "&");
            }
        }
        builder.append(key); 
        return GetMD5(builder.toString(), inputCharset);
    }

    public static String GetMD5(String s) {
        return GetMD5(s, "utf-8");
    }

    public static String GetMD5(String input, String inputCharset) {
        String s = null;
        char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符 
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            java.security.MessageDigest md =
                    java.security.MessageDigest.getInstance("MD5");
            md.update(input.getBytes(inputCharset));
            byte tmp[] = md.digest();          // MD5 的计算结果是一个 128 位的长整数，     // 用字节表示就是 16 个字节 
            char str[] = new char[16 * 2];
            int k = 0;                                // 表示转换结果中对应的字符位置 
            for (int i = 0; i < 16; i++) {          // 从第一个字节开始，对 MD5 的每一个字节 // 转换成 16 进制字符的转换        
                byte byte0 = tmp[i];                 // 取第 i 个字节 
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];  // 取字节中高 4 位的数字转换,   // >>> 为逻辑右移，将符号位一起右移 
                str[k++] = hexDigits[byte0 & 0xf];            // 取字节中低 4 位的数字转换
            }
            s = new String(str);                                 // 换后的结果转换为字符串  
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String[] BubbleSort(String[] originalArray) {
        int i, j; //交换标志     
        String temp;
        Boolean exchange;
        for (i = 0; i < originalArray.length; i++) //最多做R.Length-1趟排序 
        {
            exchange = false; //本趟排序开始前，交换标志应为假  
            for (j = originalArray.length - 2; j >= i; j--) {
                if (originalArray[j + 1].compareTo(originalArray[j]) < 0)//交换条件        
                {
                    temp = originalArray[j + 1];
                    originalArray[j + 1] = originalArray[j];
                    originalArray[j] = temp;
                    exchange = true; //发生了交换，故将交换标志置为真    
                }
            }
            if (!exchange) //本趟排序未发生交换，提前终止算法     
            {
                break;
            }
        }
        return originalArray;
    }
}
