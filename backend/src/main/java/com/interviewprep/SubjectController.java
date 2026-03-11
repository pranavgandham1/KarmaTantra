package com.interviewprep;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/subjects")
@CrossOrigin
public class SubjectController {

    private final SubjectRepository subjectRepository;
    private final CategoryRepository categoryRepository;

    public SubjectController(SubjectRepository subjectRepository,
                             CategoryRepository categoryRepository) {
        this.subjectRepository = subjectRepository;
        this.categoryRepository = categoryRepository;
    }

    // =========================
    // CREATE Subject
    // =========================
    @PostMapping
    public Subject addSubject(@RequestBody Subject subject) {

        if (subject.getCategory() == null || subject.getCategory().getId() == null) {
            throw new RuntimeException("Category ID must be provided");
        }

        Long categoryId = subject.getCategory().getId();

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

        subject.setCategory(category);

        return subjectRepository.save(subject);
    }

    // =========================
    // GET All Subjects
    // =========================
    @GetMapping
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    // =========================
    // GET Subject By ID
    // =========================
    @GetMapping("/{id}")
    public Subject getSubjectById(@PathVariable Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));
    }

    // =========================
    // UPDATE Subject
    // =========================
    @PutMapping("/{id}")
    public Subject updateSubject(@PathVariable Long id,
                                 @RequestBody Subject updatedSubject) {

        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));

        subject.setName(updatedSubject.getName());

        if (updatedSubject.getCategory() != null &&
            updatedSubject.getCategory().getId() != null) {

            Long categoryId = updatedSubject.getCategory().getId();

            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

            subject.setCategory(category);
        }

        return subjectRepository.save(subject);
    }

    // =========================
    // DELETE Subject
    // =========================
    @DeleteMapping("/{id}")
    public String deleteSubject(@PathVariable Long id) {

        if (!subjectRepository.existsById(id)) {
            throw new RuntimeException("Subject not found with id: " + id);
        }

        subjectRepository.deleteById(id);
        return "Subject deleted successfully";
    }
}