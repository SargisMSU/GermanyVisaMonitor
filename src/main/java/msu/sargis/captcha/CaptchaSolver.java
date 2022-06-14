package msu.sargis.captcha;

import com.twocaptcha.TwoCaptcha;
import com.twocaptcha.captcha.Normal;

public class CaptchaSolver {
    private static CaptchaSolver instance;
    private final String apiKey;

    public CaptchaSolver() {
        this.apiKey = "4ef5798d992966b6e8b70f3901ffe698";
    }

    public static CaptchaSolver getInstance() {
        if (instance == null) instance = new CaptchaSolver();
        return instance;
    }

    public String solveCaptcha(String base64) {
        TwoCaptcha solver = new TwoCaptcha(apiKey);
        String code = null;
        Normal captcha = new Normal();
        try {
            captcha.setBase64(base64);
            solver.solve(captcha);
            code = captcha.getCode();
            System.out.println("Captcha solved: " + code);
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
        return code;
    }
}
