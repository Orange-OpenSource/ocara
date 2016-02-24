/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model.loader;

import android.util.Base64;

import com.orange.ocara.modelStatic.Accessibility;
import com.orange.ocara.modelStatic.Category;
import com.orange.ocara.modelStatic.Handicap;
import com.orange.ocara.modelStatic.HandicapAccessibilities;
import com.orange.ocara.modelStatic.Illustration;
import com.orange.ocara.modelStatic.ObjectDescription;
import com.orange.ocara.modelStatic.Question;
import com.orange.ocara.modelStatic.Response;
import com.orange.ocara.modelStatic.Rule;
import com.orange.ocara.modelStatic.RuleSet;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

public class RuleSetJsonParser extends AbstractJsonParser {

    @Getter
    private RuleSet ruleSet;

    @Setter
    private File pictosPath = new File("./pictos");
    @Setter
    private File imagesPath = new File("./images");


    @Getter
    private Map<String, Question> questions = null;
    private Map<String, ObjectDescription> objectDescriptionsByName = null;
    private Map<String, List<Illustration>> illustrations = null;
    private List<Category> categories = new ArrayList<Category>();

    protected static class LinkedIllustration {
        Illustration illustration;
        String nextIllustrationId;
    }

    @SuppressWarnings("unchecked")
    protected void doLoad(InputStream jsonStream) throws IOException {

        HashMap content = readValue(jsonStream, HashMap.class);

        ruleSet = new RuleSet();

        ruleSet.setType(extractString(content, "type"));
        ruleSet.setVersion(extractString(content, "version"));
        ruleSet.setLanguage(extractString(content, "language"));


        Map jsonIllustrations = extractMap(content, "illustrations");
        illustrations = loadIllustrations(jsonIllustrations);

        Map jsonQuestions = extractMap(content, "questions");
        questions = loadQuestions(jsonQuestions);
        ruleSet.setQuestionsById(questions);


        Map jsonQuestionsTree = extractMap(content, "rules");
        loadQuestionsTree(jsonQuestionsTree, questions);

        Collection<Map> jsonHandicaps = extractCollection(content, "handicaps");
        loadHandicaps(jsonHandicaps);


        Collection<Map> jsonObjectDescription = extractCollection(content, "objects");

        objectDescriptionsByName = loadObjectDescriptions(jsonObjectDescription);

        loadCategories(content);


        ruleSet.setCategories(categories);
        ruleSet.getObjectDescriptions().addAll(objectDescriptionsByName.values());
    }


    private void loadHandicaps(Collection<Map> jsonHandicaps) {

        Map<String, Handicap> handicaps = new LinkedHashMap<String, Handicap>();
        ruleSet.setHandicapsById(handicaps);

        for (Map jsonHandicap : jsonHandicaps) {
            Handicap handicap = new Handicap();
            // name
            handicap.setName(extractString(jsonHandicap, "label"));

            // id
            String handicapId = extractString(jsonHandicap, "idHandicap");


            // icon
            String icon = extractString(jsonHandicap, "picto");
            File iconPath = new File(pictosPath, icon);
            if (!iconPath.exists()) {
                throw new JsonBadStructureException(icon);
            } else {
                handicap.setIcon(iconPath.toURI());
            }

            handicaps.put(handicapId, handicap);
        }
    }

    private void loadCategories(HashMap content) {
        Collection<Map> jsonCategoriesArray = extractCollection(content, "sites");
        if (jsonCategoriesArray.size() > 0) {
            Map<String, Object> jsonCategoriesWrapperObject = (Map<String, Object>)jsonCategoriesArray.iterator().next();
            String type = (String)jsonCategoriesWrapperObject.get("type");
            Collection<Map> jsonCategories = extractCollection(jsonCategoriesWrapperObject, "areas");
            for(Map jsonCategory : jsonCategories) {
                Category category = loadCategory(jsonCategory);
                categories.add(category);
            }
        } else {
            throw new JsonBadStructureException("missing or empty 'sites' fields");
        }
    }

    private Category loadCategory(Map jsonCategory) {
        Category category  = new Category();

        String categoryName = extractString(jsonCategory, "desc");
        category.setName(categoryName);

        Collection<String> objectRefs = extractCollection(jsonCategory, "objects");
        for(String objectRef : objectRefs) {
            // get corresponding object
            ObjectDescription objectDescription = objectDescriptionsByName.get(objectRef);
            if (objectDescription == null) {
                throw new JsonBadStructureException("category '"+categoryName+"' references an invalid object reference : " + objectRef);
            }

            category.getObjects().add(objectDescription);
        }
        return category;
    }



