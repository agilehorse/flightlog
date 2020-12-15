package eu.profinit.education.flightlog.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.MediaType;

@Data
@AllArgsConstructor
public class FileExportTo {

    private String fileName;
    private MediaType contentType;
    private byte[] content;
}
