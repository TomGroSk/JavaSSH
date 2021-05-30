package pl.ssh.moviesservice.FileStorage;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.UUID;

@Component
public class BlobStorage {
    private BlobServiceClient blobServiceClient;

    public BlobStorage() {
        blobServiceClient = new BlobServiceClientBuilder().connectionString("DefaultEndpointsProtocol=https;AccountName=javasa;AccountKey=MDcvLrbLqeciw8nu7nXqaJeTyjtERUikZlx2ddPV+/x3+5G4Jkkk+5WWvbg5O5bLxO/y4QwZyO7mLiIsLsSrEw==;EndpointSuffix=core.windows.net").buildClient();
    }

    public String uploadFile(String fileName, InputStream stream, Integer size) {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient("movies");

        var split = fileName.split("\\.");
        String extension = split[split.length - 1];
        String newFileName = UUID.randomUUID() + "." + extension;
        BlobClient blobClient = containerClient.getBlobClient(newFileName);

        blobClient.upload(stream, size);
        return blobClient.getBlobUrl();
    }
}
