package com.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Table(name = "comment_table")
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommentEntity extends AuditEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int commentId;
    
	 @NotBlank
	 @Column(length = 1000) 
    private String text;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private TicketEntity ticket;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;
    
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentVoteEntity> votes = new ArrayList<>();
    
    @JsonIgnore
    public TicketEntity getTicket() {
        return ticket;
    }

    @JsonIgnore
    public EmployeeEntity getEmployee() {
        return employee;
    }
    
    public void vote(EmployeeEntity employee, VoteType voteType) {
        // Check if the employee has already voted on this comment
        CommentVoteEntity existingVote = null;
        for (CommentVoteEntity vote : votes) {
            if (vote.getEmployee().equals(employee)) {
                existingVote = vote;
                break;
            }
        }

        if (existingVote != null) {
            // If employee already voted, update the vote type
            existingVote.setVoteType(voteType);
        } else {
            // If employee hasn't voted, create a new CommentVoteEntity
            CommentVoteEntity newVote = new CommentVoteEntity(this, employee, voteType);
            votes.add(newVote);
            employee.getCommentVotes().add(newVote); // Associate the vote with the employee
        }
    }
    
//    public List<CommentVoteEntity> getVotes() {
//    	return votes == null ? null : new ArrayList<>(votes);
//    }

    public int getNetVotes() {
        int netVotes = 0;
        for (CommentVoteEntity vote : votes) {
            if (vote.getVoteType() == VoteType.UPVOTE) {
                netVotes++;
            } else {
                netVotes--;
            }
        }
        return netVotes;
    }

    

}
