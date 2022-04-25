package com.se.kumbangapiserver.domain.board;

import com.se.kumbangapiserver.dto.FileDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "file")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "path")
    private String path;

    @Column(name = "size")
    private Long size;

    @Column(name = "type")
    private String type;

    @OneToMany(mappedBy = "file", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardFiles> boardFiles = new ArrayList<>();

    public static File fromDTO(FileDTO fileDTO) {
        return File.builder()
                .id(fileDTO.getId())
                .name(fileDTO.getName())
                .path(fileDTO.getPath())
                .size(Long.valueOf(fileDTO.getSize()))
                .type(fileDTO.getType())
                .build();
    }

    public List<BoardFiles> getBoardFiles() {
        return boardFiles;
    }

    public FileDTO toDTO() {

        return FileDTO.builder()
                .id(id)
                .name(name)
                .path(path)
                .size(size.toString())
                .type(type)
                .build();
    }
}
