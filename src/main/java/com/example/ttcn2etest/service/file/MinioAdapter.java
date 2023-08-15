package com.example.ttcn2etest.service.file;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
@Slf4j
public class MinioAdapter {
    private final MinioClient minioClient;
    @Value("${minio.bucket.name}")
    String defaultBucketName;
    @Value("${minio.default.folder}")
    String defaultBaseFolder;

    public MinioAdapter(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public Flux<Bucket> getAllBuckets() {
        try {
            return Flux.fromIterable(minioClient.listBuckets()).subscribeOn(Schedulers.boundedElastic());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @SneakyThrows
    public Mono<com.example.ttcn2etest.response.UploadResponse> uploadFile(Mono<FilePart> file) {
        return file.subscribeOn(Schedulers.boundedElastic()).map(multipartFile -> {
            long startMillis = System.currentTimeMillis();
            File temp = new File(multipartFile.filename());
            temp.canWrite();
            temp.canRead();
            try {
                System.out.println(temp.getAbsolutePath());
                // blocking to complete io operation
                multipartFile.transferTo(temp).block();
                UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                        .bucket(defaultBucketName)
                        .object(defaultBaseFolder + "/" + multipartFile.filename())
                        .filename(temp.getAbsolutePath())
                        .build();
                ObjectWriteResponse response = minioClient.uploadObject(uploadObjectArgs);
                temp.delete();
                log.info("upload file execution time {} ms", System.currentTimeMillis() - startMillis);
                return com.example.ttcn2etest.response.UploadResponse.builder().bucket(response.bucket()).objectName(response.object()).build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).log();
    }
    public Mono<InputStreamResource> download(String name) {
        return Mono.fromCallable(() -> {
            InputStream response = minioClient.getObject(GetObjectArgs.builder().bucket(defaultBucketName).object(name).build());
            return new InputStreamResource(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
    public Mono<com.example.ttcn2etest.response.UploadResponse> putObject(FilePart file) {
        return file.content()
                .subscribeOn(Schedulers.boundedElastic())
                .reduce(new com.example.ttcn2etest.service.file.InputStreamCollector(),
                        (collector, dataBuffer) -> collector.collectInputStream(dataBuffer.asInputStream()))
                .map(inputStreamCollector -> {
                    long startMillis = System.currentTimeMillis();
                    try {
                        String randomName = UUID.randomUUID().toString();
                        System.out.println(file.headers().getContentType().toString());
                        PutObjectArgs args = PutObjectArgs.builder().object(defaultBaseFolder + "/" + randomName + ".png")
                                .contentType(file.headers().getContentType().toString())
                                .bucket(defaultBucketName)
                                .stream(inputStreamCollector.getStream(), inputStreamCollector.getStream().available(), -1)
                                .build();

                        ObjectWriteResponse response = minioClient.putObject(args);

                        int expiryTimeInSeconds = 7 * 24 * 60 * 60;
                        GetPresignedObjectUrlArgs urlArgs = GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket(response.bucket())
                                .object(response.object())
                                .expiry(expiryTimeInSeconds)
                                .build();

                        String urlImage = minioClient.getPresignedObjectUrl(urlArgs);
                        int questionMarkIndex = urlImage.indexOf('?');
                        if (questionMarkIndex != -1) {
                            urlImage = urlImage.substring(0, questionMarkIndex);
                        }

                        log.info("upload file execution time {} ms", System.currentTimeMillis() - startMillis);
                        return com.example.ttcn2etest.response.UploadResponse.builder().bucket(response.bucket()).objectName(response.object()).urlImage(urlImage).build();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).log();
    }



    public Mono<com.example.ttcn2etest.response.UploadResponse> uploadFile1(Mono<FilePart> file) {
        return file.flatMap(multipartFile -> {
            long startMillis = System.currentTimeMillis();
            File temp = new File(multipartFile.filename());
            temp.canWrite();
            temp.canRead();
            try {
                System.out.println(temp.getAbsolutePath());

                // Non-blocking transfer operation
                return multipartFile.transferTo(temp)
                        .then(Mono.defer(() -> {
                            UploadObjectArgs uploadObjectArgs = null;
                            try {
                                uploadObjectArgs = UploadObjectArgs.builder()
                                        .bucket(defaultBucketName)
                                        .object(defaultBaseFolder + "/" + multipartFile.filename())
                                        .filename(temp.getAbsolutePath())
                                        .build();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            UploadObjectArgs finalUploadObjectArgs = uploadObjectArgs;
                            return Mono.fromCallable(() -> minioClient.uploadObject(finalUploadObjectArgs))
                                    .map(response -> {
                                        temp.delete();
                                        log.info("upload file execution time {} ms", System.currentTimeMillis() - startMillis);
                                        return com.example.ttcn2etest.response.UploadResponse.builder()
                                                .bucket(response.bucket())
                                                .objectName(response.object())
                                                .build();
                                    });
                        }));
            } catch (Exception e) {
                return Mono.error(e);
            }
        }).log();
    }

}
