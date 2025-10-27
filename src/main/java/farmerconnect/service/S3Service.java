package farmerconnect.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${application.bucket.name}")
    private String bucketName;

    // Upload a file under dynamic folder
    public String uploadFile(MultipartFile file, String folderName, Integer entityId) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Folder structure: folderName/entityId/
        String key = folderName + "/" + entityId + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build(); // no ACL needed if bucket disables ACLs

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        return "https://" + bucketName + ".s3.amazonaws.com/" + key;
    }

    // Delete single file (same as before)
    public void deleteFile(String fileUrl) {
        String key = fileUrl.replace("https://" + bucketName + ".s3.amazonaws.com/", "");
        s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(key).build());
    }

    // Delete all files under a dynamic folder/entity
    public void deleteAllFiles(String folderName, Long entityId) {
        String prefix = folderName + "/" + entityId + "/";

        ListObjectsV2Request listReq = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .build();

        ListObjectsV2Response listRes = s3Client.listObjectsV2(listReq);

        List<ObjectIdentifier> toDelete = listRes.contents().stream()
                .map(obj -> ObjectIdentifier.builder().key(obj.key()).build())
                .toList();

        if (!toDelete.isEmpty()) {
            DeleteObjectsRequest delReq = DeleteObjectsRequest.builder()
                    .bucket(bucketName)
                    .delete(Delete.builder().objects(toDelete).build())
                    .build();
            s3Client.deleteObjects(delReq);
        }
    }

}

