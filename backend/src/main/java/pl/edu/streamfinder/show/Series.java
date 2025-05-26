package pl.edu.streamfinder.show;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.TypeAlias;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@TypeAlias("series")
public class Series extends Show {
    private int firstAirYear;
    private int lastAirYear;
    private List<Season> seasons;
}
