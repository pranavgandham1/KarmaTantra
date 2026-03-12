package com.interviewprep;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {


private Map<String, String> otpStorage = new HashMap<>();

public String generateOtp(String email) {

    Random random = new Random();

    int otp = 100000 + random.nextInt(900000);

    String otpString = String.valueOf(otp);

    otpStorage.put(email, otpString);

    return otpString;
}

public boolean verifyOtp(String email, String otp) {

    String storedOtp = otpStorage.get(email);

    if (storedOtp != null && storedOtp.equals(otp)) {
        otpStorage.remove(email);
        return true;
    }

    return false;
}


}
