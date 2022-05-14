package com.se.kumbangapiserver.domain.board;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class RoomBoardRepositoryCustomImpl implements RoomBoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<RoomBoard> findByDynamicQuery(RoomBoardSearchQuery query) {
        QRoomBoard roomBoard = QRoomBoard.roomBoard;

        return queryFactory.selectFrom(roomBoard)
                .where(
                        roomBoard.cordX.between(query.getMinCordX(), query.getMaxCordX()),
                        roomBoard.cordY.between(query.getMinCordY(), query.getMaxCordY()),
                        durationType(query),
                        durationStart(query),
                        durationEnd(query),
                        priceStart(query),
                        priceEnd(query)
                )
                .fetch();
    }

    BooleanExpression durationType(RoomBoardSearchQuery query) {
        if (query.getDurationType() == null) {
            return null;
        }
        return QRoomBoard.roomBoard.durationTerm.eq(DurationTerm.valueOf(query.getDurationType()));
    }

    BooleanExpression durationStart(RoomBoardSearchQuery query) {
        if (query.getDurationStart() == null) {
            return null;
        }
        return QRoomBoard.roomBoard.durationStart.before(query.getDurationStart());
    }

    BooleanExpression durationEnd(RoomBoardSearchQuery query) {
        if (query.getDurationEnd() == null) {
            return null;
        }
        return QRoomBoard.roomBoard.durationEnd.after(query.getDurationEnd());
    }

    BooleanExpression priceStart(RoomBoardSearchQuery query) {
        if (query.getPriceStart() == null) {
            return null;
        }
        return QRoomBoard.roomBoard.price.goe(query.getPriceStart());
    }

    BooleanExpression priceEnd(RoomBoardSearchQuery query) {
        if (query.getPriceEnd() == null) {
            return null;
        }
        return QRoomBoard.roomBoard.price.loe(query.getPriceEnd());
    }

}
