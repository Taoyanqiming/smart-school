//package com.sky.utils;
//
//import com.obs.services.ObsClient;
//import com.obs.services.exception.ObsException;
//import com.obs.services.model.PutObjectRequest;
//import com.sky.properties.HuaWeiObsProperties;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import java.io.ByteArrayInputStream;
//
//@Data
//@AllArgsConstructor
//@Slf4j
//public class HuaWeiObsUtil {
//
//    private HuaWeiObsProperties huaWeiObsProperties;
//
//    /**
//     * 文件上传
//     *
//     * @param bytes
//     * @param objectName
//     * @return
//     */
//    public String upload(byte[] bytes, String objectName) {
//        ObsClient obsClient = new ObsClient(huaWeiObsProperties.getAccessKeyId(), huaWeiObsProperties.getAccessKeySecret(), huaWeiObsProperties.getEndpoint());
//        try {
//            PutObjectRequest request = new PutObjectRequest(huaWeiObsProperties.getBucketName(), objectName, new ByteArrayInputStream(bytes));
//            obsClient.putObject(request);
//        } catch (ObsException e) {
//            log.error("上传文件到华为云 OBS 失败：{}", e);
//        } finally {
//            try {
//                obsClient.close();
//            } catch (Exception e) {
//                log.error("关闭华为云 OBS 客户端失败：{}", e);
//            }
//        }
//
//        // 文件访问路径规则 https://BucketName.Endpoint/ObjectName
//        StringBuilder stringBuilder = new StringBuilder("https://");
//        stringBuilder
//                .append(huaWeiObsProperties.getBucketName())
//                .append(".")
//                .append(huaWeiObsProperties.getEndpoint())
//                .append("/")
//                .append(objectName);
//
//        log.info("文件上传到:{}", stringBuilder.toString());
//
//        return stringBuilder.toString();
//    }
//}