package ru.sstu.hobbyboard.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.sstu.hobbyboard.entities.PhotoProduct;
import ru.sstu.hobbyboard.entities.Product;
import ru.sstu.hobbyboard.entities.dto.interfaces.Imageable;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDTO implements Imageable {

    @NotNull
    private PhotoProduct mainPhoto;

    private List<PhotoProduct> photoToDel;

    private List<MultipartFile> newPhotos;

    public ImageDTO(Product product) {
        mainPhoto = product.getMainPhoto();
    }

    @Override
    public String getDefaultImage() {
        return "product.jpg";
    }

    @Override
    public List<MultipartFile> getFiles() {
        return newPhotos;
    }
}
