package fr.istic.science.service;

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
    // Other CRUD operations for Tag
}

