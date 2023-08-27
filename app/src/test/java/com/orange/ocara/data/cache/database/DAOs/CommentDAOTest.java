package com.orange.ocara.data.cache.database.DAOs;

import com.orange.ocara.data.cache.database.Tables.Comment;
import com.orange.ocara.utils.enums.CommentType;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CommentDAOTest extends DAOTest {

    @Test
    public void updateCommentById() {
        Comment comment1 = new Comment(CommentType.TEXT, "date1", "attach1", "comment1");
        Comment comment2 = new Comment(CommentType.TEXT, "date2", "attach2", "comment2");
        comment1.setAudit_id(1);
        comment2.setAudit_id(1);
        commentDAO.insert(comment1).blockingGet();
        commentDAO.insert(comment2).blockingGet();
        commentDAO.updateCommentById(1, "comment1Updated", "date1Updated", "attach1Updated").blockingAwait();
        List<Comment> auditComments = commentDAO.getAuditComments(1).blockingGet();
        Assert.assertEquals(auditComments.size(), 2);

        // we don't know the order in which the comments where retrieved , so I make comnt1 be the comment with id=1 which is comment1
        // in the first line
        Comment comnt1 = auditComments.get(0).getId() == 1 ? auditComments.get(0) : auditComments.get(1);
        Comment comnt2 = auditComments.get(0).getId() == 2 ? auditComments.get(0) : auditComments.get(1);
        Assert.assertEquals(comnt1.getContent(), "comment1Updated");
        Assert.assertEquals(comnt1.getAttachment(), "attach1Updated");
        Assert.assertEquals(comnt1.getDate(), "date1Updated");

        Assert.assertEquals(comnt2.getContent(), "comment2");
        Assert.assertEquals(comnt2.getAttachment(), "attach2");
        Assert.assertEquals(comnt2.getDate(), "date2");
    }

    @Test
    public void getAuditComments() {
        Comment comment1 = new Comment(CommentType.TEXT, "", "", "comment1");
        Comment comment2 = new Comment(CommentType.TEXT, "", "", "comment2");
        Comment comment3 = new Comment(CommentType.TEXT, "", "", "comment3");
        comment1.setAudit_id(1);
        comment2.setAudit_id(1);
        comment3.setAudit_id(2);
        commentDAO.insert(comment1).blockingGet();
        commentDAO.insert(comment2).blockingGet();
        commentDAO.insert(comment3).blockingGet();
        List<Comment> audit1Comments = commentDAO.getAuditComments(1).blockingGet();
        List<Comment> audit2Comments = commentDAO.getAuditComments(2).blockingGet();

        Assert.assertEquals(audit1Comments.size(), 2);
        Assert.assertEquals(audit2Comments.size(), 1);

        Comment comnt1 = audit1Comments.get(0).getId() == 1 ? audit1Comments.get(0) : audit1Comments.get(1);
        Comment comnt2 = audit1Comments.get(0).getId() == 2 ? audit1Comments.get(0) : audit1Comments.get(1);

        Assert.assertEquals(comnt1.getContent(), "comment1");
        Assert.assertEquals(comnt2.getContent(), "comment2");
        Assert.assertEquals(audit2Comments.get(0).getContent(), "comment3");
    }

    @Test
    public void getAuditObjectComments() {
        Comment comment1 = new Comment(CommentType.TEXT, "", "", "comment1");
        Comment comment2 = new Comment(CommentType.TEXT, "", "", "comment2");
        Comment comment3 = new Comment(CommentType.TEXT, "", "", "comment3");
        comment1.setAudit_equipment_id(1);
        comment2.setAudit_equipment_id(1);
        comment3.setAudit_equipment_id(2);
        commentDAO.insert(comment1).blockingGet();
        commentDAO.insert(comment2).blockingGet();
        commentDAO.insert(comment3).blockingGet();
        List<Comment> audit1Comments = commentDAO.getAuditObjectComments(1L).blockingGet();
        List<Comment> audit2Comments = commentDAO.getAuditObjectComments(2L).blockingGet();

        Assert.assertEquals(audit1Comments.size(), 2);
        Assert.assertEquals(audit2Comments.size(), 1);

        Comment comnt1 = audit1Comments.get(0).getId() == 1 ? audit1Comments.get(0) : audit1Comments.get(1);
        Comment comnt2 = audit1Comments.get(0).getId() == 2 ? audit1Comments.get(0) : audit1Comments.get(1);

        Assert.assertEquals(comnt1.getContent(), "comment1");
        Assert.assertEquals(comnt2.getContent(), "comment2");
        Assert.assertEquals(audit2Comments.get(0).getContent(), "comment3");
    }

    @Test
    public void deleteSingleComment() {
        Comment comment1 = new Comment(CommentType.TEXT, "", "", "comment1");
        Comment comment2 = new Comment(CommentType.TEXT, "", "", "comment2");
        comment1.setAudit_equipment_id(1);
        comment2.setAudit_equipment_id(1);
        commentDAO.insert(comment1).blockingGet();
        commentDAO.insert(comment2).blockingGet();
        commentDAO.deleteSingleComment(1).blockingAwait();
        List<Comment> audit1Comments = commentDAO.getAuditObjectComments(1L).blockingGet();

        Assert.assertEquals(audit1Comments.size(), 1);

        Comment comnt1 = audit1Comments.get(0);
        Assert.assertEquals(comnt1.getContent(), "comment2");
    }

    @Test
    public void deleteAllEquipmentComments() {
        Comment comment1 = new Comment(CommentType.TEXT, "", "", "comment1");
        Comment comment2 = new Comment(CommentType.TEXT, "", "", "comment2");
        Comment comment3 = new Comment(CommentType.TEXT, "", "", "comment3");
        comment1.setAudit_equipment_id(1);
        comment2.setAudit_equipment_id(1);
        comment3.setAudit_equipment_id(2);
        commentDAO.insert(comment1).blockingGet();
        commentDAO.insert(comment2).blockingGet();
        commentDAO.insert(comment3).blockingGet();
        commentDAO.deleteAllEquipmentComments(1).blockingAwait();
        List<Comment> audit1Comments = commentDAO.getAuditObjectComments(1L).blockingGet();
        List<Comment> audit2Comments = commentDAO.getAuditObjectComments(2L).blockingGet();

        Assert.assertEquals(audit1Comments.size(), 0);
        Assert.assertEquals(audit2Comments.size(), 1);

        Assert.assertEquals(audit2Comments.get(0).getContent(), "comment3");
    }

    @Test
    public void deleteAllAuditComments() {
        Comment comment1 = new Comment(CommentType.TEXT, "", "", "comment1");
        Comment comment2 = new Comment(CommentType.TEXT, "", "", "comment2");
        Comment comment3 = new Comment(CommentType.TEXT, "", "", "comment3");
        comment1.setAudit_id(1);
        comment2.setAudit_id(1);
        comment3.setAudit_id(2);
        commentDAO.insert(comment1).blockingGet();
        commentDAO.insert(comment2).blockingGet();
        commentDAO.insert(comment3).blockingGet();
        commentDAO.deleteAllAuditComments(1).blockingAwait();
        List<Comment> audit1Comments = commentDAO.getAuditComments(1).blockingGet();
        List<Comment> audit2Comments = commentDAO.getAuditComments(2).blockingGet();

        Assert.assertEquals(audit1Comments.size(), 0);
        Assert.assertEquals(audit2Comments.size(), 1);

        Assert.assertEquals(audit2Comments.get(0).getContent(), "comment3");
    }

    @Test
    public void getCommentById() {
        Comment comment1 = new Comment(CommentType.TEXT, "date1", "attach1", "comment1");
        Comment comment2 = new Comment(CommentType.TEXT, "date2", "attach2", "comment2");
        commentDAO.insert(comment1).blockingGet();
        commentDAO.insert(comment2).blockingGet();
        Comment comnt1 = commentDAO.getCommentById(1).blockingGet();
        Comment comnt2 = commentDAO.getCommentById(2).blockingGet();

        Assert.assertEquals(comnt1.getContent(), "comment1");
        Assert.assertEquals(comnt1.getAttachment(), "attach1");
        Assert.assertEquals(comnt1.getDate(), "date1");

        Assert.assertEquals(comnt2.getContent(), "comment2");
        Assert.assertEquals(comnt2.getAttachment(), "attach2");
        Assert.assertEquals(comnt2.getDate(), "date2");
    }
}