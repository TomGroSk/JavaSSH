package pl.ssh.java.Service;

import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

@Service
public class BookService {
    public InputStream convertFileContentToStream(String fileContent){
        //var byteArray = fileContent.getBytes();
        //String encoded = Base64.getEncoder().encodeToString(byteArray);
        byte[] decoded = Base64.getDecoder().decode(fileContent);
        return new ByteArrayInputStream(decoded);
    }
}
