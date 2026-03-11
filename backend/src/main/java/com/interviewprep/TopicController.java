package com.interviewprep;

import org.springframework.web.bind.annotation.*;
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
    // CREATE Topic
    // =========================
    @PostMapping
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
    // GET Topics (With Filtering Support)
    // =========================
    @GetMapping
    public List<Topic> getTopics(@RequestParam(required = false) Long subjectId) {

        if (subjectId != null) {
            return topicRepository.findBySubjectId(subjectId);
        }

        return topicRepository.findAll();
    }

    // =========================
    // GET Topic By ID
    // =========================
    @GetMapping("/{id}")
    public Topic getTopicById(@PathVariable Long id) {

        return topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Topic not found with id: " + id));
    }

    // =========================
    // UPDATE Topic
    // =========================
    @PutMapping("/{id}")
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
    // DELETE Topic
    // =========================
    @DeleteMapping("/{id}")
    public String deleteTopic(@PathVariable Long id) {

        if (!topicRepository.existsById(id)) {
            throw new RuntimeException("Topic not found with id: " + id);
        }

        topicRepository.deleteById(id);

        return "Topic deleted successfully";
    }
}