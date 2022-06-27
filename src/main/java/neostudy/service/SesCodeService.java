package neostudy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import neostudy.entity.Application;
import neostudy.exception.NoElementException;
import neostudy.exception.SesCodeException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class SesCodeService {

    public Integer generateSesCode() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int x = 0; x < 4; x++) {
            stringBuilder.append((int)(Math.random() * 9) + 1);
        }
        log.debug("generateSesCode(): сгенерирован SES код {}", stringBuilder);
        return Integer.parseInt(stringBuilder.toString());
    }

    public void checkSesCode(Integer sesFromDB, Integer sesFromUser, Long id) throws SesCodeException {

        log.debug("checkSesCode(): ожидаемый SES код: {}", sesFromDB);
        log.debug("checkSesCode(): введённый SES код: {}", sesFromUser);

        if(!Objects.equals(sesFromDB, sesFromUser)) {
            log.debug("checkSesCode(): неверный SES-код");
            throw new SesCodeException("Неверный SES-код!!", id);
        }
    }
}
