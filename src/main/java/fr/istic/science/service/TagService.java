package fr.istic.science.service;

import fr.istic.science.dto.TagDto;
import fr.istic.science.exception.ResourceNotFoundException;
import fr.istic.science.model.Tag;
import fr.istic.science.model.Theme;
import fr.istic.science.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }

    public Tag getTagById(Long tagId) {
        return tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", tagId));
    }
    public List<Tag> getTags() {
        return tagRepository.findAll();
    }

    public Tag updateTag(Long tagId, TagDto tagDetails) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", tagId));

        tag.setTagName(tagDetails.getTagName());

        return tagRepository.save(tag);
    }

    public void deleteTag(Long tagId) {
        tagRepository.deleteById(tagId);
    }
}

