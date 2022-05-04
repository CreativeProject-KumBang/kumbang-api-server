package com.se.kumbangapiserver.domain.board;

import com.se.kumbangapiserver.domain.file.Files;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "board_files")
public class BoardFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_files_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "board_id", nullable = false)
    private RoomBoard roomBoard;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "file_id", nullable = false)
    private Files files;

    public static BoardFiles makeRelation(RoomBoard roomBoard, Files files) {

        BoardFiles boardFiles = new BoardFiles();
        boardFiles.setRoomBoard(roomBoard);
        boardFiles.setFiles(files);
        return boardFiles;
    }

    public Files getFiles() {
        return files;
    }

    private void setFiles(Files files) {
        this.files = files;
        if (files != null) {
            files.getBoardFiles().add(this);
        }
    }

    private void setRoomBoard(RoomBoard roomBoard) {
        this.roomBoard = roomBoard;
        if (roomBoard != null) {
            roomBoard.getBoardFiles().add(this);
        }
    }
}
