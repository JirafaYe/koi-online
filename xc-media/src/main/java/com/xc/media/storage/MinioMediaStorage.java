package com.xc.media.storage;


import com.xc.common.exceptions.CommonException;
import com.xc.common.utils.AssertUtils;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;

import static com.xc.media.constants.FileConstants.Msg.*;

@RequiredArgsConstructor
@Component
@Slf4j
public class MinioMediaStorage implements IFileStorage {

    @Value("${xc.minio.bucket.media}")
    private String bucketName;

    private final MinioClient minioClient;
    @Override
    public String uploadFile(String fileName, InputStream inputStream, long size, String contentType) {
        // 1.数据校验
        AssertUtils.isNotBlank(bucketName, BUCKET_NAME_IS_NULL);
        AssertUtils.isNotBlank(fileName, MEDIA_KEY_IS_NULL);
        AssertUtils.isNotNull(inputStream);
        try {
//             2.上传文件元数据处理
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(inputStream, size, -1)
                            .contentType(contentType)
                            .build()
            );
            return ENDPOINT + "/" + bucketName + "/" + fileName;
        } catch (Exception e) {
            log.error("上传视频[{}]失败 ", fileName, e);
            throw new CommonException("上传视频失败!", e);
        }
    }

    @Override
    public void deleteFile(String fileName) {
        // 1.数据校验
        AssertUtils.isNotBlank(bucketName, BUCKET_NAME_IS_NULL);
        AssertUtils.isNotBlank(fileName, MEDIA_KEY_IS_NULL);
        try {
            // 2.删除
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
        } catch (Exception e) {
            log.error("删除视频[{}]时发生异常：", fileName, e);
            throw new CommonException("删除异常。", e);
        }
    }
}
