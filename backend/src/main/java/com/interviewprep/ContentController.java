package com.interviewprep;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

@RestController
@RequestMapping("/contents")
public class ContentController {

    private final ContentRepository contentRepository;
    private final SubTopicRepository subTopicRepository;

    public ContentController(ContentRepository contentRepository,
                             SubTopicRepository subTopicRepository) {
        this.contentRepository = contentRepository;
        this.subTopicRepository = subTopicRepository;
    }

    // =========================
    // CREATE (ADMIN ONLY)
    // =========================
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Content addContent(@RequestBody Content content) {

        if (content.getSubTopic() != null) {
            Long subTopicId = content.getSubTopic().getId();

            SubTopic subTopic = subTopicRepository.findById(subTopicId)
                    .orElseThrow(() -> new RuntimeException("SubTopic not found"));

            content.setSubTopic(subTopic);
        }

        return contentRepository.save(content);
    }

    // =========================
    // GET ALL (USER + ADMIN)
    // =========================
    @GetMapping
    public List<Content> getAllContents() {
        return contentRepository.findAll();
    }

    // =========================
    // GET BY SUBTOPIC (USER + ADMIN)
    // =========================
    @GetMapping(params = "subTopicId")
    public List<Content> getContentsBySubTopic(@RequestParam Long subTopicId) {
        return contentRepository.findBySubTopicId(subTopicId);
    }

    // =========================
    // GET BY ID (USER + ADMIN)
    // =========================
    @GetMapping("/{id}")
    public Content getContentById(@PathVariable Long id) {
        return contentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Content not found"));
    }

    // =========================
    // UPDATE (ADMIN ONLY)
    // =========================
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Content updateContent(@PathVariable Long id,
                                 @RequestBody Content updatedContent) {

        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Content not found"));

        content.setTitle(updatedContent.getTitle());
        content.setExplanation(updatedContent.getExplanation());
        content.setCodeExample(updatedContent.getCodeExample());

        return contentRepository.save(content);
    }

    // =========================
    // DELETE (ADMIN ONLY)
    // =========================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteContent(@PathVariable Long id) {

        if (!contentRepository.existsById(id)) {
            throw new RuntimeException("Content not found");
        }

        contentRepository.deleteById(id);

        return "Content deleted successfully";
    }
}