    @SuppressWarnings("unchecked")
    private Map<String, List<Illustration>> loadIllustrations(Map jsonIllustrations) {
        Map<String, List<Illustration>> result = new LinkedHashMap<String,  List<Illustration>>();
        Map<String, LinkedIllustration> illustrations = new LinkedHashMap<String, LinkedIllustration>();


        Set<Map.Entry> entrySet = jsonIllustrations.entrySet();
        for (Map.Entry jsonEntry : entrySet) {
            String id = (String) jsonEntry.getKey();
            Map jsonIllustration = (Map) jsonEntry.getValue();

            Illustration illustration = new Illustration();

            String image = extractString(jsonIllustration, "image", false);
            if (!StringUtils.isBlank(image)) {

                File illustrationPath = new File(imagesPath, image);
                if (!illustrationPath.exists()) {
                    throw new JsonBadStructureException(image);
                }

                illustration.setImage( illustrationPath.toURI() );
            }


            illustration.setTitle(extractString(jsonIllustration, "title", false));
            illustration.setComment(extractString(jsonIllustration, "comment", false));
            LinkedIllustration linkedIllustration = new LinkedIllustration();
            linkedIllustration.illustration = illustration;

            linkedIllustration.nextIllustrationId = extractString(jsonIllustration, "next", false);
            illustrations.put(id, linkedIllustration);
        }

        // resolve illustrations chaining
        for (String id : illustrations.keySet()) {
            List<Illustration> chain = new ArrayList<>();
            String illustrationId = id;
            while (StringUtils.isNotBlank(illustrationId)) {
                LinkedIllustration linkedIllustration = illustrations.get(illustrationId);
                chain.add(linkedIllustration.illustration);
                illustrationId = linkedIllustration.nextIllustrationId;
            }
            result.put(id, chain);
        }
        return result;
    }


    @SuppressWarnings("unchecked")
    private Map<String, Question> loadQuestions(Map jsonQuestions) {
        Map<String, Question> result = new LinkedHashMap<String, Question>();


        Set<Map.Entry> entrySet = jsonQuestions.entrySet();
        for (Map.Entry jsonEntry : entrySet) {
            String questionId = (String) jsonEntry.getKey();
            Map jsonQuestion = (Map) jsonEntry.getValue();

            Question question = new Question();
            question.setQuestionId(questionId);
            question.setSubject(extractString(jsonQuestion, "subject"));
            question.setTitle(extractString(jsonQuestion, "text"));

            Collection<Map> jsonDoubts = extractCollection(jsonQuestion, "doubts");
            Collection<Rule> rules = loadQuestionRules(jsonDoubts);
            for (Rule rule : rules) {
                question.addRule(rule);
            }

            result.put(questionId, question);
        }
        return result;
    }

    private void loadQuestionsTree(Map jsonQuestionsTree, Map<String, Question> questions) {
        Set<Map.Entry> entrySet = jsonQuestionsTree.entrySet();
        for (Map.Entry jsonEntry : entrySet) {
            String questionId = (String) jsonEntry.getKey();
            Question question = questions.get(questionId);
            if (question == null) {
                throw new JsonBadStructureException("Bad question ref '" + questionId + "' while building question tree");
            }
            Map<String, String> branches = (Map<String, String>) jsonEntry.getValue();
            for (String response : branches.keySet()) {
                Response responseType = toResponseType(response);
                String nextQuestionId = branches.get(response);
                if (nextQuestionId.equals(questionId)) {
                    continue;
                }
                Question nextQuestion = questions.get(nextQuestionId);
                if (nextQuestion == null) {
                    throw new JsonBadStructureException("Bad question ref '" + nextQuestionId + "' while building question tree, response '" + response + "'");
                }
                question.getNextQuestionByResponseType().put(responseType, nextQuestion);
            }

        }
    }


    @SuppressWarnings("unchecked")
    protected Map<String, ObjectDescription> loadObjectDescriptions(Collection<Map> jsonObjectDescription) {
        Map<String, ObjectDescription> result  = new LinkedHashMap<String, ObjectDescription>();

        for (Map jsonObjDesc : jsonObjectDescription) {
            ObjectDescription objDesc = new ObjectDescription();
            // name
            objDesc.setName(extractString(jsonObjDesc, "id"));

            // description
            objDesc.setDescription(extractString(jsonObjDesc, "name"));

            // definition
            objDesc.setDefinition(extractString(jsonObjDesc, "definition", false));

            // icon
            String icon = extractString(jsonObjDesc, "icon");
            File iconPath = new File(pictosPath, icon);
            if (!iconPath.exists()) {
                throw new JsonBadStructureException(icon);
            }
            objDesc.setIcon(iconPath.toURI());

            // illustrations
            List<Illustration> illustrationChain = extractIllustration(jsonObjDesc);
            objDesc.setIllustrations(illustrationChain);

            // questions
            Collection<String> questionReferences = extractCollection(jsonObjDesc, "questions");
            for (String questionId : questionReferences) {
                Question question = questions.get(questionId);
                if (question == null) {
                    throw new JsonUnknownReferenceException("questions", questionId);
                }
                objDesc.getQuestions().add(question);
            }
            result.put(objDesc.getName(), objDesc);
        }

        // second pass to resolve children objects
        for (Map jsonObjDesc : jsonObjectDescription) {
            // name
            String name = extractString(jsonObjDesc, "id");
            ObjectDescription objDesc = result.get(name);

            // children objects
            Collection<String> childrenNames = extractCollection(jsonObjDesc, "objects");

            for (String childName : childrenNames) {
                ObjectDescription child = result.get(childName);
                if (child == null) {
                    throw new JsonUnknownReferenceException("objects", childName);
                }
                if (child.getName().equals(objDesc.getName())) {
                    throw new JsonBadStructureException("object description cycle detected in '"+objDesc.getName()+"'");
                }
                objectChildrenTreeDoesNotContainsObject(child, objDesc);
                objDesc.getChildren().add(child);
            }
        }

        return result;
    }

