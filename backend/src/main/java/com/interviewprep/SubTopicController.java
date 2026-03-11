package com.interviewprep;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/subtopics")
@CrossOrigin
public class SubTopicController {

    private final SubTopicRepository subTopicRepository;
    private final TopicRepository topicRepository;

    public SubTopicController(SubTopicRepository subTopicRepository,
                              TopicRepository topicRepository) {
        this.subTopicRepository = subTopicRepository;
        this.topicRepository = topicRepository;
    }

    // =========================
    // CREATE SubTopic
    // =========================
    @PostMapping
    public SubTopic addSubTopic(@RequestBody SubTopic subTopic) {

        if (subTopic.getTopic() == null || subTopic.getTopic().getId() == null) {
            throw new RuntimeException("Topic ID must be provided");
        }

        Long topicId = subTopic.getTopic().getId();

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found with id: " + topicId));

        subTopic.setTopic(topic);

        return subTopicRepository.save(subTopic);
    }

    // =========================
    // GET SubTopics (with filter)
    // =========================
    @GetMapping
    public List<SubTopic> getSubTopics(@RequestParam(required = false) Long topicId) {

        if (topicId != null) {
            return subTopicRepository.findByTopicId(topicId);
        }

        return subTopicRepository.findAll();
    }

    // =========================
    // GET By ID
    // =========================
    @GetMapping("/{id}")
    public SubTopic getSubTopicById(@PathVariable Long id) {
        return subTopicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SubTopic not found"));
    }

    // =========================
    // UPDATE
    // =========================
    @PutMapping("/{id}")
    public SubTopic updateSubTopic(@PathVariable Long id,
                                   @RequestBody SubTopic updatedSubTopic) {

        SubTopic subTopic = subTopicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SubTopic not found"));

        subTopic.setName(updatedSubTopic.getName());

        if (updatedSubTopic.getTopic() != null &&
            updatedSubTopic.getTopic().getId() != null) {

            Long topicId = updatedSubTopic.getTopic().getId();

            Topic topic = topicRepository.findById(topicId)
                    .orElseThrow(() -> new RuntimeException("Topic not found"));

            subTopic.setTopic(topic);
        }

        return subTopicRepository.save(subTopic);
    }

    // =========================
    // DELETE
    // =========================
    @DeleteMapping("/{id}")
    public String deleteSubTopic(@PathVariable Long id) {

        if (!subTopicRepository.existsById(id)) {
            throw new RuntimeException("SubTopic not found");
        }

        subTopicRepository.deleteById(id);

        return "SubTopic deleted successfully";
    }
}