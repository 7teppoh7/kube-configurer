package ru.sstu.hobbyboard.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.sstu.hobbyboard.entities.dto.interfaces.Imageable;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MakerDTO implements Imageable {

    @NotBlank
    private String name;

    private String description;

    private MultipartFile logo;

    @Override
    public String getDefaultImage() {
        return "avatar.jpg";
    }

    @Override
    public List<MultipartFile> getFiles() {
        if (logo == null) return null;
        return List.of(logo);
    }
}
