/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model.export.docx;

import com.orange.ocara.modelStatic.Accessibility;
import com.orange.ocara.model.Audit;
import com.orange.ocara.model.AuditObject;
import com.orange.ocara.model.Auditor;
import com.orange.ocara.model.Comment;
import com.orange.ocara.model.ModelUtils;
import com.orange.ocara.modelStatic.ObjectDescription;
import com.orange.ocara.modelStatic.Question;
import com.orange.ocara.model.QuestionAnswer;
import com.orange.ocara.modelStatic.Response;
import com.orange.ocara.modelStatic.Rule;
import com.orange.ocara.model.RuleAnswer;
import com.orange.ocara.modelStatic.RuleSet;
import com.orange.ocara.model.Site;

import org.apache.commons.io.FileUtils;
import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.mockito.Matchers.any;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", resourceDir = "../../build/intermediates/res/debug", emulateSdk = 18)
public class AuditDocxExporterTest {

    @org.junit.Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private File templateDirectory;
    private File workingDirectory;

    private Audit audit;
    private RuleSet ruleSet;
    private Auditor author;
    private Site site;




    @Before
    public void setUp() throws Exception {
        templateDirectory = new File("src/main/assets/export/docx");
        workingDirectory = testFolder.newFolder("working");

        FileUtils.copyDirectory(templateDirectory, workingDirectory);

        audit = new Audit();

        ruleSet = new RuleSet();
        author = new Auditor();
        site = new Site();

        audit.setName("");
        audit.setRuleSet(ruleSet);
        audit.setAuthor(author);
        audit.setSite(site);
        audit.setDate(Calendar.getInstance().getTime());
    }

    @Test
    public void compile_With_Audit_Details() throws DocxExporterException, IOException {

        // Given
        final String expectedAuditLevel = "Expert";
        final String expectedDate = "16 janvier 2015";

        ruleSet.setType("audit de parcours");
        author.setUserName("Alexis Chemin");
        site.setNoImmo("935001");
        site.setName("Pantin");
        Date date = new GregorianCalendar(2015, 0, 16).getTime();

        audit.setName("test audit");
        audit.setLevel(Audit.Level.EXPERT);
        audit.setDate(date);

        // When
        buildAndCompileAuditDocxExporter();

        // Then
        File document = new File(workingDirectory, "word/document.xml");
        final String content = FileUtils.readFileToString(document, "UTF-8");
        Assertions.assertThat(content).contains(audit.getName());
        Assertions.assertThat(content).contains(ruleSet.getType());

        Assertions.assertThat(content).contains(site.getNoImmo());
        Assertions.assertThat(content).contains(site.getName());
        Assertions.assertThat(content).contains(author.getUserName());
        Assertions.assertThat(content).contains(expectedDate);
    }

    @Test
    public void compile_With_Audit_Having_Anomalies() throws Exception {
        // Given
        String expectedQuestionTitle = "Porte monnaie : contient des pennies";

        AuditObject auditObject = buildAuditObjectWithAtLeastOneRuleHavingResponse(Response.NOK, expectedQuestionTitle);
        audit.getObjects().add(auditObject);

        // When
        buildAndCompileAuditDocxExporter();

        // Then
        File document = new File(workingDirectory, "word/document.xml");
        final String content = FileUtils.readFileToString(document, "UTF-8");

        Assertions.assertThat(content).contains(auditObject.getName());
        Assertions.assertThat(content).contains(expectedQuestionTitle);
    }

    @Test
    public void compile_With_Audit_Having_Doubts() throws Exception {
        // Given
        String expectedQuestionTitle = "Porte monnaie : contient des pennies";

        AuditObject auditObject = buildAuditObjectWithAtLeastOneRuleHavingResponse(Response.DOUBT, expectedQuestionTitle);
        audit.getObjects().add(auditObject);

        // When
        buildAndCompileAuditDocxExporter();

        // Then
        File document = new File(workingDirectory, "word/document.xml");
        final String content = FileUtils.readFileToString(document, "UTF-8");

        Assertions.assertThat(content).contains(auditObject.getName());
        Assertions.assertThat(content).contains(expectedQuestionTitle);
    }


