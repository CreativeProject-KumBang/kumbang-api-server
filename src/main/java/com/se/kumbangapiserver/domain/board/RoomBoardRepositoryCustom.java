package com.se.kumbangapiserver.domain.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoomBoardRepositoryCustom {

    List<RoomBoard> findByDynamicQuery(RoomBoardSearchQuery query);

}
