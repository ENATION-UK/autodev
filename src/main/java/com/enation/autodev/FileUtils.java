package com.enation.autodev;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/

public class FileUtils {

    /**
     * 读取文件内容
     * @param fileName 相对于resources下的路径
     * @return 文件内容
     */
    public static String readFile(String fileName) {
        // 使用类加载器获取文件输入流
        InputStream is = FileUtils.class.getResourceAsStream(fileName);
        if (is == null) {
            System.out.println("文件未找到: " + fileName);
            return null;
        }

        // 使用StringBuilder来存储文件内容
        StringBuilder content = new StringBuilder();

        // 使用BufferedReader读取文件内容
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }

    /**
     * json提取
     * @param str
     * like this:
     * 根据您的描述，我已经将需求分析梳理为以下的json格式：
     *
     * ```json
     * {
     * this is json content
     * }
     * ```
     * @return
     */
    public static String jsonExtract(String str) throws AIResAnalyzingException {
        str = str.replaceAll("```json", "```");
        str = str.replaceAll("```sql", "```");
        String pattern = "```\\n(.*?)```";

        Pattern r = Pattern.compile(pattern, Pattern.DOTALL);
        Matcher m = r.matcher(str);
        if (m.find()) {
          return m.group(1);
        } else {
            throw  new AIResAnalyzingException("Json Extract error",str);
        }
    }


    public static String  convertBase64(String filePath) {
        File file = new File(filePath); // 你的图片文件路径
        try {
            // 读取图片到 BufferedImage
            BufferedImage bufferedImage = ImageIO.read(file);

            // 创建一个字节数组输出流，用于输出转换后的字节
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream); // "jpg"是你的图片文件的格式

            // 使用 Base64 编码字节数组为字符串
            byte[] bytes = byteArrayOutputStream.toByteArray();
            String encodedString = Base64.getEncoder().encodeToString(bytes);

            return (encodedString); // 打印转换后的base64字符串
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
