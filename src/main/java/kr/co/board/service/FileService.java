package kr.co.board.service;

import kr.co.board.model.File;
import kr.co.board.model.Post;
import kr.co.board.model.enums.SaveType;
import kr.co.board.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;
    private Integer sequence = 0;

    private final FileRepository fileRepository;

    @PostConstruct
    public void initializeDirectory() {
        Path attachmentPath = Paths.get(this.uploadPath);
        Path summernoteImgPath = Paths.get(this.uploadPath + "/summernote");

        try {
            if (!Files.exists(attachmentPath)) {
                Files.createDirectories(attachmentPath);
            }
            if (!Files.exists(summernoteImgPath)) {
                Files.createDirectories(summernoteImgPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void saveAttachment(MultipartFile multipartFile, Post post) throws IOException {
        if(!multipartFile.getOriginalFilename().equals("")) {
            File file = createFile(multipartFile);

            file.assignPost(post);
            file = fileRepository.save(file);
            uploadFile(multipartFile, file);
        }
    }

    private void uploadFile(MultipartFile multipartFile, File file) throws IOException {
        Path path = Paths.get(this.uploadPath + file.getRelativePath());
        Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        file.updateSaveType(SaveType.ATTACHMENT);
    }

    private void uploadImage(MultipartFile multipartFile, File file) throws IOException {
        Path path = Paths.get(this.uploadPath +  "/summernote" + file.getRelativePath());
        Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        file.updateSaveType(SaveType.SUMMERNOTE);
    }

    private File createFile(MultipartFile multipartFile) {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String fileName = String.format("%s_%d", timeStamp, this.sequence);
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        File file = File.builder()
                .name(fileName)
                .originalName(multipartFile.getOriginalFilename())
                .size(BigInteger.valueOf(multipartFile.getSize()))
                .extension(extension)
                .relativePath(String.format("/%s.%s", fileName, extension))
                .sequence(this.sequence)
                .fileType(multipartFile.getContentType()).build();
        return file;
    }

    private List<File> createFiles(List<MultipartFile> multipartFileList) {
        List<File> files = new ArrayList<>();
        for (int i=0; i < multipartFileList.size(); i++) {
            this.sequence = i;
            File file = createFile(multipartFileList.get(i));
            files.add(file);
        }
        this.sequence = 0;
        return files;
    }

    public File findById(Long id) {
        Optional<File> optionalFile = fileRepository.findById(id);
        return optionalFile.orElse(null);
    }

    public ResponseEntity<?> downloadFileById(Long id) throws IOException {
        File file = this.findById(id);
        String encodedOriginalFileName = URLEncoder
                .encode(file.getOriginalName(), "UTF-8")
                .replaceAll("\\+", "%20");
        Path downloadPath = Paths.get(uploadPath + file.getRelativePath());
        Resource resource = new InputStreamResource(Files.newInputStream(downloadPath));
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + encodedOriginalFileName + "\"")
                .body(resource);
    }

    @Transactional
    public void updateAttachment(Post post, Long[] deleteFileIds, List<MultipartFile> multipartFiles) throws IOException {
        if (deleteFileIds.length > 0) {
            for (Long deleteFileId : deleteFileIds) {
                this.deleteFileById(deleteFileId);
            }
        }
        if (multipartFiles.size() > 0) {
            for (MultipartFile multipartFile : multipartFiles) {
                this.saveAttachment(multipartFile, post);
            }
        }
    }

    @Transactional
    public void deleteFileById(Long id) {
        File fileToDelete = this.findById(id);
        Post p = fileToDelete.getPost();

        p.deleteFileById(id);

        fileRepository.deleteById(id);
        String path = this.uploadPath + fileToDelete.getRelativePath();
        FileUtils.deleteQuietly(FileUtils.getFile(path));
    }
    @Transactional
    public List<File> saveImages(MultipartFile[] multipartFiles) throws IOException {
        List<File> files = new ArrayList<>();
        for (MultipartFile multipartFile: multipartFiles) {
            if(!multipartFile.getOriginalFilename().equals("")) {
                File file = createFile(multipartFile);
                file = fileRepository.save(file);
                uploadImage(multipartFile, file);
                files.add(file);
            }
        }
        return files;
    }

    @Transactional
    public List<File> saveFiles(MultipartFile[] multipartFiles) throws IOException {
        List<File> files = new ArrayList<>();
        for (MultipartFile multipartFile: multipartFiles) {
            if(!multipartFile.getOriginalFilename().equals("")) {
                File file = createFile(multipartFile);
                file = fileRepository.save(file);
                uploadFile(multipartFile, file);
                files.add(file);
            }
        }
        return files;
    }

    public void setPostId(Long[] imgIds, Post post){
        for(int i=0; i<imgIds.length; i++) {
            File file = this.findById(imgIds[i]);
            file.assignPost(post);
        }
    }
}