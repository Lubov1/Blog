package ru.yandex.practicum.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dao.Tag;
import ru.yandex.practicum.dto.TagDTOrq;
import ru.yandex.practicum.repositories.TagRepository;

import java.util.List;

@Service
public class TagService {
    final TagRepository TagRepository;

    public TagService(TagRepository TagRepository) {
        this.TagRepository = TagRepository;
    }

    public List<Tag> getTags(Long id) {
        return TagRepository.findAllByPostId(id);
    }

    public Tag convertTagDTOToTag(TagDTOrq tagDTO, Long id) {
        return new Tag(tagDTO.getText(), id);
    }

    @Transactional
    public void saveTag(TagDTOrq tag, Long postId) {
        TagRepository.save(convertTagDTOToTag(tag, postId));
    }
}
