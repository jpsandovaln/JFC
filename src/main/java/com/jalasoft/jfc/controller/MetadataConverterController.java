/*
 * Copyright (c) 2019 Jalasoft.
 *
 * This software is the confidential and proprietary information of Jalasoft.
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Jalasoft.
 */

package com.jalasoft.jfc.controller;

import com.jalasoft.jfc.model.Param;
import com.jalasoft.jfc.model.entity.FileEntity;
import com.jalasoft.jfc.model.exception.ConvertException;
import com.jalasoft.jfc.model.exception.Md5Exception;
import com.jalasoft.jfc.model.exception.ZipJfcException;
import com.jalasoft.jfc.model.metadata.MetadataConverter;
import com.jalasoft.jfc.model.repository.FileRepository;
import com.jalasoft.jfc.model.result.ErrorResponse;
import com.jalasoft.jfc.model.result.FileResponse;
import com.jalasoft.jfc.model.result.MessageResponse;
import com.jalasoft.jfc.model.result.Response;
import com.jalasoft.jfc.model.utility.FileServiceController;
import com.jalasoft.jfc.model.utility.FolderRemover;
import com.jalasoft.jfc.model.utility.LinkGenerator;
import com.jalasoft.jfc.model.utility.Md5Checksum;
import com.jalasoft.jfc.model.utility.PathJfc;
import com.jalasoft.jfc.model.utility.ZipFolder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.io.File;

/**
 * Extracts metadata of an uploaded file.
 *
 * @version 0.1 08 Jan 2020.
 *
 * @author Juan Martinez.
 */
@Api(value = "MetadataConverterController", description = "REST API related to Metadata converter")
@RestController
@RequestMapping("/api/v1")
public class MetadataConverterController {

    // Inject FileRepository.
    @Autowired
    FileRepository fileRepository;

    /**
     * Generates metadata from multipart file.
     * @param file is multipart value.
     * @param md5 is md5 value.
     * @param request is client request value.
     * @return ResponseEntity<Response> for status code.
     */
    @PostMapping("/metadataConverter")
    @ApiOperation(value = "File", notes = "Provides values for converting metadata",
            response = Response.class, authorizations = { @Authorization(value="JWT") })
    public ResponseEntity<Response> metadataConverter(
            @RequestParam("file") MultipartFile file, @RequestParam String md5, HttpServletRequest request) throws Md5Exception {
        FileResponse fileResponse;
        ErrorResponse errorResponse = new ErrorResponse();
        Param param = new Param();
        String fileUploaded;

        try {
            FileEntity fileEntity = new FileEntity();
            String cleanMd5 = md5.trim();

            if (fileRepository.findByMd5(cleanMd5) != null) {
                param.setInputPathFile(fileRepository.findByMd5(cleanMd5).getFilePath());
            } else {
                fileUploaded = FileServiceController.writeFile(PathJfc.getInputFilePath() +
                        file.getOriginalFilename(), file);
                cleanMd5 = Md5Checksum.getMd5(fileUploaded, cleanMd5);
                param.setInputPathFile(fileUploaded);
                fileEntity.setFilePath(fileUploaded);
                fileEntity.setMd5(cleanMd5);
                fileRepository.save(fileEntity);
            }

            param.setMd5(cleanMd5);
            param.setInputPathFile(PathJfc.getInputFilePath());
            param.setOutputPathFile(PathJfc.getOutputFilePath());
            param.setFolderName(cleanMd5);

            MetadataConverter metadataConverter = new MetadataConverter();
            fileResponse = metadataConverter.convert(param);

            String zipFilePath = zipFile(param);
            FolderRemover.removeFolder(param.getOutputPathFile() + param.getFolderName());

            LinkGenerator linkGenerator = new LinkGenerator();
            fileResponse.setDownload(linkGenerator.linkGenerator(zipFilePath, request));
            fileResponse.setName(param.getFolderName());
            fileResponse.setStatus(MessageResponse.SUCCESS200.getMessageResponse());

            return new ResponseEntity<>(fileResponse, HttpStatus.CREATED);
        } catch (ConvertException | ZipJfcException | Md5Exception ioe) {
            errorResponse.setName(this.getClass().getName());
            errorResponse.setError(ioe.getMessage());
            errorResponse.setStatus(MessageResponse.ERROR406.getMessageResponse());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception ioe) {
            errorResponse.setName(this.getClass().getName());
            errorResponse.setError(ioe.getMessage());
            errorResponse.setStatus(MessageResponse.ERROR404.getMessageResponse());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Allows to zip Param folder generated.
     * @param param object.
     * @return String value.
     * @throws ZipJfcException when something was wrong in the zip.
     */
    private String zipFile(Param param) throws ZipJfcException {

        // Assigns the zip's Path.
        String zipPath;

        // Constant slash variable.
        final String SLASH = "/";

        // Constant zip extension variable.
        final String ZIP_TAG = ".zip";

        ZipFolder zipFolder = new ZipFolder();

        File [] files = new File(param.getOutputPathFile() + SLASH + param.getFolderName() +
        SLASH).listFiles();

        File fileZip = new File(PathJfc.getPublicFilePath() + param.getFolderName() + ZIP_TAG);
        zipPath = fileZip.getAbsolutePath();

        zipFolder.zipFolderFile(files, fileZip);
        return zipPath;
    }
}
