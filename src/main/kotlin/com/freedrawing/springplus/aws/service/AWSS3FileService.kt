package com.freedrawing.springplus.aws.service

import org.apache.tomcat.util.http.fileupload.FileUploadException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.IOException
import java.util.*


@Service
class AWSS3FileService(
    private val s3Client: S3Client,
    @Value("\${cloud.aws.s3.bucket}") private val bucket: String
) {

    fun uploadImage(file: MultipartFile): String {
        val fileName = createUniqueFileName(file.originalFilename)

        try {
            val request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(file.contentType)
                .build()

            // Upload file
            s3Client.putObject(
                request,
                RequestBody.fromInputStream(file.inputStream, file.size)
            )

            // Get url
            return s3Client.utilities().getUrl {
                it.bucket(bucket).key(fileName)
            }.toString()

        } catch (e: Exception) {
            throw FileUploadException("파일 업로드 중 오류가 발생했습니다: ${e.message}")
        }
    }

    private fun createUniqueFileName(originalFileName: String?): String {
        val uuid = UUID.randomUUID()
        val fileExtension = originalFileName?.substringAfterLast(".", "") // 원본의 파일 확장자 추출

        return if (fileExtension.isNullOrBlank()) {
            uuid.toString()
        } else {
            "${uuid}.${fileExtension}"
        }
    }

    fun deleteImage(fileUrl: String) {
        try {
            val fileName = fileUrl.substringAfter(".amazonaws.com/")
            val deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build()

            s3Client.deleteObject(deleteObjectRequest)
        } catch (e: Exception) {
            throw IOException("파일 삭제 중 오류가 발생했습니다: ${e.message}")
        }
    }
}