    @Test
    public void compile_With_Audit_Comments() throws Exception {
        // Given

        File photo = new File("src/test/resources/photo/20140522165044.png");
        Comment comment = new Comment();
        comment.setType(Comment.Type.TEXT);
        comment.setAudit(audit);
        comment.setContent("content-audit-comment");
        Comment commentPhoto = new Comment();
        commentPhoto.setType(Comment.Type.PHOTO);
        commentPhoto.setAttachment(photo.toURI().toString());
        commentPhoto.setContent("content-audit-comment-photo");

        audit.getComments().add(comment);
        audit.getComments().add(commentPhoto);

        // When
        buildAndCompileAuditDocxExporter();

        // Then
        File document = new File(workingDirectory, "word/document.xml");
        final String content = FileUtils.readFileToString(document, "UTF-8");

        Assertions.assertThat(content).contains(comment.getContent());
    }


    @Test
    public void compile_With_AuditObject_Comments() throws Exception {
        // Given

        AuditObject auditObject = buildAuditObjectWithAtLeastOneRuleHavingResponse(Response.NOK, "", "", "", buildObjectDescription("A"));
        auditObject.setAudit(audit);
        auditObject.setName("porte monnaie");

        ModelUtils.setId(auditObject, 777L);

        Comment comment = new Comment();
        comment.setType(Comment.Type.TEXT);
        comment.setAuditObject(auditObject);
        comment.setContent("content-auditobject-comment");

        auditObject.getComments().add(comment);
        audit.getObjects().add(auditObject);
        // When
        buildAndCompileAuditDocxExporter();

        // Then
        File document = new File(workingDirectory, "word/document.xml");
        final String content = FileUtils.readFileToString(document, "UTF-8");

        Assertions.assertThat(content).contains(auditObject.getId().toString());
        Assertions.assertThat(content).contains(auditObject.getName());
        Assertions.assertThat(content).contains(comment.getContent());
    }

    @Test
    public void compile_With_N_AuditObjects_Should_Generate_Path_With_Title_Containing_N() throws Exception {

        // Given
        for (int i = 0; i < 10; i++) {
            audit.getObjects().add(buildAuditObjectWithObjectDescription(Response.OK, buildObjectDescription("A")));
        }

        // When
        buildAndCompileAuditDocxExporter();

        // Then
        String content = FileUtils.readFileToString(new File(workingDirectory, "word/document.xml"), "UTF-8");

        Assertions.assertThat(content).contains(String.format("%d élément(s) audité(s)", audit.getObjects().size()));
    }

    @Test
    public void compile_With_AuditObjects_Having_OK_AuditObject_Should_Generate_Path() throws Exception {

        checkAuditPathColor(Response.OK, "259B24");
    }

    @Test
    public void compile_With_AuditObjects_Having_NOK_AuditObject_Should_Generate_Path() throws Exception {

        checkAuditPathColor(Response.NOK, "DD2C00");
    }

    @Test
    public void compile_With_AuditObjects_Having_NoAnswer_AuditObject_Should_Generate_Path() throws Exception {

        checkAuditPathColor(Response.NoAnswer, "FFFFFF");
    }

    private void checkAuditPathColor(Response response, String color) throws Exception {
        // Given
        AuditObject auditObject = buildAuditObjectWithAtLeastOneRuleHavingResponse(response, "", "", "", buildObjectDescription("A"));
        audit.getObjects().add(auditObject);

        // When
        buildAndCompileAuditDocxExporter();

        // Then
        String content = FileUtils.readFileToString(new File(workingDirectory, "word/document.xml"), "UTF-8");

        //Assertions.assertThat(content).contains(String.format("<w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"%s\"/>", color)); // OK
        Assertions.assertThat(content).contains(String.format("<a:srgbClr val=\"%s\" />", color));
        Assertions.assertThat(content).contains(String.format("r:embed=\"icon_777\">", auditObject.getId()));

        content = FileUtils.readFileToString(new File(workingDirectory, "word/[_]rels/document.xml.rels"), "UTF-8");

        Assertions.assertThat(content).contains(String.format("<Relationship Id=\"icon_%d\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/image\" Target=\"media/icon/A.png\"/>", auditObject.getId())); // OK
    }



    @Test
    public void compile_With_AuditObject_Having_Anomaly_Should_Generate_Report_With_Anomaly() throws Exception {

        // Given
        AuditObject auditObject = buildAuditObjectWithAtLeastOneRuleHavingResponse(Response.NOK, "question", "ruleId", "rule description", buildObjectDescription("A"));
        audit.getObjects().add(auditObject);

        // When
        buildAndCompileAuditDocxExporter();

        // Then
        String content = FileUtils.readFileToString(new File(workingDirectory, "word/document.xml"), "UTF-8");

        Assertions.assertThat(content).contains("ruleId");
        Assertions.assertThat(content).contains("rule description");
        Assertions.assertThat(content).contains("KO");
    }

