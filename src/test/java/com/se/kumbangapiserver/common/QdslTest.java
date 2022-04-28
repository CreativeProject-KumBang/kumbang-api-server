package com.se.kumbangapiserver.common;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.se.kumbangapiserver.domain.user.User;
import com.se.kumbangapiserver.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.se.kumbangapiserver.domain.user.QUser.user;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class QdslTest {

    @Autowired
    private JPAQueryFactory qf;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void testQdsl() {

        userRepository.save(User.builder().id(1L).name("test").email("aaa@email.com").password("123123").build());

        List<User> users = qf
                .selectFrom(user)
                .fetch();

        assertThat(users.size()).isEqualTo(1);
    }
}
