package tech.wetech.admin3.infra.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.wetech.admin3.sys.model.StorageConfig;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author cjbi
 */
public class LocalStorage implements Storage {
  private static final Logger log = LoggerFactory.getLogger(LocalStorage.class);
  private final StorageConfig config;

  private final Path rootLocation;

  public LocalStorage(StorageConfig config) {
    this.config = config;

    this.rootLocation = Paths.get(config.getStoragePathWithEnv());
    try {
      Files.createDirectories(rootLocation);
    } catch (IOException e) {
      log.error("unable create directories {}", this.rootLocation, e);
    }
  }


  @Override
  public String getId() {
    return config.getStorageId();
  }

  @Override
  public void store(InputStream inputStream, long contentLength, String contentType, String keyName) {
    try {
      Files.copy(inputStream, rootLocation.resolve(keyName), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw new RuntimeException("Failed to store file " + keyName, e);
    }
  }

  private Path get(String filename) {
    return rootLocation.resolve(filename);
  }

  public InputStream getFileContent(String filename) {
    try {
      Path path = get(filename);
      File file = path.toFile();
      if (!file.exists()) {
        return null;
      }
      return new FileInputStream(path.toFile());
    } catch (FileNotFoundException e) {
      return null;
    }
  }

  @Override
  public void delete(String filename) {
    Path file = get(filename);
    try {
      Files.delete(file);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
  }

}
