package eu.profinit.education.flightlog.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileExportTo {

    private String fileName;
    private MediaType contentType;
    private byte[] content;
}