    @Test
    public void compile_With_AuditObject_Having_Doubt_Should_Generate_Report_With_Doubt() throws Exception {

        // Given
        AuditObject auditObject = buildAuditObjectWithAtLeastOneRuleHavingResponse(Response.DOUBT, "question", "ruleId", "rule description", buildObjectDescription("A"));
        audit.getObjects().add(auditObject);

        // When
        buildAndCompileAuditDocxExporter();

        // Then
        String content = FileUtils.readFileToString(new File(workingDirectory, "word/document.xml"), "UTF-8");

        Assertions.assertThat(content).contains("ruleId");
        Assertions.assertThat(content).contains("rule description");
        Assertions.assertThat(content).contains("doute");
    }

    @Test
    public void compile_With_AuditObject_Having_Text_Comment_Should_Generate_Report_With_Text_Comment() throws Exception {

        // Given
        Comment comment = new Comment();
        comment.setType(Comment.Type.TEXT);
        comment.setContent("comment");

        AuditObject auditObject = buildAuditObjectWithAtLeastOneRuleHavingResponse(Response.NOK, "");
        auditObject.getComments().add(comment);

        audit.getObjects().add(auditObject);

        // When
        buildAndCompileAuditDocxExporter();

        // Then
        String content = FileUtils.readFileToString(new File(workingDirectory, "word/document.xml"), "UTF-8");

        Assertions.assertThat(content).contains(comment.getContent());
    }

    @Test
    public void compile_With_AuditObject_Having_Photo_Comment_Should_Generate_Report_With_Photo_Comment() throws Exception {

        // Given
        File photo = new File("src/test/resources/photo/20140522165044.png");

        Comment comment = new Comment();
        comment.setType(Comment.Type.PHOTO);
        comment.setAttachment(photo.toURI().toString());
        comment.setContent("comment");

        ModelUtils.setId(comment, 777L);

        AuditObject auditObject = buildAuditObjectWithAtLeastOneRuleHavingResponse(Response.NOK, "");
        auditObject.getComments().add(comment);

        audit.getObjects().add(auditObject);

        // When
        buildAndCompileAuditDocxExporter();

        // Then

        String content = FileUtils.readFileToString(new File(workingDirectory, "word/document.xml"), "UTF-8");

        Assertions.assertThat(content).contains(String.format("<a:blip cstate=\"print\" r:embed=\"comment_%d\">", comment.getId()));

        content = FileUtils.readFileToString(new File(workingDirectory, "word/[_]rels/document.xml.rels"), "UTF-8");

        Assertions.assertThat(content).contains(String.format("<Relationship Id=\"comment_%d\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/image\" Target=\"media/comment/%s\"/>", comment.getId(), photo.getName())); // OK
        Assertions.assertThat(new File(workingDirectory, String.format("word/media/comment/%s", photo.getName()))).exists();
    }


    @Test
    public void compile_With_AuditObject_Having_Audio_Comment_Should_Generate_Report_With_Audio_Comment() throws Exception {

        // Given
        File photo = new File("src/test/resources/audio/A.3GP");

        Comment comment = new Comment();
        comment.setType(Comment.Type.AUDIO);
        comment.setAttachment(photo.toURI().toString());
        comment.setContent("comment");

        ModelUtils.setId(comment, 777L);

        AuditObject auditObject = buildAuditObjectWithAtLeastOneRuleHavingResponse(Response.NOK, "");
        auditObject.getComments().add(comment);

        audit.getObjects().add(auditObject);

        // When
        buildAndCompileAuditDocxExporter();

        // Then

        String content = FileUtils.readFileToString(new File(workingDirectory, "word/document.xml"), "UTF-8");

        Assertions.assertThat(content).contains(String.format("<o:OLEObject Type=\"Embed\" ProgID=\"Package\" ShapeID=\"_x0000_i1%d\" DrawAspect=\"Content\" ObjectID=\"_14829135%d\" r:id=\"comment_%d\"/>", comment.getId(), comment.getId(), comment.getId()));

        content = FileUtils.readFileToString(new File(workingDirectory, "word/[_]rels/document.xml.rels"), "UTF-8");

        Assertions.assertThat(content).contains(String.format("<Relationship Id=\"comment_%d\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/oleObject\" Target=\"media/comment/A.bin\"/>", comment.getId())); // OK
        Assertions.assertThat(new File(workingDirectory, "word/media/comment/A.bin")).exists();
    }

