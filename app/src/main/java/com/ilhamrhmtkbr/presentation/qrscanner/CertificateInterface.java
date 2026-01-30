package com.ilhamrhmtkbr.presentation.qrscanner;

public interface CertificateInterface {
    default String extractCertificateId(String url) {
        // Extract ID from: https://course.iamra.site/certificates?id=5c13..dst-uuid
        if (url != null && url.contains("id=")) {
            try {
                // Split by "id=" and get the part after it
                String[] parts = url.split("id=");
                if (parts.length == 2 && !parts[1].isEmpty()) {
                    // Get the ID and remove any trailing parameters or fragments
                    String id = parts[1].split("[&#]")[0];
                    return id.trim();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
