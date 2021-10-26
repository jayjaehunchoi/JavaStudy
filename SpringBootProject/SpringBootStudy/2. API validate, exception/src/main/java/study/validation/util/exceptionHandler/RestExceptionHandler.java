package study.validation.util.exceptionHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import study.validation.dto.ErrorResponseDto;
import study.validation.dto.ResponseDto;
import study.validation.util.enumeration.ValidCode;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validErrorHandler(MethodArgumentNotValidException e){
        log.error("[validErrorHandler]", e);
        List<ErrorResponseDto> errorResponseDtos = makeErrorResponseDtoList(e.getBindingResult());
        return ResponseEntity.badRequest().body(errorResponseDtos);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public ResponseDto runTimeExHandler(RuntimeException e){
        log.error("[runTimeException] error message = {}",e.getMessage());
        String message = e.getMessage() == null ? "런타임 에러" : e.getMessage();

        ResponseDto<Object> dto = ResponseDto.builder()
                .error(message)
                .build();
        return dto;
    }

    private List<ErrorResponseDto> makeErrorResponseDtoList(BindingResult bindingResult){
        List<ErrorResponseDto> errorResponseDtos = new ArrayList<>();
        if(bindingResult.hasErrors()){
            List<ObjectError> errors = bindingResult.getAllErrors();
            for (ObjectError error : errors) {
                ErrorResponseDto errorResponse = createErrorResponse((FieldError) error);
                errorResponseDtos.add(errorResponse);
            }

        }
        return errorResponseDtos;
    }

    private ErrorResponseDto createErrorResponse(FieldError fieldError){
        String errorCode = fieldError.getCode();

        String code = getCodeName(errorCode);
        String field = fieldError.getField();
        String defaultMessage = fieldError.getDefaultMessage();

        return new ErrorResponseDto(code,field,defaultMessage);
    }

    private String getCodeName(String errorCode){
        log.info("[Error Code] = {}",errorCode);
        String codeName = "";
        switch (errorCode){
            case "NotBlank":
                codeName = ValidCode.NOT_BLANK.getCode();
                break;
            case "NotNull":
                codeName = ValidCode.NOT_NULL.getCode();
                break;
            case "Pattern":
                codeName = ValidCode.PATTERN.getCode();
                break;
            case "Max":
                codeName = ValidCode.MAX.getCode();
                break;
            case "Email":
                codeName = ValidCode.EMAIL.getCode();
                break;
        }
        return codeName;
    }
}