    @Test
    public void compile_With_Audit_Should_Generate_Chart() throws Exception {

        // Given
        final String expectedAuditLevel = "Expert";
        final String expectedDate = "vendredi 16 janvier 2015";

        ruleSet.setType("audit de parcours");
        author.setUserName("Alexis Chemin");
        site.setNoImmo("935001");
        site.setName("Pantin");
        Date date = new GregorianCalendar(2015, 0, 16).getTime();

        audit.setName("test audit");
        audit.setLevel(Audit.Level.EXPERT);
        audit.setDate(date);

        // When
        buildAndCompileAuditDocxExporter();

        // Then
        String content = FileUtils.readFileToString(new File(workingDirectory, "word/document.xml"), "UTF-8");

        Assertions.assertThat(content).contains("r:id=\"rId23\" />");

    }

    @Test
    public void compile_With_Audit_Should_Generate_Chart_With_Stat() throws Exception {

        // Given
        final String expectedAuditLevel = "Expert";
        final String expectedDate = "vendredi 16 janvier 2015";

        ruleSet.setType("audit de parcours");
        author.setUserName("Alexis Chemin");
        site.setNoImmo("935001");
        site.setName("Pantin");
        Date date = new GregorianCalendar(2015, 0, 16).getTime();

        audit.setName("test audit");
        audit.setLevel(Audit.Level.EXPERT);
        audit.setDate(date);

        // When
        buildAndCompileAuditDocxExporter();

        // Then
        String content = FileUtils.readFileToString(new File(workingDirectory, "word/document.xml"), "UTF-8");

        Assertions.assertThat(content).contains("r:id=\"rId23\" />");

    }


    private AuditObject buildAuditObjectWithAtLeastOneRuleHavingResponse(Response response, String questionTitle) throws NoSuchFieldException, IllegalAccessException, URISyntaxException {
        return buildAuditObjectWithAtLeastOneRuleHavingResponse(response, questionTitle, "", "", buildObjectDescription("A"));
    }

    private AuditObject buildAuditObjectWithAtLeastOneRuleHavingResponse(Response response, String questionTitle, String ruleId, String ruleDescription, ObjectDescription objectDescription) throws NoSuchFieldException, IllegalAccessException, URISyntaxException {
        QuestionAnswer answer = Mockito.spy(new QuestionAnswer());
        AuditObject auditObject = buildAuditObjectWithObjectDescription(response, objectDescription);
        auditObject.getQuestionAnswers().add(answer);

        ModelUtils.setId(auditObject, 777L);

        answer.setAuditObject(auditObject);
        Question question = new Question();
        question.setTitle(questionTitle);
        Mockito.doReturn(question).when(answer).getQuestion();

        Rule rule = Mockito.spy(new Rule());
        rule.setId(ruleId);
        rule.setDescription(ruleDescription);
        Mockito.doReturn(false).when(rule).hasAccessibility(any(Accessibility.class));

        RuleAnswer ruleAnswer = Mockito.spy(new RuleAnswer());
        answer.getRuleAnswers().add(ruleAnswer);
        ruleAnswer.setQuestionAnswer(answer);
        ruleAnswer.updateResponse(response);
        Mockito.doReturn(rule).when(ruleAnswer).getRule();


        answer.getRuleAnswers().add(ruleAnswer);

        return auditObject;
    }

    private AuditObject buildAuditObjectWithObjectDescription(Response response, ObjectDescription objectDescription) {
        AuditObject auditObject = buildAuditObjectWithObjectDescription(objectDescription);
        // auditObject.setGlobalResponse(response);


        return auditObject;
    }

    private AuditObject buildAuditObjectWithObjectDescription(ObjectDescription objectDescription) {
        AuditObject auditObject = Mockito.spy(new AuditObject(audit, objectDescription));


        auditObject.setName(objectDescription.getDescription());
        auditObject.setObjectDescriptionId(objectDescription.getName());
        Mockito.doReturn(objectDescription).when(auditObject).getObjectDescription();

        return auditObject;
    }

    private ObjectDescription buildObjectDescription(String name) throws URISyntaxException {
        ObjectDescription objectDescription = new ObjectDescription();
        objectDescription.setName(name);
        objectDescription.setDescription(name);
        objectDescription.setIcon(new File("src/test/resources/icon", String.format("%s.png", name)).toURI());

        return objectDescription;
    }

    private void buildAndCompileAuditDocxExporter() throws DocxExporterException {
        DocxExporter exporter = new AuditDocxExporter(audit, templateDirectory);
        exporter.compile(workingDirectory);
    }

}


