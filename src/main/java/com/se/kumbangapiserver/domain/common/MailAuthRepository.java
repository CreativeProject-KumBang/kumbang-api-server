package com.se.kumbangapiserver.domain.common;

public interface MailAuthRepository {

    void saveMailAndCode(String mail, Integer code);

    Boolean isValidCode(String mail, Integer code);
}
