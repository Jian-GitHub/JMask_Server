package com.jian;

import com.jian.entity.CompareAppWeb;
import com.jian.entity.JMaskAppInfo;
import com.jian.entity.Result;
import com.jian.mapper.JMask_Mapper;
import com.jian.mapper.User_Mapper;
import net.coobird.thumbnailator.Thumbnails;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Jian Qi
 * @Date 2022/3/27 11:27 下午
 * @Description
 * @Version
 */
@SpringBootTest
public class MYTEST {
    @Resource
    private User_Mapper user_mapper;
    @Resource
    private JMask_Mapper compareAppWeb_mapper;

    @Test
    public void test(){
//        System.out.println(File.separator);
//        String oldImg = "/Users/qi/Downloads/IMG_0626.HEIC";
        String oldImg = "/Users/qi/Downloads/52.jpg";
//        String newImg = "/Users/qi/Downloads/IMG_0626.jpg";
        String newImg = "/Users/qi/Downloads/52.png";
        ConvertCmd cmd = new ConvertCmd();
        IMOperation op = new IMOperation();
        op.addImage(oldImg);
        op.addImage(newImg);
        try {
            cmd.run(op);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IM4JavaException e) {
            e.printStackTrace();
        }


//        try {
//            File file = new File("/Users/qi/Downloads/IMG_0626.HEIC");
////            int length = (int) file.length();
//            byte[] data = Files.readAllBytes(file.toPath());
////            System.out.println(data.length);
//            BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
//            Thumbnails.of(image).scale(1).outputFormat("jpg").toFile("/Users/qi/Downloads/IMG_0626.jpg");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        System.out.println(System.currentTimeMillis());
//        for (CompareAppWeb compareAppWeb : compareAppWeb_mapper.selectCompareAppWebList()) {
//            System.out.println(compareAppWeb.toString());
//        }
//        System.out.println(compareAppWeb_mapper.selectCompareAppWebList());
//        System.out.println(compareAppWeb_mapper.selectJMaskAppInfo().toString());
//        JMaskAppInfo jMaskAppInfo = compareAppWeb_mapper.selectJMaskAppInfo();
////        System.out.println(jMaskAppInfo.getDate().split(" ")[0]);
//
//        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy年M月d日");
//        SimpleDateFormat timeFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = null;
//        try {
//            date = timeFormat1.parse(jMaskAppInfo.getDate());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        String dateStr = timeFormat.format(date);
//        System.out.println(dateStr);
//        jMaskAppInfo.setDate(jMaskAppInfo.getDate().split(" ")[0]);
//        System.out.println(user_mapper.countUserLog("1f0af08b05e04220acba0abc7d2a2872"));
    }
}
