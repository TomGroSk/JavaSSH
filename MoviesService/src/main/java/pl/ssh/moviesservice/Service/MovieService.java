package pl.ssh.moviesservice.Service;

import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

@Service
public class MovieService {
    public InputStream convertFileContentToStream(String fileContent) {
        byte[] decoded = Base64.getDecoder().decode(fileContent);
        return new ByteArrayInputStream(decoded);
    }
}
