package api.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

@Builder
@Data
public class Author {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("idBook")
    @Expose
    private Integer idBook;

    @SerializedName("firstName")
    @Expose
    @Builder.Default
    private String firstName = RandomStringUtils.randomAlphabetic(100);

    @SerializedName("lastName")
    @Expose
    @Builder.Default
    private String lastName = RandomStringUtils.randomAlphabetic(100);
}