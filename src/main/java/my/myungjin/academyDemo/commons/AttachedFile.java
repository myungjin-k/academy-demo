package my.myungjin.academyDemo.commons;


import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.commons.lang3.StringUtils.*;

@RequiredArgsConstructor
public class AttachedFile {

    private final String originalFileName;

    private final String contentType;

    private final byte[] bytes;

    private static boolean verify(MultipartFile multipartFile) {
        if (multipartFile != null && multipartFile.getSize() > 0 && multipartFile.getOriginalFilename() != null) {
            String contentType = multipartFile.getContentType();
            String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            // 첨부파일 타입을 확인하고 이미지인 경우 처리
            if (isNotEmpty(contentType) &&
                    ("jpg".equals(extension) || "png".equals(extension) || "jpeg".equals(extension)))
                return true;
        }
        return false;
    }

    public static AttachedFile toAttachedFile(MultipartFile multipartFile) throws IOException {
        return verify(multipartFile) ?
                new AttachedFile(multipartFile.getOriginalFilename(), multipartFile.getContentType(), multipartFile.getBytes())
                : null;
    }

    public String extension(String defaultExtension) {
        return defaultIfEmpty(getExtension(originalFileName), defaultExtension);
    }

    public String randomName(String defaultExtension) {
        return randomName(null, defaultExtension);
    }

    public String randomName(String basePath, String defaultExtension) {
        String name = isEmpty(basePath) ? UUID.randomUUID().toString() : basePath + "/" + UUID.randomUUID().toString();
        return name + "." + extension(defaultExtension);
    }

    public InputStream inputStream() {
        return new ByteArrayInputStream(bytes);
    }

    public long length() {
        return bytes.length;
    }

    public String getContentType() {
        return contentType;
    }

}