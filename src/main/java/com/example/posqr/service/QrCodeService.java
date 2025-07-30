package com.example.posqr.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QrCodeService {

    private static final String QR_CODE_IMAGE_PATH = "src/main/resources/static/qrcodes/";

    public void generateQrCodeImage(String text, int width, int height, String fileName) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        Path path = FileSystems.getDefault().getPath(QR_CODE_IMAGE_PATH + fileName);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public String getQrCodeImagePath(String fileName) {
        return QR_CODE_IMAGE_PATH + fileName;
    }

    public String generateQrCodeTextForProduct(Long productId, String productName, String price) {
        return "Product ID: " + productId + "\nName: " + productName + "\nPrice: $" + price;
    }

    public String generateQrCodeTextForSale(Long saleId, String productName, String quantity, String totalAmount) {
        return "Sale ID: " + saleId + "\nProduct: " + productName + "\nQuantity: " + quantity + "\nTotal: $" + totalAmount;
    }

    public String generateQrFileName(Long productId) {
        return "product-qr-" + productId + ".png";
    }

    public String generateTimestampedQrFileName(Long id, String prefix) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return prefix + "-qr-" + id + "-" + timestamp + ".png";
    }

    public boolean deleteQrCodeImage(String fileName) {
        Path path = FileSystems.getDefault().getPath(QR_CODE_IMAGE_PATH + fileName);
        try {
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void clearAllQrCodes() {
        try {
            FileUtils.cleanDirectory(new File(QR_CODE_IMAGE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> listAllQrCodeFiles() {
        File dir = new File(QR_CODE_IMAGE_PATH);
        String[] files = dir.list((d, name) -> name.endsWith(".png"));
        return files != null ? Arrays.asList(files) : Collections.emptyList();
    }

    public boolean doesQrCodeExist(String fileName) {
        File file = new File(QR_CODE_IMAGE_PATH + fileName);
        return file.exists();
    }

    public long getQrCodeFileSize(String fileName) {
        File file = new File(QR_CODE_IMAGE_PATH + fileName);
        return file.exists() ? file.length() : -1;
    }

    public Map<String, Object> getQrCodeMetadata(String fileName) {
        File file = new File(QR_CODE_IMAGE_PATH + fileName);
        Map<String, Object> metadata = new HashMap<>();
        if (file.exists()) {
            metadata.put("name", file.getName());
            metadata.put("size", file.length());
            metadata.put("lastModified", new Date(file.lastModified()));
        }
        return metadata;
    }

    public byte[] generateQrCodeToBytes(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    public String generateQrCodeAsBase64(String text, int width, int height) throws WriterException, IOException {
        byte[] qrBytes = generateQrCodeToBytes(text, width, height);
        return Base64.getEncoder().encodeToString(qrBytes);
    }

    public boolean renameQrCodeFile(String oldName, String newName) {
        File oldFile = new File(QR_CODE_IMAGE_PATH + oldName);
        File newFile = new File(QR_CODE_IMAGE_PATH + newName);
        return oldFile.exists() && oldFile.renameTo(newFile);
    }

    public boolean moveQrCodeFile(String fileName, String newDirectory) {
        File oldFile = new File(QR_CODE_IMAGE_PATH + fileName);
        File newDir = new File(newDirectory);
        if (!newDir.exists()) newDir.mkdirs();
        File newFile = new File(newDir, fileName);
        return oldFile.exists() && oldFile.renameTo(newFile);
    }

    public boolean copyQrCodeFile(String fileName, String destinationDirectory) {
        File srcFile = new File(QR_CODE_IMAGE_PATH + fileName);
        File destFile = new File(destinationDirectory + File.separator + fileName);
        try {
            FileUtils.copyFile(srcFile, destFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<String, Object>> getAllQrCodeMetadata() {
        File folder = new File(QR_CODE_IMAGE_PATH);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));
        if (files == null) return Collections.emptyList();

        return Arrays.stream(files).map(file -> {
            Map<String, Object> data = new HashMap<>();
            data.put("name", file.getName());
            data.put("size", file.length());
            data.put("lastModified", new Date(file.lastModified()));
            return data;
        }).collect(Collectors.toList());
    }

    public String getQrCodeDirectoryPath() {
        return QR_CODE_IMAGE_PATH;
    }
} 
