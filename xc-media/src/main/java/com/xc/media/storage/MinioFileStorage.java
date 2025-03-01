package com.xc.media.storage;

import com.xc.common.exceptions.CommonException;
import com.xc.common.utils.AssertUtils;
import com.xc.common.utils.BeanUtils;
import com.xc.common.utils.CollUtils;
import io.minio.*;
import io.minio.messages.DeleteObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

import static com.xc.media.constants.FileConstants.Msg.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class MinioFileStorage implements IFileStorage{

    @Value("${xc.minio.bucket.files}")
    private String bucketName;
    
    private final MinioClient minioClient;
    

    @Override
    public String uploadFile(String fileName, InputStream inputStream, long size, String contentType) {
        // 1.数据校验
        AssertUtils.isNotBlank(bucketName, BUCKET_NAME_IS_NULL);
        AssertUtils.isNotBlank(fileName, FILE_KEY_IS_NULL);
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
            log.error("上传文件[{}]失败 ", fileName, e);
            throw new CommonException("上传文件失败!", e);
        }
    }

    @Override
    public void deleteFile(String fileName) {
        // 1.数据校验
        AssertUtils.isNotBlank(bucketName, BUCKET_NAME_IS_NULL);
        AssertUtils.isNotBlank(fileName, FILE_KEY_IS_NULL);
        try {
            // 2.删除
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
        } catch (Exception e) {
            log.error("删除文件[{}]时发生异常：", fileName, e);
            throw new CommonException("删除异常。", e);
        }
    }

    @Override
    public void deleteFiles(List<String> files) {
        // 1.数据校验
        AssertUtils.isNotBlank(bucketName, BUCKET_NAME_IS_NULL);
        if (CollUtils.isEmpty(files)){
            return;
        }
        try {
            List<DeleteObject> list = BeanUtils.copyList(files, DeleteObject.class);
            minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucketName).objects(list).build());
        }  catch (Exception e) {
            log.error("{}清理图片时发生异常：", LocalDateTime.now(), e);
            throw new CommonException("删除异常。", e);
        }
    }
}
