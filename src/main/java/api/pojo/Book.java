package api.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@ToString
public class Book {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    @Builder.Default
    private String title = RandomStringUtils.randomAlphabetic(100);

    @SerializedName("description")
    @Expose
    @Builder.Default
    private String description = RandomStringUtils.randomAlphabetic(100);

    @SerializedName("pageCount")
    @Expose
    @Builder.Default
    private Integer pageCount = RandomUtils.nextInt(1, 100);

    @SerializedName("excerpt")
    @Expose
    @Builder.Default
    private String excerpt = RandomStringUtils.randomAlphabetic(100);

    @SerializedName("publishDate")
    @Expose
    @Builder.Default
    private String publishDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
}