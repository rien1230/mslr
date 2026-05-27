package com.example.mslr.service;

import com.example.mslr.model.SccCode;
import com.example.mslr.repo.SccCodeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SccCodeService {

    // IMPORTANT: order must match image_0..image_19
    private static final List<String> CODES = List.of(
            "1AZN0FXJVM",
            "JOV50TOSYR",
            "SDUBJ5IOYB",
            "YFUVLYBQZR",
            "IGBQET8OOY",
            "R2ZHBUYO2V",
            "Z9HOC1LF4X",
            "9IJKHGHJK4",
            "N5J53QK9FO",
            "ZDN06T01V9",
            "4XRDN9O4AW",
            "921664ML8D",
            "A546AKU16A",
            "V0GB2G690L",
            "12EOU5RGVX",
            "0IXYCAH8UW",
            "GKJ3K1YBGE",
            "46HJV9KH1F",
            "S6K3AV3IVR",
            "IKKSZYJTSH"
    );

    // matches image_0.png ... image_19.png (or jpg/jpeg)
    private static final Pattern QR_NAME =
            Pattern.compile("image_(\\d+)\\.(png|jpg|jpeg)$", Pattern.CASE_INSENSITIVE);

    private final SccCodeRepository sccCodeRepository;

    public SccCodeService(SccCodeRepository sccCodeRepository) {
        this.sccCodeRepository = sccCodeRepository;
    }

    /** Option 1 "QR scan": upload provided file image_0.png..image_19.png */
    public String sccFromQrFilename(String originalFilename) {
        if (originalFilename == null) return null;

        Matcher m = QR_NAME.matcher(originalFilename.trim());
        if (!m.find()) return null;

        int idx = Integer.parseInt(m.group(1));
        if (idx < 0 || idx >= CODES.size()) return null;

        return CODES.get(idx);
    }

    /** Validate SCC exists and is unused, then mark it used */
    @Transactional
    public void validateAndConsume(String codeRaw) {
        if (codeRaw == null || codeRaw.isBlank()) {
            throw new IllegalArgumentException("SCC is required.");
        }

        String code = codeRaw.trim();

        // since code is @Id, findById is perfect
        SccCode scc = sccCodeRepository.findById(code)
                .orElseThrow(() -> new IllegalArgumentException("SCC does not match any record in the database."));

        if (scc.isUsed()) {
            throw new IllegalStateException("This SCC has already been used (or the QR code was already scanned).");
        }

        scc.setUsed(true);
        sccCodeRepository.save(scc);
    }

    public List<SccCode> findAll() {
        return sccCodeRepository.findAll();
    }

    public boolean exists(String code) {
        return sccCodeRepository.existsById(code.trim());
    }

    public void createNewCode(String code) {
        String c = code.trim();
        if (c.length() != 10) {
            throw new IllegalArgumentException("SCC must be exactly 10 characters.");
        }
        if (sccCodeRepository.existsById(c)) {
            throw new IllegalStateException("SCC already exists: " + c);
        }

        SccCode s = new SccCode();
        s.setCode(c);
        s.setUsed(false);
        sccCodeRepository.save(s);
    }

    /** Alternative consume method using findByCodeAndUsedFalse */
    @Transactional
    public void consume(String code) {
        String c = code.trim();

        SccCode scc = sccCodeRepository.findByCodeAndUsedFalse(c)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or already used SCC code: " + c));

        scc.setUsed(true);
        sccCodeRepository.save(scc);
    }
}
