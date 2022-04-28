package com.se.kumbangapiserver.domain.board;

import java.util.List;

public interface RoomBoardRepositoryCustom {

    List<RoomBoard> findByDynamicQuery(RoomBoardSearchQuery query);
}
