package com.xc.media.controller;


import com.xc.common.domain.dto.PageDTO;
import com.xc.media.domain.dto.FileDTO;
import com.xc.media.domain.query.FileMediaQuery;
import com.xc.media.domain.vo.FileVO;
import com.xc.media.service.IFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 *  图片上传等
 *
 * @author Koi
 * @since 2024-05-12
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {

    private final IFileService fileService;

    /**
     * 分页查询图片
     * @param query
     * @return
     */
    @GetMapping("/list")
    public PageDTO<FileVO> queryFilePage(FileMediaQuery query){
        return fileService.queryFilePage(query);
    }

    /**
     * 上传图片
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public FileDTO uploadFile(@RequestParam("file") MultipartFile file){
        return fileService.uploadFile(file);
    }

    /**
     * 获取图片信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public FileDTO getFileInfo(@PathVariable("id") Long id){
        return fileService.getFileInfo(id);
    }

    /**
     * 删除图片
     * @param id
     */
    @DeleteMapping("/{id}")
    public void deleteFileById(@PathVariable("id") Long id){
        fileService.deleteFileById(id);
    }

    /**
     * 批量删除图片
     * @param ids
     */
    @DeleteMapping("/fileIds")
    public void deleteFileByIds(@RequestParam("ids") List<Long> ids){
        fileService.deleteFileByIds(ids);
    }

    /**
     * 暴露给其他服务调用，用于返回图片的信息
     * @param ids
     * @return
     */
    @GetMapping("/ids")
    public List<FileDTO> getFileInfos(List<Long> ids){
        return fileService.getFileInfos(ids);
    }
}
