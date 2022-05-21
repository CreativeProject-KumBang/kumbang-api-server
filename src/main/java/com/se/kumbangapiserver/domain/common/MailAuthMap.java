package com.se.kumbangapiserver.domain.common;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class MailAuthMap implements MailAuthRepository {

    private Map<String, Integer> mailAuthMap = new HashMap<>();


    @Override
    public void saveMailAndCode(String mail, Integer code) {
        log.info("save main and code >> mail : {}, code : {}", mail, code);
        mailAuthMap.put(mail, code);
    }

    @Override
    public Boolean isValidCode(String mail, Integer code) {
        for (String key : mailAuthMap.keySet()) {
            log.info("key :  {}, value : {}", key, mailAuthMap.get(key));
        }
        Boolean equals = mailAuthMap.get(mail).equals(code);
        if (equals) {
            mailAuthMap.remove(mail);
        }
        return equals;
    }
}
