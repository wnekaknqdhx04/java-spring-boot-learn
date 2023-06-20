package com.example.demo;
import ch.qos.logback.core.util.Loader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    // @PostMapping("/bankcard/recognize")
    @GetMapping("/kim")
    public ResponseEntity<String> recognizeBankCard() {
        try {
            // // 将Base64编码的图像数据解码为字节数组
            // byte[] imageData = Base64.getDecoder().decode(imageBase64);

            // // 将图像数据保存为临时文件
            // String imagePath = saveImageToFile(imageData);

            String imagePath = "downloaded-image.png"; // 下载后保存的文件名

            // 执行银行卡号识别
            String bankCardNumber = recognizeBankCardNumber(imagePath);

            // 删除临时文件
            Files.deleteIfExists(Paths.get(imagePath));

            // 返回识别到的银行卡号
            return ResponseEntity.ok(bankCardNumber);
        } catch (Exception e) {
            // 发生错误时返回错误信息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("识别银行卡号时发生错误");
        }
    }

    private String saveImageToFile(byte[] imageData) throws Exception {
        String imagePath = "temp_image.jpg";
        Files.write(Paths.get(imagePath), imageData);
        return imagePath;
    }

    private String recognizeBankCardNumber(String imagePath) throws Exception {
        // 加载OpenCV库
        // Loader.load(org.bytedeco.javacpp.opencv_core.class);

        // // 读取图像文件
        // org.bytedeco.opencv.opencv_core.Mat image = opencv_imgcodecs.imread(imagePath);

        // // 转换为灰度图像
        // Mat grayImage = new Mat();

        // opencv_imgproc.cvtColor(image, grayImage, opencv_imgproc.CV_BGR2GRAY);

        // // 进行图像处理，例如调整大小、二值化等
        // // 这里只是一个示例，你可以根据实际需求进行其他处理
        // opencv_imgproc.resize(grayImage, grayImage, new Size(800, 600));
        // opencv_imgproc.threshold(grayImage, grayImage, 0, 255, opencv_imgproc.THRESH_BINARY | opencv_imgproc.THRESH_OTSU);

        // // 创建Tesseract OCR实例
        // ITesseract tesseract = new Tesseract();

        // // 设置OCR语言为英文（根据实际需求设置）
        // tesseract.setLanguage("eng");

        // // 识别银行卡号
        // String result = tesseract.doOCR(new File(imagePath));

        // // 提取银行卡号
        // String bankCardNumber = extractBankCardNumber(result);

        // return bankCardNumber;
        return "233";
    }

    private String extractBankCardNumber(String ocrResult) {
        // 在OCR结果中提取银行卡号
        // 这里只是一个示例，你可能需要根据具体的OCR结果格式进行提取

        // 假设OCR结果中银行卡号位于第一行
        String[] lines = ocrResult.split("\n");
        if (lines.length > 0) {
            return lines[0].replaceAll("[^0-9]", ""); // 提取数字部分
        }

        return null;
    }
}