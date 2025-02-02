package cs.home.shopping.api.v1.common;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface DeleteResourceV1 {

    @DeleteMapping(value = "/by-id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> deleteById(@PathVariable(value = "id") Integer id);

}