    private void objectChildrenTreeDoesNotContainsObject(ObjectDescription object, ObjectDescription child) {
        for(ObjectDescription aChildOfObject : object.getChildren()) {
            if (aChildOfObject.getName().equals(child.getName())) {
                throw new JsonBadStructureException("object description cycle detected in '"+object.getName()+"'");
            }
            objectChildrenTreeDoesNotContainsObject(aChildOfObject, child);
        }
    }


    protected Collection<Rule> loadQuestionRules(Collection<Map> jsonRules) {
        List<Rule> result = new ArrayList<Rule>();

        for (Map jsonRule : jsonRules) {
            Rule rule = new Rule();
            rule.setId(extractString(jsonRule, "id"));
            rule.setDescription(extractString(jsonRule, "text"));

            List<Illustration> illustrationChain = extractIllustration(jsonRule);
            rule.setIllustrations(illustrationChain);

            HandicapAccessibilities handicapAccessibilities = loadAccessibilityValue(jsonRule);
            rule.setHandicapAccessibilities(handicapAccessibilities);
            result.add(rule);
        }
        return result;
    }
    protected HandicapAccessibilities loadAccessibilityValue(Map jsonRule) {
        HandicapAccessibilities result = new HandicapAccessibilities();

        try {
            Map<String, String> handicapValues = extractMap(jsonRule, "handicaps");
            for (String handicapId : handicapValues.keySet()) {
                String valueAsString  = handicapValues.get(handicapId);
                Accessibility value = toAccessibility(valueAsString);
                result.setHandicapAccessibility(handicapId, value);
            }
        }catch(JsonMissingFieldException e) {
            return result;
        }
        return result;

    }


    private void storeResourcesIfNeeded(HashMap content, String resourcesName, File resourcesPath) throws IOException {
        if (resourcesPath.exists()) {
            return; // do nothing
        }
        resourcesPath.mkdirs();
        Map jsonResources = extractMap(content, resourcesName);


        Set<Map.Entry> entrySet = jsonResources.entrySet();
        for (Map.Entry resourceEntry : entrySet) {
            String resourceId = (String) resourceEntry.getKey();
            String resourceData = (String) resourceEntry.getValue();
            storeResource(resourceId, resourceData, resourcesPath);
        }

    }

    private void storeResource(String resourceId, String resourceData, File resourcesPath) throws IOException {
        String base64Pattern = "base64,";
        int indexOfBase64Data = resourceData.lastIndexOf(base64Pattern);
        if (indexOfBase64Data < 0) {
            throw new JsonBadStructureException(resourceId);
        }
        String base64String = resourceData.substring(indexOfBase64Data + base64Pattern.length());
        byte[] decodedData = Base64.decode(base64String, Base64.DEFAULT);

        File file = new File(resourcesPath, resourceId);

        FileOutputStream ouputStream = new FileOutputStream(file);
        ouputStream.write(decodedData);
        ouputStream.close();
    }




    private List<Illustration> extractIllustration(Map json) {
        String illustrationId = extractString(json, "illustration", false);
        if (StringUtils.isBlank(illustrationId)) {
            return Collections.emptyList();
        }

        List<Illustration> illustrationChain = illustrations.get(illustrationId);

        if (illustrationChain == null) {
            return Collections.emptyList();
        }
        return illustrationChain;
    }


    private Response toResponseType(String response) {
        if ("V".equals(response)) {
            return Response.OK;
        } else if ("NV".equals(response)) {
            return Response.NOK;
        }
        return null;
    }




    private Accessibility toAccessibility(String accessibility) {
        if ("B".equals(accessibility)) {
            return Accessibility.BLOCKING;
        } else if ("G".equals(accessibility)) {
            return Accessibility.ANNOYING;
        }
        return Accessibility.NO_IMPACT;
    }

}


