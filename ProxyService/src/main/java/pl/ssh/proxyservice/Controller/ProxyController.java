package pl.ssh.proxyservice.Controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.ssh.proxyservice.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;

@RestController
public class ProxyController {

    @RequestMapping("/books/**")
    public ResponseEntity mirrorRestBooks(@RequestBody(required = false) String body,
                                          HttpMethod method, HttpServletRequest request, HttpServletResponse response)
            throws URISyntaxException {

        return processRequest(body, method, request, response, Configuration.Books.Address, Configuration.Books.Port);
    }

    @RequestMapping("/movies/**")
    public ResponseEntity mirrorRestMovies(@RequestBody(required = false) String body,
                                          HttpMethod method, HttpServletRequest request, HttpServletResponse response)
            throws URISyntaxException {

        return processRequest(body, method, request, response, Configuration.Movies.Address, Configuration.Movies.Port);
    }

    @RequestMapping("/games/**")
    public ResponseEntity mirrorRestGames(@RequestBody(required = false) String body,
                                          HttpMethod method, HttpServletRequest request, HttpServletResponse response)
            throws URISyntaxException {

        return processRequest(body, method, request, response, Configuration.Games.Address, Configuration.Games.Port);
    }

    @RequestMapping("/comments/**")
    public ResponseEntity mirrorRestComments(@RequestBody(required = false) String body,
                                          HttpMethod method, HttpServletRequest request, HttpServletResponse response)
            throws URISyntaxException {

        return processRequest(body, method, request, response, Configuration.Comments.Address, Configuration.Comments.Port);
    }

    private ResponseEntity processRequest(String body,
                                          HttpMethod method, HttpServletRequest request, HttpServletResponse response,
                                          String host, int port)
            throws URISyntaxException {
        String requestUrl = request.getRequestURI();

        URI uri = new URI("http", null, host, port, null, null, null);
        uri = UriComponentsBuilder.fromUri(uri)
                .path(requestUrl)
                .query(request.getQueryString())
                .build(true).toUri();

        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.set(headerName, request.getHeader(headerName));
        }

        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.exchange(uri, method, httpEntity, String.class);
        } catch(HttpStatusCodeException e) {
            return ResponseEntity.status(e.getRawStatusCode())
                    .headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsString());
        }
    }
}
