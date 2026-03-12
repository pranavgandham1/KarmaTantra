package com.interviewprep;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

@RestController
@RequestMapping("/topics")
@CrossOrigin
public class TopicController {

    private final TopicRepository topicRepository;
    private final SubjectRepository subjectRepository;

    public TopicController(TopicRepository topicRepository,
                           SubjectRepository subjectRepository) {
        this.topicRepository = topicRepository;
        this.subjectRepository = subjectRepository;
    }

    // =========================
    // CREATE Topic (ADMIN ONLY)
    // =========================
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Topic addTopic(@RequestBody Topic topic) {

        if (topic.getSubject() == null || topic.getSubject().getId() == null) {
            throw new RuntimeException("Subject ID must be provided");
        }

        Long subjectId = topic.getSubject().getId();

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + subjectId));

        topic.setSubject(subject);

        return topicRepository.save(topic);
    }

    // =========================
    // GET Topics (USER + ADMIN)
    // =========================
    @GetMapping
    public List<Topic> getTopics(@RequestParam(required = false) Long subjectId) {

        if (subjectId != null) {
            return topicRepository.findBySubjectId(subjectId);
        }

        return topicRepository.findAll();
    }

    // =========================
    // GET Topic By ID (USER + ADMIN)
    // =========================
    @GetMapping("/{id}")
    public Topic getTopicById(@PathVariable Long id) {

        return topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Topic not found with id: " + id));
    }

    // =========================
    // UPDATE Topic (ADMIN ONLY)
    // =========================
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Topic updateTopic(@PathVariable Long id,
                             @RequestBody Topic updatedTopic) {

        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Topic not found with id: " + id));

        topic.setName(updatedTopic.getName());

        if (updatedTopic.getSubject() != null &&
            updatedTopic.getSubject().getId() != null) {

            Long subjectId = updatedTopic.getSubject().getId();

            Subject subject = subjectRepository.findById(subjectId)
                    .orElseThrow(() -> new RuntimeException("Subject not found with id: " + subjectId));

            topic.setSubject(subject);
        }

        return topicRepository.save(topic);
    }

    // =========================
    // DELETE Topic (ADMIN ONLY)
    // =========================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteTopic(@PathVariable Long id) {

        if (!topicRepository.existsById(id)) {
            throw new RuntimeException("Topic not found with id: " + id);
        }

        topicRepository.deleteById(id);

        return "Topic deleted successfully";
    }
}