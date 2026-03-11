package com.interviewprep;

import jakarta.persistence.*;

@Entity
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 5000)
    private String explanation;

    @Column(length = 5000)
    private String codeExample;

    @ManyToOne
    @JoinColumn(name = "subtopic_id")
    private SubTopic subTopic;

    public Content() {}

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getCodeExample() {
        return codeExample;
    }

    public SubTopic getSubTopic() {
        return subTopic;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public void setCodeExample(String codeExample) {
        this.codeExample = codeExample;
    }

    public void setSubTopic(SubTopic subTopic) {
        this.subTopic = subTopic;
    }
}