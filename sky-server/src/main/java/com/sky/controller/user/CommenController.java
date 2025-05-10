//package com.sky.controller.user;
//
//import com.sky.result.Result;
//import com.sky.utils.HuaWeiObsUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//import java.io.IOException;
//import java.util.UUID;
//import lombok.extern.slf4j.Slf4j;
//
//@RestController
//@RequestMapping("/common")
//@Slf4j
//public class CommenController {
//
//    @Autowired
//    private HuaWeiObsUtil huaWeiObsUtil;
//
//    /**
//     * 上传图片
//     *
//     * @param file
//     * @return
//     */
//    @PostMapping("/upload")
//    public Result<String> upload(MultipartFile file) {
//        log.info("上传图片：{}", file);
//        String originalFilename = file.getOriginalFilename();
//        String suffix = ".jpg";
//        if (originalFilename != null) {
//            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
//        }
//        String fileName = UUID.randomUUID().toString() + suffix;
//        try {
//            byte[] bytes = file.getBytes();
//            String imgUrl = huaWeiObsUtil.upload(bytes, fileName);
//            return Result.success(imgUrl);
//        } catch (IOException e) {
//            log.error("上传图片失败：{}", e);
//            return Result.error("上传图片失败");
//        }
//    }
//}
