package com.umeshgiri.drones.service;

import com.umeshgiri.drones.entity.Document;
import com.umeshgiri.drones.entity.DocumentType;
import com.umeshgiri.drones.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Service
public class DocumentService {
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of("image/png", "image/jpeg", "image/gif");

    private final DocumentRepository documentRepository;

    @Value("${file.upload-dir}")
    private String documentLocation;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public String getUrl(Document document) {
        return "/image/" + document.getType().name().toLowerCase() + "/" + document.getName() + "." + document.getExtension();
    }

    public Document save(MultipartFile multipartFile, DocumentType documentType) throws IOException {
        String contentType = multipartFile.getContentType();

        if (!ALLOWED_MIME_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Invalid file type. Only PNG, JPEG, and GIF images are allowed");
        }

        String originalFilename = multipartFile.getOriginalFilename();

        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file must have a name.");
        }

        return save(multipartFile.getBytes(), getExtension(originalFilename), documentType);
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    private Document save(byte[] bytes, String extension, DocumentType documentType) throws IOException {
        Document document = new Document();
        document.setExtension(extension);
        document.setName(UUID.randomUUID().toString());
        document.setType(documentType);
        document.setUrl(getUrl(document));

        writeFileToDisk(bytes, document);

        return documentRepository.save(document);
    }

    private void writeFileToDisk(byte[] bytes, Document document) throws IOException {
        Path dir = Paths.get(documentLocation, document.getType().name().toLowerCase());
        Files.createDirectories(dir);
        Path file = dir.resolve(document.getName() + "." + document.getExtension());
        Files.write(file, bytes);
    }
}
