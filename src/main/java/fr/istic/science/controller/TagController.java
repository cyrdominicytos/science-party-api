package fr.istic.science.controller;

import fr.istic.science.dto.TagDto;
import fr.istic.science.dto.TagListDto;
import fr.istic.science.exception.ResourceNotFoundException;
import fr.istic.science.model.Tag;
import fr.istic.science.model.Theme;
import fr.istic.science.model.User;
import fr.istic.science.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @PostMapping
    public ResponseEntity<Object> createTag(@RequestBody TagDto tag) {
        Tag t = new Tag();
        t.setTagName(tag.getTagName());
        TagListDto createdTag = tagService.createTag(t);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTag);
    }

    @GetMapping("/{tagId}")
    public ResponseEntity<Object> getTagById(@PathVariable Long tagId) {
        try{
            TagListDto tag = tagService.getTagById(tagId);
            return ResponseEntity.status(HttpStatus.OK).body(tag);
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Le tag avec l'identifiant "+tagId+" n'existe pas !");
        }
    }
    @GetMapping("")
    public ResponseEntity<Object> getTags() {
        List<TagListDto> tags = tagService.getTags();
        return ResponseEntity.status(HttpStatus.OK).body(tags);
    }

    @PutMapping("/{tagId}")
    public ResponseEntity<Object> updateTag(@PathVariable Long tagId, @RequestBody TagDto tagDetails) {
        TagListDto updatedTag = tagService.updateTag(tagId, tagDetails);
        return new ResponseEntity<>(updatedTag, HttpStatus.OK);
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long tagId) {
        tagService.deleteTag(tagId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

