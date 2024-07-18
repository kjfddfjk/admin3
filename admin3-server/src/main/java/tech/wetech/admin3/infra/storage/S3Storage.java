package tech.wetech.admin3.infra.storage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import tech.wetech.admin3.sys.model.StorageConfig;

import java.io.InputStream;
import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author cjbi
 */
public class S3Storage implements Storage {

  private final Logger log = LoggerFactory.getLogger(S3Storage.class);

  // Share S3Client without reconnect each time,
  private static final ConcurrentMap<Long, S3Client> s3clientMap = new ConcurrentHashMap<>();

  private final StorageConfig config;

  public S3Storage(StorageConfig config) {
    this.config = config;
  }

    private S3Client getS3Client() {
        return s3clientMap.computeIfAbsent(config.getId(), k -> S3Client.builder()
            .endpointOverride(URI.create(config.getEndpoint()))
            .region(StringUtils.isBlank(config.getRegion()) ?
                Region.AWS_GLOBAL : Region.of(config.getRegion()))
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(config.getAccessKey(), config.getSecretKey())))
            .serviceConfiguration((S3Configuration) (config.getExtraConfig() == null ? S3Configuration.builder().build()
                    : config.getExtraConfig().toServiceConfiguration())
            )
            .build());
    }

  /**
   * 上传文件
   *
   * @param bucketName  bucket名称
   * @param objectName  文件名称
   * @param is      文件流
   * @param contentType 类型
   * @param contentLength 大小
   * @see <a href= "http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/PutObject">AWS
   * API Documentation</a>
   */
  public PutObjectResponse putObject(String bucketName, String objectName, InputStream is,
                                     String contentType, long contentLength) {
    try {
      return getS3Client().putObject(
        req -> req.bucket(bucketName).key(objectName).contentType(contentType),
        RequestBody.fromInputStream(is, contentLength)
      );
    } catch (AwsServiceException e) {
        throw new RuntimeException(e);
    } catch (SdkClientException e) {
        throw new RuntimeException(e);
    }
  }

  @Override
  public String getId() {
    return config.getStorageId();
  }

  @Override
  public void store(InputStream inputStream, long contentLength, String contentType, String keyName) {
    String bucketName = config.getBucketName();

    if (!doesBucketExist(bucketName)) {
      createBucket(bucketName);
    }
    putObject(bucketName, keyName, inputStream, contentType, contentLength);
  }

  private void createBucket(String bucketName) {
    try {
        getS3Client().createBucket(req -> req.bucket(bucketName));
    } catch (AwsServiceException e) {
        throw new RuntimeException(e);
    } catch (SdkClientException e) {
        throw new RuntimeException(e);
    }
  }

  private boolean doesBucketExist(String bucketName) {
    try {
        getS3Client().headBucket(req -> req.bucket(bucketName));
        return true;
    } catch (AwsServiceException ex) {
      if (ex.statusCode() == 404 | ex.statusCode() == 403 || ex.statusCode() == 301) {
        return false;
      }
      throw ex;
    } catch (Exception ex) {
      log.error("Cannot check access", ex);
      throw ex;
    }
  }

  @Override
  public InputStream getFileContent(String filename) {
    String bucketName = config.getBucketName();
    try {
      return getS3Client().getObject(req -> req.bucket(bucketName).key(filename));
    } catch (AwsServiceException e) {
        throw new RuntimeException(e);
    } catch (SdkClientException e) {
        throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(String filename) {
    String bucketName = config.getBucketName();
    getS3Client().deleteObject(req -> req.bucket(bucketName).key(filename));
  }

//  @Override
  public String getUrl(String filename) {
    if (config.getAddress() != null) {
      return config.getAddress() + filename;
    }
    String bucketName = config.getBucketName();
    try {
      return getS3Client().utilities()
        .getUrl(req -> req.bucket(bucketName).key(filename)).toString();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
  }

    public void invalidClient(Long id) {
        s3clientMap.computeIfPresent((id), (key, client) -> {
            try { client.close();} catch (Exception e) {}
            return null;
        });
        s3clientMap.remove(id);
    }

    public void invalidClient() {
        s3clientMap.keySet().parallelStream().forEach(id -> invalidClient(id));
    }

}
