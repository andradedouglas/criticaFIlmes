package com.api.config.validacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class TratamentoErro {
    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<FormularioErroDTO> handle(MethodArgumentNotValidException exception) {
        List<FormularioErroDTO> dto = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        fieldErrors.forEach(e -> {
            String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            FormularioErroDTO erro = new FormularioErroDTO(e.getField(), mensagem);
            dto.add(erro);
        });

        return dto;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<MensagemErro> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        MensagemErro message = new MensagemErro(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<MensagemErro>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MensagemErro> globalExceptionHandler(Exception ex, WebRequest request) {
        MensagemErro message = new MensagemErro(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<MensagemErro>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity <MensagemErro> notFoundException(final NullPointerException e) {
        MensagemErro message = new MensagemErro(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                "Valor nulo",
                "Valor nulo encontrado");

        return new ResponseEntity<MensagemErro>(message, HttpStatus.NOT_FOUND);
    }


}
