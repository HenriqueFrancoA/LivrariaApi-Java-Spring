package br.com.henrique.JWT.services;

import br.com.henrique.JWT.config.FileStorageConfig;
import br.com.henrique.JWT.exceptions.FileStorageException;
import br.com.henrique.JWT.exceptions.MyFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

@Service
public class FileStorageService {

    private final Logger logger = Logger.getLogger(FileStorageService.class.getName());

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageConfig fileStorageConfig){
        Path path = Paths.get(fileStorageConfig.getUploadDir())
                .toAbsolutePath().normalize();

        this.fileStorageLocation = path;

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            throw new FileStorageException("Não pode criar o diretório onde os arquivos vão ser armazenados.", e);
        }
    }

    public String storeFile(MultipartFile file){
        logger.info("Armazenando arquivo " + file.getName());
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            Path targetLocation = this.fileStorageLocation.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return filename;
        } catch (Exception e) {
            throw new FileStorageException("Não pode armazernar o arquivo " + filename + " tente novamente." ,e);
        }
    }

    public Resource loadFilesAsResource(String filename){
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();

            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists()) return resource;
            else throw new MyFileNotFoundException("Arquivo não encontrado " + filename);


        } catch (Exception e) {
            throw new MyFileNotFoundException("Arquivo não encontrado " + filename, e);
        }
    }
}
