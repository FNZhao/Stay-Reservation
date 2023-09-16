package com.zhaofn.staybooking.service;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.zhaofn.staybooking.exception.GCSUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@Service
public class ImageStorageService {

    @Value("${gcs.bucket}")//spring boot的功能，去application.property里去找到这个值，也可以不用@Value这个然后private final来定义，坏处是多了几行，好处是定义后不会在其他地方被更改，更加安全
    private String bucketName;

    private final Storage storage;


    public ImageStorageService(Storage storage) {
        this.storage = storage;
    }


    public String save(MultipartFile file) throws GCSUploadException {
        String filename = UUID.randomUUID().toString();
        BlobInfo blobInfo = null;
        try {
            blobInfo = storage.createFrom(
                    BlobInfo
                            .newBuilder(bucketName, filename)
                            .setContentType("image/jpeg")
                            .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))))
                            .build(),
                    file.getInputStream());
        } catch (IOException exception) {
            throw new GCSUploadException("Failed to upload file to GCS");
        }


        return blobInfo.getMediaLink();
    }

}
