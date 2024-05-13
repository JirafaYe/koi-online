//package com.xc.user.utils;
//
//
//import org.springframework.util.StringUtils;
//
///**
// * 用于检查是否存在参数非法
// */
//public class CheckUtils {
//
//    public static void checkUserLoginDto(UserLoginDto userLoginDto) {
//        String email = userLoginDto.getEmail();
//        checkEmail(email);
//        String password = userLoginDto.getPassword();
//        if (StringUtils.isEmpty(password) || password.length() < 6 || password.length() > 16) {
//            throw new CloudDiskException(ExceptionConstant.PASSWORD_REGEX_ERROR);
//        }
//    }
//
//
//    public static void checkEmail(String email) {
//        String regex = "^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$";
//        boolean matches = email.matches(regex);
//        if (!matches || email == null || "".equals(email)) {
//            throw new CloudDiskException(ExceptionConstant.MAIL_ERROR);
//        }
//    }
//
//    public static void checkUserRegisterDto(UserRegisterDto userRegisterDto) {
//        String email = userRegisterDto.getEmail();
//        checkEmail(email);
//        String password = userRegisterDto.getPassword();
//        if (StringUtils.isEmpty(password) || password.length() < 6 || password.length() > 16) {
//            throw new CloudDiskException(ExceptionConstant.PASSWORD_REGEX_ERROR);
//        }
//        String userName = userRegisterDto.getUserName();
//        if (userName == null ||!userName.matches("^[a-zA-Z0-9_-]{4,16}$")) {
//            throw new CloudDiskException(ExceptionConstant.USER_NAME_ERROR);
//        }
//        Long phone = userRegisterDto.getPhone();
//        String regex = "^1[3-9]\\d{9}$";
//        if (phone==null||!String.valueOf(phone).matches(regex)) {
//            throw new CloudDiskException(ExceptionConstant.PHONE_ERROR);
//        }
//    }
//
//    public static void checkResetPasswordDto(ResetPasswordDto resetPasswordDto) {
//        checkEmail(resetPasswordDto.getEmail());
//        String password = resetPasswordDto.getPassword();
//        if (StringUtils.isEmpty(password) || password.length() < 6 || password.length() > 16) {
//            throw new CloudDiskException(ExceptionConstant.PASSWORD_REGEX_ERROR);
//        }
//    }
//}
