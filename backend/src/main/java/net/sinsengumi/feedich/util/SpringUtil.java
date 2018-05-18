package net.sinsengumi.feedich.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public final class SpringUtil {

    public static boolean isEmptyMultipartFile(MultipartFile file) {
        return file == null || file.isEmpty() || StringUtils.isEmpty(file.getOriginalFilename());
    }
}
