package api.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AuthorsBooks {

    @SerializedName("authorsBooks")
    @Expose
    private List<Author> authorsBooks;

}