package project3.nutrisubscriptionservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name="form")
public class FormEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id", nullable = false)
    private long resultId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="id", nullable = false)
    private UserEntity user;

    @Column(name="height", nullable = false)
    private int height;

    @Column(name="weight", nullable = false)
    private int weight;

    @Column(name="gender", length = 1, nullable = false)
    private String gender;

    @Column(name="answer1", nullable = false)
    private int answer1;

    @Column(name="answer2", nullable = false)
    private int answer2;

    @Column(name="answer3", nullable = false)
    private int answer3;

    @Column(name="answer4", nullable = false)
    private int answer4;

    @Column(name="answer5", nullable = false)
    private int answer5;

    @Column(name="answer6", nullable = false)
    private int answer6;

    @Column(name="answer7", nullable = false)
    private int answer7;

    @Column(name="answer8", nullable = false)
    private int answer8;

}
