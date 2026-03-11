package com.interviewprep;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubTopicRepository extends JpaRepository<SubTopic, Long> {

    List<SubTopic> findByTopicId(Long topicId);
}