package com.example.posqr.controller;

import com.example.posqr.service.QrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/qrcode")
public class QrCodeController {

    private final QrCodeService qrCodeService;

    @Autowired
    public QrCodeController(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @GetMapping("/generate/{productId}")
    public ResponseEntity<Resource> generateQrCode(@PathVariable Long productId) {
        byte[] qrImage = qrCodeService.generateQrCodeForProduct(productId);
        ByteArrayResource resource = new ByteArrayResource(qrImage);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=product-" + productId + "-qrcode.png")
                .contentType(MediaType.IMAGE_PNG)
                .contentLength(qrImage.length)
                .body(resource);
    }

    @GetMapping("/preview/{productId}")
    public ResponseEntity<Resource> previewQrCode(@PathVariable Long productId) {
        byte[] qrImage = qrCodeService.generateQrCodeForProduct(productId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(new ByteArrayResource(qrImage));
    }

    @PostMapping("/decode")
    public ResponseEntity<String> decodeQrCode(@RequestParam("image") MultipartFile file) {
        try {
            byte[] imageBytes = file.getBytes();
            String decodedText = qrCodeService.decodeQrCode(imageBytes);
            return ResponseEntity.ok(decodedText);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to decode QR code: " + e.getMessage());
        }
    }

    @PostMapping("/batch/generate")
    public ResponseEntity<List<byte[]>> generateQrCodesForMultipleProducts(@RequestBody List<Long> productIds) {
        List<byte[]> qrCodes = qrCodeService.generateQrCodesForProducts(productIds);
        return ResponseEntity.ok(qrCodes);
    }

    @GetMapping("/text/{productId}")
    public ResponseEntity<String> getQrCodeText(@PathVariable Long productId) {
        String qrContent = qrCodeService.getQrCodeTextForProduct(productId);
        return ResponseEntity.ok(qrContent);
    }

    @DeleteMapping("/clear-cache")
    public ResponseEntity<String> clearQrCodeCache() {
        qrCodeService.clearQrCodeCache();
        return ResponseEntity.ok("QR code cache cleared successfully.");
    }

    @PostMapping("/save-to-disk/{productId}")
    public ResponseEntity<String> saveQrCodeToDisk(@PathVariable Long productId) {
        boolean saved = qrCodeService.saveQrCodeToDisk(productId);
        return saved ? ResponseEntity.ok("QR code saved to disk.") : ResponseEntity.status(500).body("Failed to save QR code to disk.");
    }

    @GetMapping("/exists/{productId}")
    public ResponseEntity<Boolean> checkQrCodeExists(@PathVariable Long productId) {
        boolean exists = qrCodeService.doesQrCodeExist(productId);
        return ResponseEntity.ok(exists);
    }

    @PutMapping("/update-text/{productId}")
    public ResponseEntity<String> updateQrCodeText(@PathVariable Long productId, @RequestBody Map<String, String> payload) {
        String newText = payload.get("text");
        qrCodeService.updateQrCodeText(productId, newText);
        return ResponseEntity.ok("QR code content updated successfully.");
    }

    @PostMapping("/generate-url-qr")
    public ResponseEntity<Resource> generateQrCodeFromUrl(@RequestParam("url") String url) {
        byte[] qrImage = qrCodeService.generateQrCodeFromText(url);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(new ByteArrayResource(qrImage));
    }

    @GetMapping("/scan-count/{productId}")
    public ResponseEntity<Integer> getScanCount(@PathVariable Long productId) {
        int count = qrCodeService.getScanCount(productId);
        return ResponseEntity.ok(count);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteQrCode(@PathVariable Long productId) {
        qrCodeService.deleteQrCode(productId);
        return ResponseEntity.ok("QR code deleted for product ID: " + productId);
    }

    @PostMapping("/regenerate-all")
    public ResponseEntity<String> regenerateAllQrCodes() {
        qrCodeService.regenerateAllQrCodes();
        return ResponseEntity.ok("All QR codes regenerated successfully.");
    }

    @GetMapping("/metadata/{productId}")
    public ResponseEntity<Map<String, Object>> getQrCodeMetadata(@PathVariable Long productId) {
        Map<String, Object> metadata = qrCodeService.getQrCodeMetadata(productId);
        return ResponseEntity.ok(metadata);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllQrCodesMetadata() {
        List<Map<String, Object>> metadataList = qrCodeService.getAllQrCodesMetadata();
        return ResponseEntity.ok(metadataList);
    }
}
