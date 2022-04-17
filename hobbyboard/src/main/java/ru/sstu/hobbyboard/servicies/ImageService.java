package ru.sstu.hobbyboard.servicies;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.sstu.hobbyboard.entities.dto.interfaces.Imageable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ImageService {

    @Value("${upload.path}")
    private String uploadPath;

    @SneakyThrows
    public List<String> saveImages(Imageable imageable) {
        List<MultipartFile> files = imageable.getFiles();
        if (files != null) {
            files = files.stream().filter(Objects::nonNull)
                    .filter((x) -> x.getOriginalFilename() != null)
                    .filter((x) -> (x.getOriginalFilename().endsWith(".png") || (x.getOriginalFilename().endsWith(".jpg"))))
                    .collect(Collectors.toList());

        } else {
            files = new ArrayList<>();
        }
        if (files.size() > 0) {

            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            List<String> result = new ArrayList<>();
            for (MultipartFile file : files) {
                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + "." + file.getOriginalFilename();
                file.transferTo(new File(uploadPath + "/" + resultFilename));
                result.add(resultFilename);
            }

            return result;
        }
        return List.of(imageable.getDefaultImage());
    }
}
