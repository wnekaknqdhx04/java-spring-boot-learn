package com.example.demo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@SpringBootApplication
@RestController
public class DemoApplication {
    public static void main(String[] args) {
      SpringApplication.run(DemoApplication.class, args);
    }
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
      return String.format("Hello 22222 %s!", name);
    }

	@GetMapping("/time")
	public String getCurrentTime() {
		LocalDateTime currentTime = LocalDateTime.now();
		return currentTime.toString();
	}

	@GetMapping("/download-image")
    public ResponseEntity<FileSystemResource> downloadImage() throws IOException {
        URL imageUrl = new URL("https://qcloudimg.tencent-cloud.cn/raw/6cd10d80b3e9b179db1df00775eaea70.png"); // 替换为实际的图片URL
        String fileName = "downloaded-image.png"; // 下载后保存的文件名

        File imageFile = downloadImageToFile(imageUrl, fileName);
        FileSystemResource resource = new FileSystemResource(imageFile);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + imageFile.getName());

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(imageFile.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
	
	private File downloadImageToFile(URL imageUrl, String fileName) throws IOException {
        URLConnection connection = imageUrl.openConnection();
        try (InputStream inputStream = connection.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(fileName)) {
            StreamUtils.copy(inputStream, outputStream);
        }

        return new File(fileName);
    }

	@GetMapping("/show-image")
	public ResponseEntity<byte[]> showImage() throws IOException {
		URL imageUrl = new URL("https://qcloudimg.tencent-cloud.cn/raw/6cd10d80b3e9b179db1df00775eaea70.png"); // 替换为实际的图片URL
		String fileName = "downloaded-image.png"; // 下载后保存的文件名

		byte[] imageBytes = downloadImage(imageUrl);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG); // 设置图片类型

		return ResponseEntity.ok()
				.headers(headers)
				.contentLength(imageBytes.length)
				.body(imageBytes);
	}

	private byte[] downloadImage(URL imageUrl) throws IOException {
		URLConnection connection = imageUrl.openConnection();
		try (InputStream inputStream = connection.getInputStream()) {
			return StreamUtils.copyToByteArray(inputStream);
		}
	}
}