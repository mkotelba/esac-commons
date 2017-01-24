package com.esacinc.commons.io.utils;

import com.esacinc.commons.io.EsacDirectories;
import com.esacinc.commons.io.EsacFiles;
import com.esacinc.commons.net.EsacUris;
import com.esacinc.commons.utils.EsacStringUtils;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Comparator;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.ResourceUtils;

public final class EsacResourceUtils {
    public final static Comparator<Resource> LOC_FILE_NAME_COMPARATOR =
        Comparator.comparing(resource -> extractPath(resource, true), Comparator.nullsLast(((Comparator<String>) (resourcePath1, resourcePath2) -> {
            String resourceFilePath1 = extractFilePath(resourcePath1), resourceFilePath2 = extractFilePath(resourcePath2);

            if (!resourceFilePath1.endsWith(resourceFilePath2) && !resourceFilePath2.endsWith(resourceFilePath1)) {
                String resourceFileName1 = org.springframework.util.StringUtils.getFilename(resourceFilePath1),
                    resourceFileName2 = org.springframework.util.StringUtils.getFilename(resourceFilePath2);

                if (!resourceFileName1.equals(resourceFileName2)) {
                    String resourceFileNameExt1 = org.springframework.util.StringUtils.getFilenameExtension(resourceFileName1),
                        resourceFileNameExt2 = org.springframework.util.StringUtils.getFilenameExtension(resourceFileName2);

                    if (resourceFileNameExt1.equals(resourceFileNameExt2) &&
                        !(resourceFileName1 = org.springframework.util.StringUtils.stripFilenameExtension(resourceFileName1))
                            .equals((resourceFileName2 = org.springframework.util.StringUtils.stripFilenameExtension(resourceFileName2)))) {
                        if (StringUtils.endsWith(resourceFileName1, EsacFiles.TEST_NAME_SUFFIX) &&
                            StringUtils.removeEnd(resourceFileName1, EsacFiles.TEST_NAME_SUFFIX).equals(resourceFileName2)) {
                            return 1;
                        } else if (StringUtils.endsWith(resourceFileName2, EsacFiles.TEST_NAME_SUFFIX) &&
                            StringUtils.removeEnd(resourceFileName2, EsacFiles.TEST_NAME_SUFFIX).equals(resourceFileName1)) {
                            return -1;
                        } else {
                            return StringUtils.removeEnd(resourceFileName1, EsacFiles.TEST_NAME_SUFFIX)
                                .compareTo(StringUtils.removeEnd(resourceFileName2, EsacFiles.TEST_NAME_SUFFIX));
                        }
                    }
                }
            }

            return 0;
        })));

    public final static Comparator<Resource> LOC_FILE_PATH_COMPARATOR =
        Comparator.comparing(resource -> extractPath(resource, true), Comparator.nullsLast(Comparator.comparing(EsacResourceUtils::extractFilePath)));

    public final static Comparator<Resource> LOC_PROTOCOL_COMPARATOR = Comparator.comparing(resource -> {
        try {
            if (resource instanceof FileSystemResource) {
                return 2;
            } else if (resource instanceof UrlResource) {
                URL resourceUrl = resource.getURL();

                if (ResourceUtils.isJarURL(resourceUrl)) {
                    return 1;
                } else if (ResourceUtils.isFileURL(resourceUrl)) {
                    return 2;
                }
            }
        } catch (IOException ignored) {
        }

        return 0;
    });

    public final static Comparator<Resource> LOC_ARCHIVE_FILE_PATH_COMPARATOR = Comparator.comparing(resource -> extractPath(resource, true),
        Comparator.nullsLast(Comparator.comparing(resourcePath -> extractFilePath(resourcePath, true))));

    public final static Comparator<Resource> LOC_COMPARATOR =
        LOC_FILE_NAME_COMPARATOR.thenComparing(LOC_FILE_PATH_COMPARATOR).thenComparing(LOC_PROTOCOL_COMPARATOR).thenComparing(LOC_ARCHIVE_FILE_PATH_COMPARATOR);

    private final static String META_INF_DIR_PATH_PART = EsacStringUtils.SLASH + EsacDirectories.META_INF_NAME + EsacStringUtils.SLASH;

    private EsacResourceUtils() {
    }

    public static String extractFilePath(String resourcePath) {
        return extractFilePath(resourcePath, false);
    }

    public static String extractFilePath(String resourcePath, boolean jarFilePath) {
        return (isJarPath(resourcePath) ? extractJarPathParts(resourcePath)[(jarFilePath ? 0 : 1)] : resourcePath);
    }

    public static String[] extractJarPathParts(String jarPath) {
        return StringUtils.split(jarPath, EsacStringUtils.APOS, 2);
    }

    public static boolean isJarPath(String resourcePath) {
        return (StringUtils.startsWith(resourcePath, EsacUris.JAR_FILE_URL_PREFIX) && resourcePath.contains(ResourceUtils.JAR_URL_SEPARATOR));
    }

    @Nullable
    public static String extractPath(Resource resource) {
        return extractPath(resource, false);
    }

    @Nullable
    public static String extractPath(Resource resource, boolean fromMetaInf) {
        try {
            String resourcePath = null;

            if (resource instanceof UrlResource) {
                resourcePath = resource.getURL().toExternalForm();
            } else if (resource instanceof FileSystemResource) {
                resourcePath = ((FileSystemResource) resource).getPath();
            } else if (resource instanceof ClassPathResource) {
                resourcePath = ((ClassPathResource) resource).getPath();
            } else if (resource instanceof PathResource) {
                resourcePath = ((PathResource) resource).getPath();
            } else {
                return null;
            }

            return (fromMetaInf ? StringUtils.substringAfterLast(resourcePath, META_INF_DIR_PATH_PART) : resourcePath);
        } catch (IOException ignored) {
        }

        return null;
    }

    @Nullable
    public static URL extractUrl(Resource resource) {
        try {
            return resource.getURL();
        } catch (IOException ignored) {
        }

        return null;
    }

    @Nullable
    public static URI extractUri(Resource resource) {
        try {
            return resource.getURI();
        } catch (IOException ignored) {
        }

        return null;
    }
}
