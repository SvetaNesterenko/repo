package teammates.common.datatransfer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FeedbackSessionQuestionsBundle {

    public FeedbackSessionAttributes feedbackSession = null;
    public Map<FeedbackQuestionAttributes, List<FeedbackResponseAttributes>>
        questionResponseBundle = null;
    public Map<String, Map<String, String>> recipientList = null;
    
    public FeedbackSessionQuestionsBundle(FeedbackSessionAttributes feedbackSession,
            Map<FeedbackQuestionAttributes, List<FeedbackResponseAttributes>> questionResponseBundle,
            Map<String, Map<String, String>> recipientList) {
        this.feedbackSession = feedbackSession;
        this.questionResponseBundle = questionResponseBundle;
        this.recipientList = recipientList;
    }
    
    /**
     * Gets the list of questions in this bundle, sorted by question number. 
     * @return A {@code List} of {@code FeedackQuestionAttributes}.
     */
    public List<FeedbackQuestionAttributes> getSortedQuestions() {
        
        List<FeedbackQuestionAttributes> sortedQuestions =
                new ArrayList<FeedbackQuestionAttributes>(this.questionResponseBundle.keySet());
        
        Collections.sort(sortedQuestions);
        
        return sortedQuestions;
    }
    
    /**
     * Gets the question in the data bunde with id == questionId
     * @param questionId
     * @return a FeedbackQuestionAttribute with the specified questionId
     */
    public FeedbackQuestionAttributes getQuestionAttributes(String questionId){
        List<FeedbackQuestionAttributes> questions =
                new ArrayList<FeedbackQuestionAttributes>(this.questionResponseBundle.keySet());
        
        for(FeedbackQuestionAttributes question : questions){
            if(question.getId().equals(questionId)){
                return question;
            }
        }
        
        return null;
    }
    
    /**
     * Gets the recipient list for a question, sorted by the recipient's name. 
     * @param feedbackQuestionId of the question
     * @return A {@code Map<String key, String value>} where {@code key} is the recipient's email
     * and {@code value} is the recipients name.
     */
    public Map<String, String> getSortedRecipientList(String feedbackQuestionId) {

        List<Map.Entry<String, String>> sortedList =
                new ArrayList<Map.Entry<String, String>>(this.recipientList
                        .get(feedbackQuestionId).entrySet());

        Collections.sort(sortedList, new Comparator<Map.Entry<String, String>>() {
            public int compare(
                    Map.Entry<String, String> o1,
                    Map.Entry<String, String> o2) {
                // Sort by value (name).
                int compare = o1.getValue().compareTo(o2.getValue());
                // Sort by key (email) if name is same.
                return compare == 0 ? o1.getKey().compareTo(o2.getKey()) : compare;
            }
        });

        Map<String, String> result = new LinkedHashMap<String, String>();

        for (Map.Entry<String, String> entry : sortedList) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    
    public Set<String> getRecipientEmails(String feedbackQuestionId) {
        List<Map.Entry<String, String>> emailList =
                new ArrayList<Map.Entry<String, String>>(this.recipientList
                        .get(feedbackQuestionId).entrySet());
        
        HashSet<String> result = new HashSet<String>();

        for (Map.Entry<String, String> entry : emailList) {
            result.add(entry.getKey());
        }
        return result;
    }
    
    /**
     * Empties responses for all questions in this bundle.
     * Used to not show existing responses when previewing as instructor 
     */
    public void resetAllResponses() {
        for(FeedbackQuestionAttributes question : questionResponseBundle.keySet()) {
            questionResponseBundle.put(question, new ArrayList<FeedbackResponseAttributes>());
        }
    }
}
