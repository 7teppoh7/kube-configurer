package ru.sstu.hobbyboard.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.sstu.hobbyboard.entities.User;
import ru.sstu.hobbyboard.entities.dto.interfaces.Imageable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Imageable {

    private String firstName = "unnamed";

    private String lastName = "unnamed";

    private String nickName;

    private String phone = "12345678910";

    private Date birth = Date.valueOf(LocalDate.now());

    private String email;

    private String password;

    private MultipartFile avatar;

    public UserDTO(User user) {
        this.birth = user.getBirth();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.nickName = user.getNickName();
        this.password = user.getPassword();
        this.phone = user.getPhone();
    }

    @Override
    public String getDefaultImage() {
        return "avatar.jpg";
    }

    @Override
    public List<MultipartFile> getFiles() {
        if (avatar == null) return null;
        return List.of(getAvatar());
    }
}
