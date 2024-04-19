package fr.istic.science.service;

import fr.istic.science.dto.TagDto;
import fr.istic.science.dto.TagListDto;
import fr.istic.science.exception.ResourceNotFoundException;
import fr.istic.science.model.Tag;
import fr.istic.science.model.Theme;
import fr.istic.science.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public TagListDto createTag(Tag tag) {
        Tag t =  tagRepository.save(tag);
        return  convertToTagListDto(t);
    }

    public TagListDto getTagById(Long tagId) {
        Tag t =  tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", tagId));
        return  convertToTagListDto(t);
    }
    public List<TagListDto> getTags() {
        List<TagListDto> list = new ArrayList<>();
        for(Tag t :  tagRepository.findAll()){
            list.add(convertToTagListDto(t));
        }
        return  list;
    }

    public TagListDto updateTag(Long tagId, TagDto tagDetails) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", tagId));
        tag.setTagName(tagDetails.getTagName());

        return convertToTagListDto(tagRepository.save(tag));
    }

    public void deleteTag(Long tagId) {
        tagRepository.deleteById(tagId);
    }

    public static TagListDto convertToTagListDto(Tag tag){
        TagListDto dto = new TagListDto();
        dto.setId(tag.getId());
        dto.setTagName(tag.getTagName());
        dto.setDateCreation(tag.getDateCreation());
        return  dto;
    }

}


