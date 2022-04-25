package com.se.kumbangapiserver.domain.board;

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
    private File file;

    public static BoardFiles makeRelation(RoomBoard roomBoard, File file) {

        BoardFiles boardFiles = new BoardFiles();
        boardFiles.setRoomBoard(roomBoard);
        boardFiles.setFile(file);
        return boardFiles;
    }

    public File getFile() {
        return file;
    }

    private void setFile(File file) {
        this.file = file;
        if (file != null) {
            file.getBoardFiles().add(this);
        }
    }

    private void setRoomBoard(RoomBoard roomBoard) {
        this.roomBoard = roomBoard;
        if (roomBoard != null) {
            roomBoard.getBoardFiles().add(this);
        }
    }
}
