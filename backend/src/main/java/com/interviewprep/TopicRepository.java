package com.interviewprep;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    // 🔥 Filtering by subject
    List<Topic> findBySubjectId(Long subjectId);
}