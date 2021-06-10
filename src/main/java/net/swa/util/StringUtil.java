
package net.swa.util;

import java.util.UUID;

import org.apache.commons.lang.math.RandomUtils;

/**
 * 字符串工具
 * @author 陈晓伟
 *
 */
public class StringUtil
{
    private static String numberAndLetter = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 随机生成指定个数的字符串，字符串从a-z A-Z
     * @param len 生成的字符串长度
     * @return 随机生成的字符串
     */
    public static String getRandomString(int len)
    {
        char[] chars = numberAndLetter.toCharArray();
        String str = "";
        for (int i = 0; i < len; i++)
        {
            char c = chars[RandomUtils.nextInt(numberAndLetter.length())];
            str += String.valueOf(c);
        }
        return str;
    }

    /**检查字符串中有特殊字符***/
    public static boolean checkStr(String str)
    {
        for (int i = 0; i < str.length(); i++)
        {
            int c = str.codePointAt(i);
            if (c < 0x0000 || c > 0xffff)
            {
                return false;
            }
        }
        return true;
    }

    /**检查字符串中有特殊字符***/
    public static String filterStr(String str)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++)
        {
            int c = str.codePointAt(i);
            if (c < 0x0000 || c > 0xffff)
            {
                //do nothing
            }
            else
            {
                sb.append(Character.toChars(c));
            }
        }
        return sb.toString();
    }

    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

    public static String generateShortUuid()
    {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++)
        {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }

}
