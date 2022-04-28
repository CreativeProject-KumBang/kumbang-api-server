package com.se.kumbangapiserver.domain.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class RoomBoardRepositoryCustomImpl implements RoomBoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<RoomBoard> findByDynamicQuery(RoomBoardSearchQuery query) {
        return null;
    }
}
