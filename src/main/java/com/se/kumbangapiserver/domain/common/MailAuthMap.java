package com.se.kumbangapiserver.domain.common;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@NoArgsConstructor
@AllArgsConstructor
public class MailAuthMap implements MailAuthRepository {

    private Map<String, Integer> mailAuthMap = new HashMap<>();


    @Override
    public void saveMailAndCode(String mail, Integer code) {
        mailAuthMap.put(mail, code);
    }

    @Override
    public Boolean isValidCode(String mail, Integer code) {
        return mailAuthMap.get(mail).equals(code);
    }
}
