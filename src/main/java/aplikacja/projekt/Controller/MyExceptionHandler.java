package aplikacja.projekt.Controller;

import org.hibernate.ObjectNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;

@RestControllerAdvice
public class MyExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception ex){
        if(ex instanceof ObjectNotFoundException){

            HashMap<String, String> errors = new HashMap<>();
            errors.put("NOT_FOUND", ((ObjectNotFoundException) ex).getEntityName());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
        }
        else if(ex instanceof DuplicateKeyException){
            HashMap<String, String> errors = new HashMap<>();
            errors.put("BAD_REQUEST", ex.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
