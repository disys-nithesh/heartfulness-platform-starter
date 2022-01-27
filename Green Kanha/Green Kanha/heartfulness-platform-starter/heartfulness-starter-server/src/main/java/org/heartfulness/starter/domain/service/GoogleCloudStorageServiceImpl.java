package org.heartfulness.starter.domain.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class GoogleCloudStorageServiceImpl implements CloudStorageService {

    private final String GOOGLE_STORAGE_URL = "https://storage.googleapis.com/";

    @Value(value = "${google.storage.bucket.name}")
    String bucketName;

    @Value("${google.storage.service.account.key:secret}")
    String serviceAccountKeyJson;

    boolean intialized = false;
    private Storage storage;

    private void initialize() throws IOException, ParserConfigurationException, SAXException {
        File serviceAccountJson = new File(serviceAccountKeyJson);
        Credentials credentials = null;

        if(serviceAccountJson.exists()) {
            credentials = GoogleCredentials.fromStream(new FileInputStream(serviceAccountJson));
        } else {
            credentials = GoogleCredentials.getApplicationDefault();
        }

        StorageOptions options = StorageOptions.newBuilder().setCredentials(credentials).build();
        storage = options.getService();
    }

    @Override
    public void uploadToCloudStorage(String filePath, InputStream fileData, boolean makePubliclyAvailable) throws IOException, ParserConfigurationException, SAXException {
        if (!this.intialized) {
            initialize();
            this.intialized = true;
        }

        BlobInfo blob = BlobInfo.newBuilder(bucketName, filePath).build();
        writeToGoogleStorage(blob, fileData, this.storage, makePubliclyAvailable);
    }

    protected void writeToGoogleStorage(BlobInfo blob, InputStream inputStream, Storage storage, boolean makePubliclyAvailable) throws IOException {
        com.google.cloud.storage.Blob b = storage.create(blob
                , inputStream);

        if (makePubliclyAvailable) {
            b.createAcl(Acl.of(User.ofAllUsers(), Acl.Role.READER));
        }
    }

    @Override
    public String getFileURL(String filePath) throws IOException, ParserConfigurationException, SAXException {
        if (!intialized) {
            initialize();
            intialized = true;
        }

        return GOOGLE_STORAGE_URL + bucketName + "/" + filePath;
    }
}
