package ru.sstu.hobbyboard.entities.dto.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface Imageable {
    String getDefaultImage();

    List<MultipartFile> getFiles();
}
