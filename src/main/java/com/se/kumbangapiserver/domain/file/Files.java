package com.se.kumbangapiserver.domain.file;

import com.se.kumbangapiserver.domain.board.BoardFiles;
import com.se.kumbangapiserver.dto.FilesDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "file")
public class Files {
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

    @OneToMany(mappedBy = "files", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardFiles> boardFiles;

    public Long getId() {
        return id;
    }

    public static Files fromDTO(FilesDTO filesDTO) {
        return Files.builder()
                .id(filesDTO.getId())
                .name(filesDTO.getName())
                .path(filesDTO.getPath())
                .size(Long.valueOf(filesDTO.getSize()))
                .type(filesDTO.getType())
                .build();
    }

    public List<BoardFiles> getBoardFiles() {
        return boardFiles;
    }

    public FilesDTO toDTO() {

        return FilesDTO.builder()
                .id(id)
                .name(name)
                .path(path)
                .size(size.toString())
                .type(type)
                .build();
    }
}
