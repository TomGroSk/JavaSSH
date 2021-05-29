package pl.ssh.java.FileStorage;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.UUID;

@Component
public class BlobStorage {
    private BlobServiceClient blobServiceClient;

    public BlobStorage(Environment env) {
        blobServiceClient = new BlobServiceClientBuilder().connectionString("").buildClient();
    }

    public String uploadFile(String fileName, InputStream stream, Integer size){
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient("books");

        var split = fileName.split("\\.");
        String extension = split[split.length - 1];
        String newFileName = UUID.randomUUID() + "." + extension;
        BlobClient blobClient = containerClient.getBlobClient(newFileName);

        blobClient.upload(stream, size);
        return blobClient.getBlobUrl();
    }
}
