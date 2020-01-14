package com.ddc.Utils;

public class DataCheck {
    public static String normalizePhoneNumber(String phoneNumber) throws WrongPhoneNumber {
        if (!phoneNumber.matches("(((05)|(\\+?(9725)))[0-9]{8})"))
            throw new WrongPhoneNumber();

        if (phoneNumber.startsWith("05") || phoneNumber.startsWith("9725"))
            phoneNumber = phoneNumber.replaceFirst("(05)|(9725)", "+9725");
        return phoneNumber;
    }

    public static class WrongPhoneNumber extends Exception {
    }
}
