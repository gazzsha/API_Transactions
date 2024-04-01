package gazssha.client.clienttransaction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import gazssha.client.clienttransaction.utils.Category;
import gazssha.client.clienttransaction.utils.JsonDateTimeDeserialize;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;

public record LimitDto(@NotNull Double limit,
                       @NotNull Category category,
                       @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssX")
                       @JsonDeserialize(using = JsonDateTimeDeserialize.class)
                       ZonedDateTime dateTime) {
}
