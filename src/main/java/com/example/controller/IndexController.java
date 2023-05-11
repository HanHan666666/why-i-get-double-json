package com.example.controller;

import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import com.example.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
public class IndexController {
    @Autowired
    private Producer producer;

    @GetMapping("/captcha")
    public Result captcha() throws IOException {
        //产生验证码字符
        //generate captcha characters
        String code = producer.createText();
        //产生一个uuid（一串随机的字符）作为key，b7af2ad9-4be8-49dc-b0fa9b4707ddd7b3。
        //generate a uuid (a string of random characters) as the key, b7af2ad9-4be8-49dc-b0fa9b4707ddd7b3.
        //产生验证码，存储到redis数据库，uuid作为存储的key, 验证码就是这个key对应存储的value
        //generate a captcha, store it in the redis database, uuid as the storage key, and the captcha is the value corresponding to the storage key
        String key = UUID.randomUUID().toString();
        //将产生验证码 转为图片 image就是内存中一个图片对象
        //convert the generated captcha to an image, image is an image object in memory
        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", out);
        //Base64转码
        //Base64 encoding
        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";
        String base64Image = str + encoder.encode(out.toByteArray());
        //在服务器端输出日志，产生验证码以及存储redis key随机码是多少
        //Output the log on the server side, what is the captcha and the random code of the storage redis key
        log.info("验证码--{}--{}", key, code);
//        error no .build()
        return Result.success(
                MapUtil.builder().put("key", key).put("captchaImg", base64Image));
        // success
//                return Result.success(
//                MapUtil.builder().put("key", key).put("captchaImg", base64Image).build());
        //return Result.success(MakeMistake());
    }

    private String MakeMistake() {
        throw new NullPointerException("this is null");
//        return "mistake";
    }
}
